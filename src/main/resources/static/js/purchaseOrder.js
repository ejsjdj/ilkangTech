const statusMap = {
  READY: { text: "발주대기", class: "bg-ready" },
  CONFIRMED: { text: "확정", class: "bg-confirmed" },
  DELIVERY: { text: "배송중", class: "bg-delivery" },
  INSPECTION: { text: "검수중", class: "bg-inspection" },
  COMPLETE: { text: "검수완료", class: "bg-complete" },
  STORED: { text: "입고완료", class: "bg-stored" },
  RETURN: { text: "반송", class: "bg-return" },
};

let selectedLineId = null;
let currentPurchaseOrderId = null;

document.addEventListener("DOMContentLoaded", function () {
  const Grid = tui.Grid;

  const grid = new Grid({
    el: document.getElementById("grid"),

    data: {
      api: {
        readData: {
          url: "/api/sales/procurement",
          method: "GET",
        },
      },

      // ✅ 서버 응답 구조 매핑 (반드시 data 안에!)
      responseData: {
        data: "data.contents",
        totalCount: "data.pagination.totalCount",
      },

      // ✅ pageable + 검색 파라미터 전부 전송
      serializer(params) {
        let query = `page=${params.page - 1}&size=${params.perPage}`;

        if (params.keyword) query += `&keyword=${params.keyword}`;
        if (params.startDate) query += `&startDate=${params.startDate}`;
        if (params.endDate) query += `&endDate=${params.endDate}`;

        return query;
      },
    },

    pageOptions: {
      useClient: false,
      perPage: 10,
    },

    columns: [
      { header: "발주번호", name: "purchaseOrderCode" },
      { header: "거래처명", name: "company" },
      { header: "담당자", name: "companyManager" },
      { header: "전화번호", name: "phone" },
      { header: "발주담당자", name: "name" },
      {
        header: "상태",
        name: "status",
        align: "center",
        formatter: (props) => {
          const info = statusMap[props.value] || {
            text: props.value,
            class: "bg-ready",
          };
          return `<span class="status-badge ${info.class}">${info.text}</span>`;
        },
      },
      { header: "발주일자", name: "orderDate" },
      { header: "총금액", name: "amount" },
      {
        header: "상세",
        name: "detail",
        align: "center",
        formatter: () =>
          '<button class="btn-primary" style="padding:4px 10px;font-size:12px;">상세보기</button>',
      },
    ],
  });

  // ✅ 검색 버튼
  document.getElementById("searchBtn").addEventListener("click", () => {
    const startDate = document.getElementById("startDate").value;
    const endDate = document.getElementById("endDate").value;
    const keyword = document.getElementById("keyword").value;

    grid.readData(1, { startDate, endDate, keyword }, true);
  });

  // ✅ 상세 클릭
  grid.on("click", (ev) => {
    if (ev.columnName !== "detail") return;

    const rowData = grid.getRow(ev.rowKey);
    currentPurchaseOrderId = rowData.id;

    fetch(`/api/sales/procurement_detail?purchaseOrderId=${rowData.id}`)
      .then((res) => res.json())
      .then((data) => {
        if (!data) return;

        document.getElementById("modalPurchaseOrderCode").value =
          data.purchaseOrderCode ?? "";
        document.getElementById("modalCompany").value = data.company ?? "";
        document.getElementById("modalManager").value =
          data.companyManager ?? "";
        document.getElementById("modalPhone").value = data.phone ?? "";
        document.getElementById("modalName").value = data.name ?? "";
        document.getElementById("modalOrderDate").value = data.orderDate ?? "";
        document.getElementById("modalAmount").value = data.amount ?? "";

        const tbody = document.getElementById("modalLineBody");
        tbody.innerHTML = "";

        if (data.purchaseOrderLineDto) {
          data.purchaseOrderLineDto.forEach((line) => {
            console.log("라인 데이터 확인:", line);
            const row = document.createElement("tr");
            const actualLineId = line.id;

            row.innerHTML = `
              <td>${line.id}</td>
              <td>${line.itemName}</td>
              <td>${line.quantity}</td>
              <td>${line.totalPrice}</td>
              <td><button onclick="openReturnModal(${actualLineId})">반품</button></td>
            `;
            tbody.appendChild(row);
          });
        }

        const qcBtn = document.getElementById("completeQcBtn");
        qcBtn.style.display =
          data.status === "COMPLETE" || data.status === "STORED"
            ? "none"
            : "block";

        document.getElementById("detailModal").style.display = "block";
      });
  });

  // ✅ 검수 완료
  const completeQcBtn = document.getElementById("completeQcBtn");
  if (completeQcBtn) {
    completeQcBtn.addEventListener("click", () => {
      if (!currentPurchaseOrderId) return;
      if (!confirm("검수 완료 처리하시겠습니까?")) return;

      fetch(
        `/api/sales/procurement/complete_qc?purchaseOrderId=${currentPurchaseOrderId}`,
        {
          method: "POST",
        },
      )
        .then((res) => {
          if (!res.ok) throw new Error("처리 실패");
          alert("검수 완료되었습니다.");
          grid.reloadData(); // ✅ 전체 reload 대신 그리드만 갱신
        })
        .catch((err) => alert(err.message));
    });
  }
});

// =======================
// 페이지 이동
// =======================
function goRegister() {
  location.href = "/sales/procurement/register";
}

// =======================
// 모달 제어
// =======================
function closeDetailModal() {
  document.getElementById("detailModal").style.display = "none";
}

function openReturnModal(lineId) {
  console.log("선택된 구매요청라인 ID:", lineId);
  selectedLineId = lineId;
  document.getElementById("returnModal").style.display = "block";
}

function closeReturnModal() {
  document.getElementById("returnModal").style.display = "none";
}

// =======================
// 반품 처리
// =======================
function submitReturn() {
  const reason = document.getElementById("returnReason").value;
  const returnQty = document.getElementById("returnQty").value;

  if (!returnQty || !reason) {
    alert("반품수량과 사유를 입력하세요.");
    return;
  }

  if (!selectedLineId) {
    alert("선택된 라인이 없습니다.");
    return;
  }

  console.log("서버로 보내는 값:", {
    purchaseRequestDetailId: selectedLineId,
    returnQty: Number(returnQty),
    memo: reason,
  });

  fetch("/api/sales/procurement/return_insert", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      purchaseRequestDetailId: selectedLineId,
      returnQty: Number(returnQty),
      memo: reason,
    }),
  })
    .then((res) => {
      if (!res.ok) {
        throw new Error("반품 실패");
      }
      return res.text();
    })
    .then(() => {
      alert("반품 처리 완료");
      closeReturnModal();
      closeDetailModal();
      location.reload();
    })
    .catch((err) => {
      alert(err.message);
    });
}
