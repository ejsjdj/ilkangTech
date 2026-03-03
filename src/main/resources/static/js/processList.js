document.addEventListener("DOMContentLoaded", function () {
    const Grid = tui.Grid;
    let detailGrid = null;
    let allProcessList = [];
    let currentRouteCode = null;

    // 1. 메인 그리드 초기화
    const grid = new Grid({
        el: document.getElementById('grid'),
        bodyHeight: 400,
        columns: [
            { header: '라우트코드', name: 'routeCode', align: 'center' },
            { header: '라우트명', name: 'routeName', align: 'center' },
            {
                header: '상세',
                name: 'detail',
                align: 'center',
                formatter: () => '<button type="button" class="btn-detail">상세보기</button>'
            }
        ]
    });

    // 2. 메인 데이터 로드
    function loadMainData() {
        fetch('/api/process_mst?page=0&size=10')
            .then(res => res.json())
            .then(data => {
                const list = data.content || data;
                grid.resetData(list);
            })
            .catch(err => console.error("메인 로드 실패:", err));
    }

    // 3. 그리드 클릭 (상세보기)
    grid.on('click', (ev) => {
        if (ev.columnName !== 'detail') return;
        const rowData = grid.getRow(ev.rowKey);

        currentRouteCode = rowData.routeCode;

        fetch(`/api/process_mst/detail?routeCode=${currentRouteCode}`)
            .then(res => res.json())
            .then(detailData => {
                const list = Array.isArray(detailData) ? detailData : (detailData.content || []);
                openSimpleModal(list);
            })
            .catch(err => console.error("상세 로드 실패:", err));
    });

    // 4. 공정 코드 전체 조회 (드롭다운용)
    function fetchAllProcesses() {
        fetch('/api/process_code_all')
            .then(res => res.json())
            .then(response => {
                if (response.success === false) {
                    console.error("서버 에러:", response.message);
                    return;
                }

                allProcessList = Array.isArray(response) ? response : (response.data || []);

                if (!Array.isArray(allProcessList)) {
                    console.error("데이터 형식이 배열이 아닙니다:", response);
                    return;
                }

                const select = document.getElementById('processSelect');
                select.innerHTML = '<option value="">-- 공정을 선택하세요 --</option>';

                allProcessList.forEach(proc => {
                    const opt = document.createElement('option');
                    opt.value = proc.id;
                    opt.text = `[${proc.operationId}] ${proc.name}`;
                    select.add(opt);
                });
            })
            .catch(err => console.error("공정 목록 조회 실패:", err));
    }

    // 5. 리스트에 공정 추가 버튼
    document.getElementById('addProcessBtn').addEventListener('click', () => {
        const select = document.getElementById('processSelect');
        const selectedId = select.value;

        if (!selectedId) {
            alert('공정을 선택해주세요.');
            return;
        }

        const selectedProc = allProcessList.find(p => p.id === Number(selectedId));
        const currentData = detailGrid.getData();
        const maxSeq = currentData.length > 0
            ? Math.max(...currentData.map(r => Number(r.sequence) || 0))
            : 0;

        detailGrid.appendRow({
            id: selectedProc.id,
            operationId: selectedProc.operationId,
            name: selectedProc.name,
            sequence: maxSeq + 1,
            description: '',
            note: ''
        });
    });

    // 6. 상세 모달 오픈
    function openSimpleModal(list) {
        const modal = document.getElementById('detailModal');
        modal.style.display = 'block';

        if (!detailGrid) {
            detailGrid = new Grid({
                el: document.getElementById('detailGrid'),
                bodyHeight: 300,
                rowHeaders: ['checkbox', 'rowNum'],
                columns: [
                    { header: '순번', name: 'sequence', width: 70, align: 'center', editor: 'text' },
                    { header: '공정ID', name: 'id', align: 'center' },
                    { header: '공정코드', name: 'operationId', align: 'center' },
                    { header: '공정명', name: 'name' },
                    { header: '공정설명', name: 'description', editor: 'text' },
                    { header: '비고', name: 'note', editor: 'text' }
                ]
            });
        }

        setTimeout(() => {
            detailGrid.refreshLayout();
            const sortedList = list.sort((a, b) => (Number(a.sequence) || 0) - (Number(b.sequence) || 0));
            detailGrid.resetData(sortedList);
        }, 100);
    }

    // 7. 저장 버튼
    document.getElementById('saveBtn').addEventListener('click', () => {
        if (!currentRouteCode) {
            alert("라우트 정보가 없습니다.");
            return;
        }

        detailGrid.finishEditing();

        const updatedData = detailGrid.getData().map(row => ({
            id: row.id || null,
            operationId: row.operationId, // Number() 제거
            sequence: Number(row.sequence),
            description: row.description,
            note: row.note
        }));
        fetch(`/api/process_mst/update?routeCode=${currentRouteCode}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(updatedData)
        })
            .then(res => {
                if (res.ok) {
                    alert('변경사항이 저장되었습니다.');
                    document.getElementById('detailModal').style.display = 'none';
                    loadMainData();
                } else {
                    alert('저장에 실패했습니다.');
                }
            })
            .catch(err => console.error(err));
    });

    loadMainData();
    fetchAllProcesses();
});