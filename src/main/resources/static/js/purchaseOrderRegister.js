document.addEventListener("DOMContentLoaded", function () {
  const select = document.getElementById("purchaseRequestSelect");
  const tbody = document.querySelector("#lineTable tbody");
  const dummyCompanies = [
    { id: 1, name: "서울스틸" },
    { id: 2, name: "주식회사 대전테크" },
    { id: 3, name: "경기제강" },
    { id: 4, name: "인천철강" },
    { id: 5, name: "(주)대구물산" },
    { id: 6, name: "부산메탈" },
    { id: 7, name: "(주)광주산업" },
  ];

  // 1️⃣ 구매요청 목록 조회
  fetch("/purchase_request_list")
    .then((response) => response.json())
    .then((data) => {
      data.forEach((pr) => {
        const option = document.createElement("option");
        option.value = pr.id;
        option.textContent = pr.purchaseRequestCode;
        select.appendChild(option);
      });
    });

  // 2️⃣ 드롭다운 선택 시 상세조회
  select.addEventListener("change", function () {
    const selectedId = this.value;
    if (!selectedId) return;

    fetch(`/purchase_request_detail?requestId=${selectedId}`)
      .then((response) => response.json())
      .then((detail) => {
        // 🔹 input 값 세팅
        document.getElementById("prCode").value =
          detail.purchaseRequestCode ?? ""; // 구매요청 코드
        document.getElementById("memberName").value = detail.memberName ?? ""; // 구매요청자
        document.getElementById("requestDate").value = detail.requestDate ?? ""; // 요청일
        document.getElementById("dueDate").value = detail.dueDate ?? ""; // 납기일
        document.getElementById("deliveryLocate").value =
          detail.deliveryLocate ?? ""; // 납품장소
        document.getElementById("contractType").value =
          detail.contractType ?? ""; // 계약상태
        document.getElementById("produceType").value = detail.produceType ?? ""; // 요청상태

        // 테이블 초기화
        tbody.innerHTML = "";

        // 🔹 라인 목록 출력
        if (detail.lines && detail.lines.length > 0) {
          detail.lines.forEach((line) => {
            const row = document.createElement("tr");

            row.innerHTML = `
                <td>${line.id}</td>
                <td data-item-id="${line.itemId}">${line.itemName}</td>
                <td>${line.quantity}</td>
                <td>${line.uom}</td>
                <td>${line.price}</td>
                <td>
                    <select class="purchaseCompanySelect">
                        <option value="">-- 거래처 선택 --</option>
                    </select>
                </td>
            `;

            const selectBox = row.querySelector(".purchaseCompanySelect");

            dummyCompanies.forEach((company) => {
              const option = document.createElement("option");
              option.value = company.id;
              option.textContent = company.name;
              selectBox.appendChild(option);
            });

            tbody.appendChild(row);
          });
        }
      });
  });
});

document
  .getElementById("saveProcurementBtn")
  .addEventListener("click", function () {
    const rows = document.querySelectorAll("#lineTable tbody tr");

    if (rows.length === 0) {
      alert("라인 정보가 없습니다.");
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
      company: document.getElementById("companyName")?.value || "",
      //   companyManager: document.getElementById("companyManager")?.value || "",
      //   phone: document.getElementById("phone")?.value || "",
      status: "READY",
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
