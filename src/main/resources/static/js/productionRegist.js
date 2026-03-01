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

  // Grid 생성
  const analysisGrid = new Grid({
    el: document.getElementById("analysisGrid"),
    bodyHeight: 300,
    scrollX: false,
    scrollY: false,
    columns: [
      { header: "제품명", name: "productName" },
      { header: "주문수량", name: "orderQty", align: "center" },
      { header: "현재재고", name: "stockQty", align: "center" },
      { header: "재고가능 여부", name: "hasStock", align: "center" },
      { header: "생산가능 여부", name: "producible", align: "center" },
      { header: "생산필요 여부", name: "needProduction", align: "center" },
    ],
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

        // 생산가능 여부는 지금은 단순하게 true 처리 (UI용)
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

function goRegister() {
  alert("계획 등록 처리 (아직 백엔드 미연결)");
}
