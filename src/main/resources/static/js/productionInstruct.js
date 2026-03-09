let selectedDefectData = null;

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
      {
        header: "시작시간",
        name: "startDate",
        formatter: ({ value }) => formatDateTime(value), // 가공 함수 적용
      },
      {
        header: "종료시간",
        name: "endDate",
        formatter: ({ value }) => formatDateTime(value), // 가공 함수 적용
      },
      {
        header: "상태",
        name: "status",
        formatter: ({ value }) => formatStatus(value), // 한글 변환 적용
      },
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

  // ===========================
  // 3️⃣ 불량 수량 등록 모달
  // ===========================
  document.querySelector(".close-defect").onclick = function () {
    document.getElementById("defectModal").style.display = "none";
  };

  // 불량 등록 완료 버튼 클릭 이벤트
  document.getElementById("submitDefect").onclick = function () {
    const qty = document.getElementById("defectiveInput").value;
    if (!qty || qty < 0) {
      alert("올바른 수량을 입력해주세요.");
      return;
    }
    sendDefectData(qty);
  };

  // 최초 로딩
  loadData();
});

function openDetailModal(instructId) {
  fetch(`/api/production_instruct/detail?instructId=${instructId}`)
    .then((res) => res.json())
    .then((data) => {
      // 상단 기본정보 표시 (그리드 박스 레이아웃)
      document.getElementById("detailInfo").innerHTML = `
                <div class="detail-info-grid">
                    <div class="info-item">
                        <label>생산계획 ID</label>
                        <div class="info-box">${data.planeId || "-"}</div>
                    </div>
                    <div class="info-item">
                        <label>작업지시코드</label>
                        <div class="info-box" style="color:#4f46e5; font-weight:700;">${data.instructCode || "-"}</div>
                    </div>
                    <div class="info-item">
                        <label>생산계획코드</label>
                        <div class="info-box">${data.planeCode || "-"}</div>
                    </div>
                    <div class="info-item">
                        <label>품목명</label>
                        <div class="info-box">${data.itemName || "-"}</div>
                    </div>
                    <div class="info-item">
                        <label>지시수량</label>
                        <div class="info-box">${data.instructQty?.toLocaleString() || "0"}</div>
                    </div>
                    <div class="info-item">
                        <label>상태</label>
                        <div class="info-box">${formatStatus(data.status)}</div>
                    </div>
                </div>
            `;

      // 하단 작업자 테이블
      const tbody = document.getElementById("workerTable");
      tbody.innerHTML = "";

      if (data.worker && data.worker.length > 0) {
        data.worker.sort((a, b) => a.sequence - b.sequence);

        data.worker.forEach((w) => {
          const commonData = `data-worker-id="${w.workerId}" data-instruct-id="${data.instructId}" data-plane-id="${data.planeId}"`;

          tbody.innerHTML += `
                        <tr>
                            <td><span class="badge bg-secondary">${w.sequence}</span></td>
                            <td class="fw-bold">${w.operationName}</td>
                            <td>${w.name}</td>
                            <td>${formatDateTime(w.startTime)}</td>
                            <td>${formatDateTime(w.endTime)}</td>
                            <td class="text-primary fw-bold">${w.productionQty}</td>
                            <td class="text-danger">${w.additionQty}</td>
                            <td>${formatStatus(w.status)}</td>
                            <td><button class="btn btn-outline-primary btn-sm" ${commonData} onclick="handleWork(this, 'start')">시작</button></td>
                            <td><button class="btn btn-outline-warning btn-sm" ${commonData} onclick="handleWork(this, 'stop')">중단</button></td>
                            <td><button class="btn btn-outline-success btn-sm" ${commonData} onclick="handleWork(this, 'complete')">완료</button></td>
                            <td><button class="btn btn-outline-danger btn-sm" ${commonData} onclick="handleWork(this, 'defect')">등록</button></td>
                        </tr>
                    `;
        });
      } else {
        tbody.innerHTML =
          '<tr><td colspan="12" class="text-center p-4">할당된 작업 공정이 없습니다.</td></tr>';
      }

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

  console.log(
    `[${type}] 요청 - 작업자ID: ${workerId}, 지시ID: ${instructId}, 계획ID: ${planeId}`,
  );

  // 타입별 URL 설정
  const urlMap = {
    start: "/api/production_instruct/start",
    stop: "/api/production_instruct/cancel",
    complete: "/api/production_instruct/complete",
    defect: "/api/production_instruct/defect",
  };

  const url = `${urlMap[type]}?planeId=${planeId}&instructId=${instructId}&workerId=${workerId}`;

  fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
  })
    .then((res) => {
      if (res.ok) {
        alert("처리가 완료되었습니다.");
        location.reload();
      } else {
        alert("처리 중 오류가 발생했습니다.");
      }
    })
    .catch((err) => console.error("API 호출 에러:", err));
}

function handleWork(button, type) {
  const workerId = button.dataset.workerId;
  const instructId = button.dataset.instructId;
  const planeId = button.dataset.planeId;

  // 불량 등록인 경우 모달만 띄우고 리턴
  if (type === "defect") {
    selectedDefectData = { workerId, instructId };
    document.getElementById("defectiveInput").value = "";
    document.getElementById("defectModal").style.display = "block";
    return;
  }

  // 나머지(start, stop, complete) 로직은 동일
  const urlMap = {
    start: "/api/production_instruct/start",
    stop: "/api/production_instruct/cancel",
    complete: "/api/production_instruct/complete",
  };

  const url = `${urlMap[type]}?planeId=${planeId}&instructId=${instructId}&workerId=${workerId}`;

  fetch(url, { method: "POST" }).then((res) => {
    if (res.ok) {
      alert("처리가 완료되었습니다.");
      location.reload();
    } else {
      alert("처리 중 오류 발생");
    }
  });
}

// 실제 불량 데이터를 서버로 전송하는 함수
function sendDefectData(defectiveQty) {
  const { instructId, workerId } = selectedDefectData;

  // 컨트롤러의 @RequestParam 명칭과 일치하도록 쿼리 스트링 구성
  const url = `/api/production_instruct/defective?DefectiveQty=${defectiveQty}&instructId=${instructId}&workerId=${workerId}`;

  fetch(url, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
  })
    .then((res) => {
      if (res.ok) {
        alert("불량 등록이 완료되었습니다.");
        location.reload();
      } else {
        alert("등록 실패");
      }
    })
    .catch((err) => console.error("API 에러:", err));
}

function formatDateTime(dateStr) {
  if (!dateStr || dateStr === "-" || dateStr.trim() === "") {
    return "-";
  }

  if (dateStr.includes("T")) {
    return dateStr.split("T")[1].substring(0, 5); // "04:00"
  }

  const date = new Date(dateStr);

  if (isNaN(date.getTime())) {
    return "-";
  }

  const year = date.getFullYear();
  const month = String(date.getMonth() + 1).padStart(2, "0");
  const day = String(date.getDate()).padStart(2, "0");
  const hours = String(date.getHours()).padStart(2, "0");
  const minutes = String(date.getMinutes()).padStart(2, "0");

  return `${year}-${month}-${day} ${hours}:${minutes}`;
}

function formatStatus(status) {
  const statusMap = {
    COMPLETE: "완료",
    PROGRESS: "생산중",
    READY: "대기",
    CAN: "중단",
  };

  // 매핑되는 값이 없으면 원본 status 그대로 출력 (혹은 '-')
  return statusMap[status] || status || "-";
}
