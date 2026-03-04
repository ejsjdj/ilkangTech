document.addEventListener("DOMContentLoaded", function () {
  const Grid = tui.Grid;

  let detailGrid = null;
  let createGrid = null;
  let allProcessList = [];
  let currentRouteCode = null;

  /* ==========================
     1. 메인 그리드
  ========================== */
  const gridEl = document.getElementById("grid");
  if (!gridEl) return;

  const grid = new Grid({
    el: gridEl,
    bodyHeight: 400,
    columns: [
      { header: "라우트코드", name: "routeCode", align: "center" },
      { header: "라우트명", name: "routeName", align: "center" },
      { header: "생성자", name: "constructor", align: "center" },
      { header: "생성일", name: "createdAt", align: "center" },
      {
        header: "상세",
        name: "detail",
        align: "center",
        formatter: () =>
          '<button type="button" class="btn-detail">상세보기</button>',
      },
    ],
  });

  /* ==========================
     2. 메인 데이터 로드
  ========================== */
  function loadMainData() {
    fetch("/api/process_mst?page=0&size=10")
      .then((res) => res.json())
      .then((data) => {
        const list = data.content || data;
        grid.resetData(list);
      })
      .catch((err) => console.error("메인 로드 실패:", err));
  }

  /* ==========================
     3. 상세 클릭
  ========================== */
  grid.on("click", (ev) => {
    if (ev.columnName !== "detail") return;

    const rowData = grid.getRow(ev.rowKey);
    if (!rowData) return;

    currentRouteCode = rowData.routeCode;

    fetch(`/api/process_mst/detail?routeCode=${currentRouteCode}`)
      .then((res) => res.json())
      .then((detailData) => {
        const list = Array.isArray(detailData)
          ? detailData
          : detailData.content || [];
        openDetailModal(list);
      })
      .catch((err) => console.error("상세 로드 실패:", err));
  });

  /* ==========================
     4. 공정 목록 조회
  ========================== */
  function fetchAllProcesses() {
    fetch("/api/process_code_all")
      .then((res) => res.json())
      .then((response) => {
        allProcessList = Array.isArray(response)
          ? response
          : response.data || [];

        console.log("공정정보 리스트: ", response);

        if (!Array.isArray(allProcessList)) return;

        const selectIds = ["processSelect", "newProcessSelect"];

        selectIds.forEach((id) => {
          const select = document.getElementById(id);
          if (!select) return;

          select.innerHTML =
            '<option value="">-- 공정을 선택하세요 --</option>';

          allProcessList.forEach((proc) => {
            const opt = document.createElement("option");
            opt.value = proc.id;
            opt.text = `[${proc.operationCode}] ${proc.name}`;
            select.add(opt);
          });
        });
      })
      .catch((err) => console.error("공정 목록 조회 실패:", err));
  }

  /* ==========================
     5. 상세 모달
  ========================== */
  function openDetailModal(list) {
    const modal = document.getElementById("detailModal");
    if (!modal) return;

    modal.style.display = "block";

    if (!detailGrid) {
      detailGrid = new Grid({
        el: document.getElementById("detailGrid"),
        bodyHeight: 300,
        rowHeaders: ["checkbox", "rowNum"],
        columns: [
          { header: "순번", name: "sequence", editor: "text" },
          { name: "id", hidden: true },
          { name: "operationId", hidden: true },
          { header: "공정코드", name: "operationCode" },
          { header: "공정명", name: "name" },
          { header: "공정설명", name: "description", editor: "text" },
          { header: "비고", name: "note", editor: "text" },
        ],
      });
    }

    const sorted = list.sort(
      (a, b) => (Number(a.sequence) || 0) - (Number(b.sequence) || 0),
    );

    detailGrid.resetData(sorted);
    detailGrid.refreshLayout();
  }

  /* ==========================
     6. 상세 공정 추가
  ========================== */
  const addProcessBtn = document.getElementById("addProcessBtn");
  if (addProcessBtn) {
    addProcessBtn.addEventListener("click", () => {
      if (!detailGrid) return;

      const select = document.getElementById("processSelect");
      if (!select) return;

      const selectedId = select.value;
      if (!selectedId) return alert("공정을 선택해주세요.");

      const selectedProc = allProcessList.find(
        (p) => p.id === Number(selectedId),
      );
      if (!selectedProc) return;

      const currentData = detailGrid.getData();

      // 🔥 중복 방지
      if (currentData.some((r) => r.operationId === selectedProc.id)) {
        return alert("이미 추가된 공정입니다.");
      }

      const maxSeq =
        currentData.length > 0
          ? Math.max(...currentData.map((r) => Number(r.sequence) || 0))
          : 0;

      detailGrid.appendRow({
        id: null,
        operationId: selectedProc.id,
        operationCode: selectedProc.operationCode,
        name: selectedProc.name,
        sequence: maxSeq + 1,
        description: "",
        note: "",
      });
    });
  }

  /* ==========================
     7. 신규 라우트
  ========================== */
  const addRouteBtn = document.getElementById("addRouteBtn");
  if (addRouteBtn) {
    addRouteBtn.addEventListener("click", () => {
      const modal = document.getElementById("createRouteModal");
      if (!modal) return;

      modal.style.display = "block";

      document.getElementById("newRouteCode").value = "RT-" + Date.now();

      initCreateGrid();
    });
  }

  function initCreateGrid() {
    if (createGrid) {
      createGrid.resetData([]);
      return;
    }

    createGrid = new Grid({
      el: document.getElementById("createRouteGrid"),
      bodyHeight: 300,
      rowHeaders: ["rowNum"],
      columns: [
        { header: "순번", name: "sequence", width: 80 },
        { name: "operationId", hidden: true },
        { header: "공정코드", name: "operationCode" },
        { header: "공정명", name: "name" },
      ],
    });
  }

  /* 신규 공정 추가 */
  const addNewProcessBtn = document.getElementById("addNewProcessBtn");
  if (addNewProcessBtn) {
    addNewProcessBtn.addEventListener("click", () => {
      if (!createGrid) return;

      const select = document.getElementById("newProcessSelect");
      if (!select) return;

      const selectedId = select.value;
      if (!selectedId) return alert("공정을 선택하세요.");

      const selectedProc = allProcessList.find(
        (p) => p.id === Number(selectedId),
      );
      if (!selectedProc) return;

      const currentData = createGrid.getData();

      // 🔥 중복 방지
      if (currentData.some((r) => r.operationId === selectedProc.id)) {
        return alert("이미 추가된 공정입니다.");
      }

      const maxSeq =
        currentData.length > 0
          ? Math.max(...currentData.map((r) => Number(r.sequence)))
          : 0;

      createGrid.appendRow({
        sequence: maxSeq + 1,
        operationId: selectedProc.id,
        operationCode: selectedProc.operationCode,
        name: selectedProc.name,
      });
    });
  }

  /* 신규 저장 */
  const saveNewRouteBtn = document.getElementById("saveNewRouteBtn");
  if (saveNewRouteBtn) {
    saveNewRouteBtn.addEventListener("click", () => {
      if (!createGrid) return;

      createGrid.finishEditing();

      const itemId = document.getElementById("newItemSelect").value;
      const routeCode = document.getElementById("newRouteCode").value;
      const routeName = document.getElementById("newRouteName").value;
      const description = document.getElementById("newRouteDescription").value;
      const note = document.getElementById("newRouteNote").value;

      if (!itemId) return alert("적용할 품목을 선택해주세요.");
      if (!routeName) return alert("라우트명을 입력하세요.");

      const gridData = createGrid.getData();
      if (gridData.length === 0)
        return alert("최소 1개 이상의 공정을 추가하세요.");

      const payload = gridData.map((row) => ({
        routeCode,
        routeName,
        description,
        note,
        operationId: row.operationId,
        sequence: row.sequence,
        itemId: Number(itemId),
      }));

      fetch("/api/process_mst/create", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload),
      }).then((res) => {
        if (res.ok) {
          alert("신규 라우트가 생성되었습니다.");
          document.getElementById("createRouteModal").style.display = "none";
          loadMainData();
        } else {
          alert("저장 실패");
        }
      });
    });
  }

  /* 상세 저장 */
  const saveBtn = document.getElementById("saveBtn");
  if (saveBtn) {
    saveBtn.addEventListener("click", () => {
      if (!detailGrid || !currentRouteCode)
        return alert("라우트 정보가 없습니다.");

      detailGrid.finishEditing();

      const updatedData = detailGrid.getData().map((row) => ({
        id: row.id || null,
        operationId: row.operationId,
        sequence: Number(row.sequence),
        description: row.description,
        note: row.note,
      }));

      fetch(`/api/process_mst/update?routeCode=${currentRouteCode}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(updatedData),
      }).then((res) => {
        if (res.ok) {
          alert("변경사항이 저장되었습니다.");
          document.getElementById("detailModal").style.display = "none";
          loadMainData();
        } else {
          alert("저장 실패");
        }
      });
    });
  }

  loadMainData();
  fetchAllProcesses();
});
