document.addEventListener("DOMContentLoaded", function () {
  const prSelect = document.getElementById("purchaseRequestSelect");
  const companySelect = document.getElementById("companySelect");
  const tbody = document.querySelector("#lineTable tbody");

  let companyList = [];

  /* =========================
     1️⃣ 구매요청 목록 조회
  ========================== */
  fetch("/api/sales/purchase_request_list")
    .then((res) => res.json())
    .then((data) => {
      data.forEach((pr) => {
        const option = document.createElement("option");
        option.value = pr.id;
        option.textContent = pr.purchaseRequestCode;
        prSelect.appendChild(option);
      });
    });

  /* =========================
     2️⃣ 거래처 목록 조회
  ========================== */
  fetch("/api/sales/company/list/purchase_company")
    .then((res) => res.json())
    .then((resData) => {
      if (!resData.success) return;

      companyList = resData.data;

      companyList.forEach((company) => {
        const option = document.createElement("option");
        option.value = company.companyId;
        option.textContent = company.name;
        companySelect.appendChild(option);
      });
    });

  /* =========================
     3️⃣ 거래처 선택 시 자동 세팅
  ========================== */
  companySelect.addEventListener("change", function () {
    const selectedId = Number(this.value);

    const selectedCompany = companyList.find(
      (c) => c.companyId === selectedId
    );

    if (!selectedCompany) {
      document.getElementById("companyManager").value = "";
      document.getElementById("companyPhone").value = "";
      return;
    }

    document.getElementById("companyManager").value =
      selectedCompany.manager ?? "";
    document.getElementById("companyPhone").value =
      selectedCompany.phone ?? "";
  });

  /* =========================
     4️⃣ 구매요청 상세 조회
  ========================== */
  prSelect.addEventListener("change", function () {
    const selectedId = this.value;
    if (!selectedId) return;

    fetch(`/api/sales/purchase_request_detail?requestId=${selectedId}`)
      .then((res) => res.json())
      .then((detail) => {
        document.getElementById("prCode").value =
          detail.purchaseRequestCode ?? "";
        document.getElementById("memberName").value =
          detail.memberName ?? "";
        document.getElementById("requestDate").value =
          detail.requestDate ?? "";
        document.getElementById("dueDate").value =
          detail.dueDate ?? "";
        document.getElementById("deliveryLocate").value =
          detail.deliveryLocate ?? "";
        document.getElementById("contractType").value =
          detail.contractType ?? "";
        document.getElementById("produceType").value =
          detail.produceType ?? "";

        // 라인 초기화
        tbody.innerHTML = "";

        if (detail.lines && detail.lines.length > 0) {
          detail.lines.forEach((line) => {
            const row = document.createElement("tr");

            row.innerHTML = `
              <td>${line.id}</td>
              <td data-item-id="${line.itemId}">${line.itemName}</td>
              <td>${line.quantity}</td>
              <td>${line.uom}</td>
              <td>${line.price}</td>
            `;

            tbody.appendChild(row);
          });
        }
      });
  });
});

/* =========================
   5️⃣ 발주 등록
========================== */
document
  .getElementById("saveProcurementBtn")
  .addEventListener("click", function () {
    const rows = document.querySelectorAll("#lineTable tbody tr");

    if (rows.length === 0) {
      alert("라인 정보가 없습니다.");
      return;
    }

    const companyId = document.getElementById("companySelect").value;

    if (!companyId) {
      alert("거래처를 선택하세요.");
      return;
    }

    let lines = [];
    let totalAmount = 0;

    rows.forEach((row) => {
      const id = row.children[0].textContent;
      const itemId = row.children[1].dataset.itemId;
      const quantity = parseInt(row.children[2].textContent);
      const totalPrice = parseInt(row.children[4].textContent);

      totalAmount += totalPrice;

      lines.push({
        id: id ? Number(id) : null,
        item: Number(itemId),
        quantity: quantity,
        totalPrice: totalPrice,
      });
    });

    const data = {
      purchaseOrderCode: document.getElementById("prCode").value,
      companyId: Number(companyId),
      status: "READY",
      totalAmount: totalAmount,
      lines: lines,
    };

    fetch("/api/sales/procurement/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(data),
    })
      .then((response) => {
        if (!response.ok) {
          throw new Error("등록 실패");
        }
        alert("발주 등록 완료");
        location.reload();
      })
      .catch((error) => {
        console.error(error);
        alert("등록 중 오류 발생");
      });
  });