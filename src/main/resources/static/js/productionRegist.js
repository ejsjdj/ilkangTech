document.addEventListener("DOMContentLoaded", function () {
  const Grid = tui.Grid;

  // 더미 수주 데이터
  const dummyOrderData = {
    1: [
      { productName: "제품A", orderQty: 100, stockQty: 120 },
      { productName: "제품B", orderQty: 50, stockQty: 20 },
    ],
    2: [{ productName: "제품C", orderQty: 30, stockQty: 10 }],
    3: [{ productName: "제품D", orderQty: 200, stockQty: 300 }],
  };

  // O/X 컬러 포맷터 함수
  const statusFormatter = ({ value }) => {
    const color = value === "O" ? "#166534" : "#ef4444"; // 초록 / 빨강
    const bgColor = value === "O" ? "#dcfce7" : "#fee2e2";
    return `<span style="color: ${color}; background: ${bgColor}; padding: 2px 8px; border-radius: 4px; font-weight: bold;">${value}</span>`;
  };

  // Grid 생성
  const analysisGrid = new Grid({
    el: document.getElementById("analysisGrid"),
    rowHeaders: ["checkbox"], // 1️⃣ 체크박스 추가
    bodyHeight: 300,
    scrollX: false,
    scrollY: false,
    columns: [
      { header: "제품명", name: "productName" },
      { header: "주문수량", name: "orderQty", align: "center" },
      { header: "현재재고", name: "stockQty", align: "center" },
      {
        header: "재고가능 여부",
        name: "hasStock",
        align: "center",
        formatter: statusFormatter,
      }, // 2️⃣ 컬러 적용
      {
        header: "생산가능 여부",
        name: "producible",
        align: "center",
        formatter: statusFormatter,
      },
      {
        header: "생산필요 여부",
        name: "needProduction",
        align: "center",
        formatter: statusFormatter,
      },
    ],
  });

  // 3️⃣ 체크박스 클릭 이벤트 (재고 X일 때 모달 띄우기)
  analysisGrid.on("click", (ev) => {
    // 클릭한 대상이 데이터 셀이 아니면 무시
    if (ev.targetType !== "cell") return;

    // 클릭한 컬럼이 'hasStock' 컬럼인지 확인
    if (ev.columnName === "hasStock") {
      const rowData = analysisGrid.getRow(ev.rowKey);

      // 상태가 'X'인 경우에만 모달 표시
      if (rowData.hasStock === "X") {
        showShortageModal(rowData);
      }
    }
  });

  // 수주 선택 시 동작
  document
    .getElementById("orderSelect")
    .addEventListener("change", function () {
      const orderId = this.value;
      if (!orderId) {
        analysisGrid.resetData([]);
        return;
      }

      const rawData = dummyOrderData[orderId];
      const processedData = rawData.map((item) => {
        const hasStock = item.stockQty >= item.orderQty;
        const needProduction = item.stockQty < item.orderQty;
        const producible = true;

        return {
          productName: item.productName,
          orderQty: item.orderQty,
          stockQty: item.stockQty,
          hasStock: hasStock ? "O" : "X",
          producible: producible ? "O" : "X",
          needProduction: needProduction ? "O" : "X",
        };
      });

      analysisGrid.resetData(processedData);
    });
});

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
