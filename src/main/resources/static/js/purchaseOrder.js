let selectedLineId = null;

document.addEventListener("DOMContentLoaded", function () {
  const Grid = tui.Grid;

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
      { header: "발주번호", name: "purchaseOrderCode" },
      { header: "거래처명", name: "company" },
      { header: "담당자", name: "companyManager" },
      { header: "전화번호", name: "phone" },
      { header: "발주담당자", name: "name" },
      { header: "상태", name: "status" },
      { header: "발주일자", name: "orderDate" },
      { header: "총금액", name: "amount" },
      {
        header: "상세",
        name: "detail",
        align: "center",
        formatter: () => {
          return '<button class="detail-btn">상세보기</button>';
        },
      },
    ],
  });

  // 🔹 상세 버튼 클릭
  grid.on("click", (ev) => {
    if (ev.columnName === "detail") {
      const rowData = grid.getRow(ev.rowKey);

      fetch(`/api/sales/procurement_detail?purchaseOrderId=${rowData.id}`)
        .then((res) => res.json())
        .then((data) => {
          console.log("상세 응답:", data);

          if (!data) {
            alert("상세 데이터가 없습니다.");
            return;
          }

          // ===== 상단 정보 매핑 =====
          document.getElementById("modalPurchaseOrderCode").value =
            data.purchaseOrderCode ?? "";
          document.getElementById("modalCompany").value = data.company ?? "";
          document.getElementById("modalManager").value =
            data.companyManager ?? "";
          document.getElementById("modalPhone").value = data.phone ?? "";
          document.getElementById("modalName").value = data.name ?? "";
          document.getElementById("modalOrderDate").value =
            data.orderDate ?? "";
          document.getElementById("modalAmount").value = data.amount ?? "";

          // ===== 라인 테이블 매핑 =====
          const tbody = document.getElementById("modalLineBody");
          tbody.innerHTML = "";

          if (
            data.purchaseOrderLineDto &&
            data.purchaseOrderLineDto.length > 0
          ) {
            data.purchaseOrderLineDto.forEach((line) => {
              const row = document.createElement("tr");

              row.innerHTML = `
                <td>${line.id}</td>
                <td>${line.item}</td>
                <td>${line.quantity}</td>
                <td>${line.totalPrice}</td>
                <td>
                  <button onclick="openReturnModal(${line.purchaseRequestLineId})">
                    반품
                  </button>
                </td>
              `;

              tbody.appendChild(row);
            });
          }

          document.getElementById("detailModal").style.display = "block";
        });
    }
  });

  // 🔹 목록 조회
  fetch("/api/sales/procurement")
    .then((res) => res.json())
    .then((data) => {
      console.log("목록 데이터:", data);
      grid.resetData(data.content);
    });
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
