document.addEventListener("DOMContentLoaded", function () {
    const Grid = tui.Grid;

    const modal = document.getElementById("processModal");
    const openBtn = document.getElementById("openModalBtn");
    const closeBtn = document.getElementById("closeModalBtn");
    const saveBtn = document.getElementById("saveProcessBtn");
    const backdrop = document.querySelector('.modal-backdrop');

    const operationCodeInput = document.getElementById("operationCode");
    const nameInput = document.getElementById("processName");
    const descriptionInput = document.getElementById("description");

    const searchKeywordInput = document.getElementById("searchKeyword");
    const searchBtn = document.getElementById("searchBtn");
    const resetBtn = document.getElementById("resetBtn");

    const saveStatusBtn = document.getElementById("saveStatusBtn");

    const state = {
        page: 1,
        perPage: 10,
        keyword: ""
    };

    class StatusSelectRenderer {
        constructor(props) {
            const el = document.createElement("select");

            el.innerHTML = `
            <option value="ACTIVE">활성화</option>
            <option value="INACTIVE">비활성화</option>
        `;

            el.value = props.value;

            el.addEventListener("mousedown", (e) => {
                e.stopPropagation();
            });

            el.addEventListener("click", (e) => {
                e.stopPropagation();
            });

            el.addEventListener("change", () => {
                props.grid.setValue(props.rowKey, props.columnInfo.name, el.value);
            });

            this.el = el;
        }

        getElement() {
            return this.el;
        }

        render(props) {
            this.el.value = props.value;
        }
    }

    const grid = new Grid({
        el: document.getElementById("grid"),
        scrollX: false,
        scrollY: false,
        rowHeaders: ["rowNum"],
        pageOptions: {
            useClient: false,
            perPage: 10
        },
        columns: [
            { header: "ID", name: "id", align: "center", hidden: "true" },
            { header: "공정코드", name: "operationCode", align: "center", editor: "text" },
            { header: "공정명", name: "name", align: "center", editor: "text" },
            { header: "설명", name: "description", align: "center", editor: "text" },
            { header: "등록자", name: "memberName", align: "center" },
            { header: "등록일", name: "createdAt", align: "center" },
            {
                header: "상태",
                name: "status",
                align: "center",
                renderer: {
                    type: StatusSelectRenderer
                }
            }
        ]
    });

    saveStatusBtn.addEventListener("click", async function () {

        const modifiedRows = grid.getModifiedRows();
        const updatedRows = modifiedRows.updatedRows;

        if (!updatedRows || updatedRows.length === 0) {
            alert("변경된 내용이 없습니다.");
            return;
        }

        try {

            const requestBody = updatedRows.map(row => ({
                processId: row.id,
                operationCode: row.operationCode,
                name: row.name,
                description: row.description,
                status: row.status
            }));

            const response = await fetch("/api/update_process_code/status", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(requestBody)
            });

            if (!response.ok) {
                throw new Error("저장 실패");
            }

            alert("저장 완료");
            loadData();

        } catch (error) {
            console.error(error);
            alert("저장 중 오류 발생");
        }
    });

    // 데이터 로드 함수
    async function loadData() {
        try {
            const response = await fetch(
                `/api/process_code?page=${state.page - 1}&size=${state.perPage}`
                + `&keyword=${encodeURIComponent(state.keyword)}`,
                {
                    method: "GET"
                }
            );

            if (!response.ok) {
                throw new Error("서버 통신 실패");
            }

            const result = await response.json();

            grid.resetData(result.content);
            grid.setPaginationTotalCount(result.totalElements);

        } catch (error) {
            console.error("데이터 조회 실패:", error);
        }
    }

    loadData();

    // 페이지 변경 이벤트
    grid.on("afterPageMove", function (ev) {
        state.page = ev.page;
        state.perPage = ev.perPage;
        loadData();
    });

    // 행 회색처리
    grid.on("onGridUpdated", function () {
        grid.getData().forEach(row => {
            if (row.status === "INACTIVE") {
                grid.addRowClassName(row.rowKey, "inactive-row");
            }
        });
    });


    // 검색 버튼
    searchBtn.addEventListener("click", function () {
        state.keyword = searchKeywordInput.value.trim();
        state.page = 1;   // 검색하면 1페이지로
        loadData();
    });

    // 초기화 버튼
    resetBtn.addEventListener("click", function () {
        searchKeywordInput.value = "";
        state.keyword = "";
        state.page = 1;
        loadData();
    });

    // 엔터키
    searchKeywordInput.addEventListener("keydown", function (e) {
        if (e.key === "Enter") {
            state.keyword = searchKeywordInput.value.trim();
            state.page = 1;
            loadData();
        }
    });

    // 모달 열기
    openBtn.addEventListener("click", function () {
        modal.style.display = "block";
        backdrop.style.display = "block";
        operationCodeInput.value = "OP-";
        operationCodeInput.focus();
    });

    // 모달 닫기
    closeBtn.addEventListener("click", function () {
        modal.style.display = "none";
        backdrop.style.display = "none"; // 배경 사라짐
    });

    // OP- 강제 유지
    operationCodeInput.addEventListener("input", function () {
        if (!operationCodeInput.value.startsWith("OP-")) {
            operationCodeInput.value = "OP-";
        }
    });

    // 저장 버튼
    saveBtn.addEventListener("click", async function () {

        const operationCode = operationCodeInput.value.trim();
        const name = nameInput.value.trim();
        const description = descriptionInput.value.trim();

        if (!operationCode || operationCode === "OP-") {
            alert("공정코드를 입력하세요.");
            return;
        }

        if (!name) {
            alert("공정명을 입력하세요.");
            return;
        }

        const requestBody = {
            operationCode: operationCode,
            name: name,
            description: description
        };

        try {
            const response = await fetch("/api/insert_process_code", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(requestBody)
            });

            if (!response.ok) {
                throw new Error("저장 실패");
            }

            alert("저장 완료");

            modal.style.display = "none";

            operationCodeInput.value = "";
            nameInput.value = "";
            descriptionInput.value = "";

            if (typeof loadData === "function") {
                loadData(1, 10);
            }

        } catch (error) {
            console.error(error);
            alert("저장 중 오류 발생");
        }
    });

});