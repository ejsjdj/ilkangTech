let detailGrid = null;
let currentPlaneId = null;

document.addEventListener("DOMContentLoaded", function () {
  const Grid = tui.Grid;

  // ===========================
  // 1️⃣ 메인 목록 Grid 생성
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
      { header: "ID", name: "id", hidden: true },
      { header: "계획코드", name: "planeCode" },
      { header: "계획일자", name: "planeDate" },
      { header: "등록자", name: "memberName" },
      { header: "품목명", name: "itemName" },
      { header: "총생산수량", name: "totalQty" },
      {
        header: "상태",
        name: "status",
        align: "center",
        formatter: ({ value }) => {
          // 메인 그리드에서도 색상이 나오도록 포맷터 추가
          const statusMap = {
            PROGRESS: { text: "생산중", class: "status-progress" },
            COMPLETE: { text: "생산완료", class: "status-complete" },
            CANCEL: { text: "중단", class: "status-cancel" },
            WAIT: { text: "대기중", class: "status-wait" },
          };
          const item = statusMap[value] || {
            text: value,
            class: "status-wait",
          };
          return `<span class="status-badge ${item.class}">${item.text}</span>`;
        },
      },
      {
        header: "상세",
        name: "detail",
        align: "center",
        formatter: () => '<button class="detail-btn">상세보기</button>',
      },
    ],
  });

  // ===========================
  // 2️⃣ 상세 버튼 클릭 이벤트
  // ===========================
  grid.on("click", (ev) => {
    if (ev.columnName !== "detail") return;

    const rowData = grid.getRow(ev.rowKey);

    if (!rowData?.id) {
      alert("잘못된 접근입니다. ID가 없습니다.");
      return;
    }

    fetch(`/api/production/plane_detail?productionId=${rowData.id}`)
      .then((res) => {
        if (!res.ok) throw new Error("상세 조회 실패");
        return res.json();
      })
      .then((data) => {
        currentPlaneId = rowData.id;

        // 상세 Grid 데이터 세팅 및 모달 열기
        openModal();
        setDetailForm(data); // 상단 정보 및 배지 업데이트
        detailGrid.resetData(data.details || []);
      })
      .catch((err) => {
        console.error(err);
        alert("상세 조회 중 오류 발생");
      });
  });

  // ===========================
  // 3️⃣ 검색 버튼
  // ===========================
  document.getElementById("searchBtn").addEventListener("click", loadData);

  // 최초 로딩
  loadData();

  // ===========================
  // 목록 조회 함수
  // ===========================
  function loadData() {
    const keyword = document.getElementById("keyword").value || "";

    fetch(`/api/production/plane?keyword=${keyword}`)
      .then((res) => {
        if (!res.ok) throw new Error("목록 조회 실패");
        return res.json();
      })
      .then((data) => {
        grid.resetData(data?.content || []);
      })
      .catch((err) => {
        console.error(err);
        alert("목록 조회 중 오류 발생");
      });
  }

  // ===========================
  // 4️⃣ 계획 취소 버튼
  // ===========================
  document.getElementById("cancelBtn").addEventListener("click", function () {
    if (!currentPlaneId) {
      alert("취소할 계획 ID가 없습니다.");
      return;
    }

    if (!confirm("정말로 이 생산계획을 취소하시겠습니까?")) return;

    fetch(`/api/production/plane_cancel?planeId=${currentPlaneId}`, {
      method: "POST",
    })
      .then((res) => {
        if (!res.ok) throw new Error("취소 실패");
        alert("생산계획이 취소되었습니다.");

        closeModal();
        loadData();
      })
      .catch((err) => {
        console.error(err);
        alert("취소 처리 중 오류 발생");
      });
  });
});

// ===========================
// 신규 등록 이동
// ===========================
function goRegister() {
  location.href = "/production/production_register";
}

// ===========================
// 모달 열기
// ===========================
function openModal() {
  document.getElementById("detailModal").style.display = "block";

  if (!detailGrid) {
    detailGrid = new tui.Grid({
      el: document.getElementById("detailGrid"),
      bodyHeight: 250,
      scrollX: false,
      scrollY: false,
      columns: [
        { header: "ID", name: "id", hidden: true },
        { header: "품목명", name: "itemName" },
        { header: "수주ID", name: "orderId", align: "center" },
        { header: "생산수량", name: "productQty", align: "center" },
        { header: "메모", name: "memo" },
        { header: "기한", name: "productionDetailDate", align: "center" },
      ],
    });
  }
}

// ===========================
// 모달 닫기
// ===========================
function closeModal() {
  document.getElementById("detailModal").style.display = "none";
  currentPlaneId = null;
}

// ===========================
// 상세 상단 폼 세팅 및 상태 배지 업데이트
// ===========================
function setDetailForm(data) {
  document.getElementById("d_planeCode").value = data?.planeCode || "";
  document.getElementById("d_planeDate").value = data?.planeDate || "";
  document.getElementById("d_memberName").value = data?.memberName || "";
  document.getElementById("d_itemName").value = data?.itemName || "";
  document.getElementById("d_totalQty").value = data?.totalQty || "";
  document.getElementById("d_memo").value = data?.memo || "";

  // 상태값 세팅 (Hidden input)
  if (document.getElementById("d_status")) {
    document.getElementById("d_status").value = data?.status || "";
  }

  // 상태 배지 업데이트 로직
  const badge = document.getElementById("d_status_badge");
  if (badge) {
    const status = data?.status || "WAIT";
    let text = "";
    let className = "status-badge ";

    switch (status) {
      case "COMPLETE":
        text = "생산완료";
        className += "status-complete";
        break;
      case "CANCEL":
        text = "중단";
        className += "status-cancel";
        break;
      case "PROGRESS":
        text = "생산중";
        className += "status-progress";
        break;
      case "WAIT":
      default:
        text = "대기중";
        className += "status-wait";
    }

    badge.innerText = text;
    badge.className = className;
  }
}
