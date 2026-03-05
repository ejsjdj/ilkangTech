let analysisGrid; // 전역 선언

document.addEventListener("DOMContentLoaded", function () {
  const Grid = tui.Grid;

  analysisGrid = new Grid({
    el: document.getElementById("analysisGrid"),
    bodyHeight: 300,
    scrollX: false,
    scrollY: false,
    columns: [
      { header: "제품ID", name: "itemId", align: "center" },
      { header: "생산수량", name: "productionQty", align: "center" },
      { header: "재고검증", name: "result", align: "center" },
      { header: "재고여부", name: "hasStock", align: "center" },
    ],
  });

  // 수주 선택
  document
    .getElementById("orderSelect")
    .addEventListener("change", function () {
      const orderId = this.value;

      if (!orderId) {
        analysisGrid.resetData([]);
        return;
      }

      let itemId = 1;
      let productionQty = 100;

      if (orderId === "2") {
        itemId = 2;
        productionQty = 200;
      }

      if (orderId === "3") {
        itemId = 3;
        productionQty = 50;
      }

      fetch(
        `/api/production/check?itemId=${itemId}&productionQty=${productionQty}`,
      )
        .then((res) => res.json())
        .then(() => {
          analysisGrid.resetData([
            {
              itemId: itemId,
              productionQty: productionQty,
              result: "생산 가능",
              hasStock: "O",
            },
          ]);
        })
        .catch(() => {
          analysisGrid.resetData([
            {
              itemId: itemId,
              productionQty: productionQty,
              result: "재고 부족",
              hasStock: "X",
            },
          ]);

          alert("재고 부족으로 생산 불가");
        });
    });

  // 🔥 Grid 클릭 이벤트도 여기 안에 넣어야 함
  analysisGrid.on("click", (ev) => {
    if (ev.targetType !== "cell") return;

    if (ev.columnName === "hasStock") {
      const rowData = analysisGrid.getRow(ev.rowKey);

      if (rowData.hasStock === "X") {
        showShortageModal(rowData);
      }
    }
  });
});

function goRegister() {
  const rows = analysisGrid.getData();

  if (rows.length === 0) {
    alert("생산 분석 데이터가 없습니다.");
    return;
  }

  const row = rows[0];

  if (row.result === "재고 부족") {
    alert("재고 부족으로 생산계획 등록 불가");
    return;
  }

  // 더미 ProductionPlaneInsertDTO 생성
  const requestData = {
    routeCode: 1,
    planeCode: "PLAN-001",
    planeDate: new Date().toISOString(),
    member: 1,
    item: row.itemId,
    totalQty: row.productionQty,
    status: "READY",
    memo: "테스트 생산계획",

    details: [
      {
        id: null,
        itemId: row.itemId,
        orderId: 1,
        productQty: row.productionQty,
        memo: "테스트 상세",
        //productionDetailDate: new Date().toISOString()
      },
    ],
  };

  console.log("보낼 데이터:", requestData);

  fetch("/api/production/regist_production", {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify(requestData),
  })
    .then((res) => res.json())
    .then((data) => {
      console.log(data);

      alert("생산계획 등록 완료");

      location.href = "/production/production_list";
    })
    .catch((err) => {
      console.error(err);

      alert("생산계획 등록 실패");
    });
}

// 4️⃣ 모달 표시 함수 (가상 원자재 계산 포함)
function showShortageModal(data) {
  const modal = document.getElementById("shortageModal");
  const content = document.getElementById("shortageContent");

  const shortageQty = data.orderQty - data.stockQty;

  // 인디고 테마에 맞춘 깔끔한 디자인
  let html = `
    <div style="background: #fff5f5; padding: 18px; border-radius: 12px; border: 1px solid #feb2b2;">
        <p style="font-size: 13px; color: #4a5568; margin-bottom:15px;">생산을 위해 다음 원자재가 필요합니다.</p>
        
        <div style="background: #ffffff; padding: 12px; border-radius: 8px; border: 1px solid #edf2f7;">
            <ul style="list-style: none; padding: 0; margin: 0; font-size: 14px;">
                <li style="display:flex; justify-content:space-between; margin-bottom: 8px; padding-bottom: 8px; border-bottom: 1px solid #f7fafc;">
                    <span>RESIN - WHITE</span>
                    <span style="color: #e53e3e; font-weight: bold;">${shortageQty * 1} kg</span>
                </li>
                <li style="display:flex; justify-content:space-between; margin-bottom: 8px; padding-bottom: 8px; border-bottom: 1px solid #f7fafc;">
                    <span>Stainless Steel Coil</span>
                    <span style="color: #e53e3e; font-weight: bold;">${shortageQty * 2} kg</span>
                </li>
                <li style="display:flex; justify-content:space-between;">
                    <span>Cold Rolled Coil</span>
                    <span style="color: #e53e3e; font-weight: bold;">${shortageQty * 0.5} kg</span>
                </li>
            </ul>
        </div>
    </div>
  `;

  content.innerHTML = html;
  modal.style.display = "block";
}

function closeShortageModal() {
  document.getElementById("shortageModal").style.display = "none";
}

// 자재 요청 버튼 클릭 시 실행
function requestMaterials() {
  // 1. 알림창 띄우기
  alert("자재 요청이 정상적으로 완료되었습니다.");

  // 2. 모달 닫기
  closeShortageModal();

  location.href = "/production/production_list";
}
