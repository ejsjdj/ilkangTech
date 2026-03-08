let productionChart = null;
let itemChart = null;

/* ================= KPI 카드 ================= */
async function loadKpi() {
  const todayPlan = await fetch("/api/production_dashboard/today_plane").then(
    (r) => r.json(),
  );
  const todayComplete = await fetch(
    "/api/production_dashboard/today_complete_plane",
  ).then((r) => r.json());
  const processing = await fetch(
    "/api/production_dashboard/processing_plane",
  ).then((r) => r.json());
  const waiting = await fetch("/api/production_dashboard/waiting_plane").then(
    (r) => r.json(),
  );

  document.querySelector(".text-primary").innerText = todayPlan;
  document.querySelector(".text-success").innerText = todayComplete;
  document.querySelector(".text-info").innerText = processing;
  document.querySelector(".text-danger").innerText = waiting;
}

/* ================= 생산 추이 그래프 ================= */

async function loadChart(unit) {
  const res = await fetch(
    `/api/production_dashboard/date_by_plane?unit=${unit.toUpperCase()}`,
  );
  const data = await res.json();

  const result = fillMissingMonths(data);

  const categories = result.categories;
  const quantities = result.quantities;

  data.forEach((row) => {
    categories.push(row[0]);
    quantities.push(row[1]);
  });

  const options = {
    series: [
      {
        name: "생산수량",
        data: quantities,
      },
    ],
    chart: {
      type: "bar",
      height: 400,
      toolbar: { show: false },
    },
    plotOptions: {
      bar: {
        borderRadius: 4,
        columnWidth: "40%",
      },
    },
    xaxis: {
      categories: categories,
    },
  };

  if (!productionChart) {
    productionChart = new ApexCharts(
      document.querySelector("#productionChart"),
      options,
    );
    productionChart.render();
  } else {
    productionChart.updateOptions({
      xaxis: { categories: categories },
    });

    productionChart.updateSeries([
      {
        name: "생산수량",
        data: quantities,
      },
    ]);
  }
}

/* ================= 품목별 생산수량 ================= */

async function loadItemChart() {
  const res = await fetch("/api/production_dashboard/get_by_item");
  const data = await res.json();

  const items = [];
  const qty = [];

  data.forEach((row) => {
    items.push(row[0]);
    qty.push(row[1]);
  });

  const options = {
    series: [
      {
        name: "생산수량",
        data: qty,
      },
    ],
    chart: {
      type: "bar",
      height: 350,
    },
    plotOptions: {
      bar: {
        horizontal: false,
        borderRadius: 4,
      },
    },
    dataLabels: {
      enabled: false,
    },
    xaxis: {
      categories: items,
    },
  };

  itemChart = new ApexCharts(document.querySelector("#itemChart"), options);
  itemChart.render();
}

/* ================= 초기 로딩 ================= */

document.addEventListener("DOMContentLoaded", () => {
  loadKpi();

  loadChart("MONTH");

  loadItemChart();
});

// 기타 함수
function fillMissingMonths(data) {
  const map = new Map();

  data.forEach((row) => {
    map.set(row[0], row[1]);
  });

  const categories = [];
  const quantities = [];

  const now = new Date();

  for (let i = 5; i >= 0; i--) {
    const d = new Date(now.getFullYear(), now.getMonth() - i, 1);
    const key = d.toISOString().slice(0, 7);

    categories.push(key);
    quantities.push(map.get(key) || 0);
  }

  return { categories, quantities };
}
