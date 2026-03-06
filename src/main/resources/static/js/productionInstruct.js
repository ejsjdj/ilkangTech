document.addEventListener("DOMContentLoaded", function () {
  const Grid = tui.Grid;

  // ===========================
  // 1️⃣ Grid 생성
  // ===========================
  const grid = new Grid({
    el: document.getElementById("grid"),
    bodyHeight: 400,
    scrollX: false,
    scrollY: false,
    pageOptions: {
      useClient: true,
      perPage: 10,
    },
    columns: [
      { header: "작업지시코드", name: "instructCode" },
      { header: "품목코드", name: "item" },
      { header: "지시수량", name: "instructQty" },
      { header: "시작시간", name: "startDate" },
      { header: "종료시간", name: "endDate" },
      { header: "상태", name: "status" },
      {
        header: "상세",
        name: "detail",
        formatter: () => {
          return `<button class="detail-btn">상세보기</button>`;
        },
      },
    ],
  });

  grid.on("click", function (ev) {
    if (ev.nativeEvent.target.classList.contains("detail-btn")) {
      const rowData = grid.getRow(ev.rowKey);
      openDetailModal(rowData.id);
    }
  });

  // ===========================
  // 2️⃣ 데이터 조회
  // ===========================
  function loadData() {
    const keyword = document.getElementById("keyword").value || "";

    fetch(`/api/production_instruct/list?keyword=${keyword}&page=0&size=100`)
      .then((res) => res.json())
      .then((data) => {
        grid.resetData(data.content || []);
      })
      .catch((err) => {
        console.error("조회 실패:", err);
        alert("데이터 조회 중 오류 발생");
      });
  }

  // ===========================
  // 3️⃣ 검색 버튼 이벤트
  // ===========================
  document.getElementById("searchBtn").addEventListener("click", loadData);

  // 최초 로딩
  loadData();
});

function openDetailModal(instructId) {
  fetch(`/api/production_instruct/detail?instructId=${instructId}`)
    .then((res) => res.json())
    .then((data) => {
      // 상단 기본정보 표시
      document.getElementById("detailInfo").innerHTML = `
        <p><b>생산계획 ID:</b> ${data.planeId}</p>
        <p><b>작업지시코드:</b> ${data.instructCode}</p>
        <p><b>생산계획코드:</b> ${data.planeCode}</p>
        <p><b>품목:</b> ${data.itemName}</p>
        <p><b>지시수량:</b> ${data.instructQty}</p>
      `;

      // 하단 작업자 테이블
      const tbody = document.getElementById("workerTable");
      tbody.innerHTML = "";

      data.worker.forEach((w) => {

        const commonData = `
          data-worker-id="${w.workerId}" 
          data-instruct-id="${data.instructId}"
          data-plane-id="${data.planeId}"
        `;

        tbody.innerHTML += `
          <tr>
            <tr>
            <td>${w.operationName}</td>
            <td>${w.name}</td>
            <td>${w.startTime}</td>
            <td>${w.endTime}</td>
            <td>${w.productionQty}</td>
            <td>${w.additionQty}</td>
            <td>${w.status}</td>
            <td><button class="btn-start" ${commonData} onclick="handleWork(this, 'start')">시작</button></td>
            <td><button class="btn-stop" ${commonData} onclick="handleWork(this, 'stop')">중단</button></td>
            <td><button class="btn-complete" ${commonData} onclick="handleWork(this, 'complete')">완료</button></td>
            <td><button class="btn-defect" ${commonData} onclick="handleWork(this, 'defect')">등록</button></td>
          </tr>
          </tr>
          </tr>
        `;
      });

      document.getElementById("detailModal").style.display = "block";
    })
    .catch((err) => {
      console.error(err);
      alert("상세 조회 실패");
    });
}

document.querySelector(".close").onclick = function () {
  document.getElementById("detailModal").style.display = "none";
};

window.onclick = function (event) {
  const modal = document.getElementById("detailModal");
  if (event.target === modal) {
    modal.style.display = "none";
  }
};

document.getElementById("createBtn").addEventListener("click", function () {
  window.location.href = "/production/instruct_register";
});

function handleWork(button, type) {
  // dataset에서 ID 추출
  const workerId = button.dataset.workerId;
  const instructId = button.dataset.instructId;
  const planeId = button.dataset.planeId;

  console.log(`[${type}] 요청 - 작업자ID: ${workerId}, 지시ID: ${instructId}, 계획ID: ${planeId}`);

  // 타입별 URL 설정 (필요에 따라 수정)
  const urlMap = {
    start: '/api/production_instruct/start',
    stop: '/api/production_instruct/stop',
    complete: '/api/production_instruct/complete',
    defect: '/api/production_instruct/defect'
  };

  const url = `${urlMap[type]}?planeId=${planeId}&instructId=${instructId}&workerId=${workerId}`;

  fetch(url, {
    method: 'POST', // 서버 컨트롤러가 @PostMapping이면 POST 유지
    headers: { 'Content-Type': 'application/json' }
  })
    .then(res => {
      if (res.ok) {
        alert("처리가 완료되었습니다.");
        location.reload(); // 성공 시 화면 갱신 (상태 변경 반영)
      } else {
        alert("처리 중 오류가 발생했습니다.");
      }
    })
    .catch(err => console.error("API 호출 에러:", err));
}
