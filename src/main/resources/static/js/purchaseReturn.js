document.addEventListener("DOMContentLoaded", function () {
  const Grid = tui.Grid;

  const grid = new Grid({
    el: document.getElementById("returnGrid"),
    bodyHeight: 400,
    scrollX: false,
    scrollY: false,

    pageOptions: {
      useClient: false, // 🔥 서버 페이징
      perPage: 10,
    },

    columns: [
      { header: "ID", name: "id", align: "center" },
      { header: "발주코드", name: "orderCode" },
      { header: "품목명", name: "itemName" },
      { header: "반품수량", name: "returnQty", align: "right" },
      { header: "반품자", name: "memberName" },
      { header: "상태", name: "status" },
      { header: "반품일자", name: "returnDate" },
    ],
  });

  // 🔥 데이터 로딩 함수
  function loadData(page = 1) {
    fetch(`/api/sales/procurement/return?page=${page - 1}&size=10`)
      .then((res) => res.json())
      .then((data) => {
        console.log("반품 리스트:", data);

        grid.resetData(data.content);

        grid.setPaginationTotalCount(data.totalElements);
      });
  }

  // 첫 로딩
  loadData(1);

  // 페이지 변경 이벤트
  grid.on("afterPageMove", (ev) => {
    loadData(ev.page);
  });
});
