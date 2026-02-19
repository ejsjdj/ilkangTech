let mainGrid, stepGrid;

document.addEventListener('DOMContentLoaded', function() {
    // 1. 메인 그리드 초기화 (DataSource 연동)
    mainGrid = new tui.Grid({
        el: document.getElementById('mainGrid'),
        bodyHeight: 400,
        rowHeaders: ['checkbox', 'rowNum'],
        pageOptions: { 
            useClient: false, 
            perPage: 10 
        },
        data: {
            api: {
                readData: {
                    url: '/api/process_mst',
                    method: 'GET'
                }
            },
            serializer(params) {
                const searchParams = new URLSearchParams();
                searchParams.append('page', params.page - 1); // Spring 0-based index
                searchParams.append('size', params.perPage);
                
                // 검색 조건 (HTML ID 기준)
                const routeType = document.getElementById('searchRouteType')?.value;
                const routeName = document.getElementById('searchRouteName')?.value;
                
                if (routeType) searchParams.append('routeType', routeType);
                if (routeName) searchParams.append('routeName', routeName);
                
                return searchParams.toString();
            },
            contentType: 'application/json'
        },
        columns: [
            { header: '라우트ID', name: 'routeId', align: 'center', sortable: true },
            { header: '제품코드', name: 'itemId', align: 'center' },
            { header: '라우트명', name: 'routeName', sortable: true },
            { header: '비고', name: 'description' },
            { header: '생성자', name: 'creator', align: 'center' },
            { header: '생성일시', name: 'createdAt', align: 'center' },
            {
                header: '상세보기',
                name: 'detail',
                align: 'center',
                formatter: () => '<button class="btn btn-sm btn-info text-white btn-detail">상세</button>'
            }
        ]
    });

    // 2. 단계(상세) 그리드 초기화
    stepGrid = new tui.Grid({
        el: document.getElementById('stepGrid'),
        bodyHeight: 250,
        rowHeaders: ['checkbox'],
        columns: [
            { header: '순번', name: 'sequence', editor: 'text', align: 'center' },
            { 
                header: '공정 ID', 
                name: 'operationId',
                formatter: 'listItemText',
                editor: {
                    type: 'select',
                    options: {
                        listItems: [
                            { text: '절단(OP-01)', value: 'OP-01' },
                            { text: '용접(OP-02)', value: 'OP-02' }
                        ]
                    }
                }
            },
            { header: '비고', name: 'description', editor: 'text' },
            { header: '생성자', name: 'creator', align: 'center' },
            { header: '생성일시', name: 'createdAt', align: 'center' }
        ]
    });

    // 상세 버튼 클릭 이벤트
    mainGrid.on('click', (ev) => {
        if (ev.columnName === 'detail') {
            const row = mainGrid.getRow(ev.rowKey);
            openModal(row);
        }
    });
});

// 검색 버튼 클릭 시 호출
function loadMainGrid() {
    mainGrid.readData(1); // 1페이지부터 다시 조회
}

// 모달 열기 및 상세 데이터 fetch
async function openModal(row) {
    document.getElementById('detailModal').style.display = 'block';
    document.getElementById('detailRouteId').value = row.routeId;
    document.getElementById('detailItemCode').value = row.itemId;
    document.getElementById('detailRouteName').value = row.routeName;

    try {
        // 상세 데이터 API 호출
        const response = await fetch(`/api/process_mst/detail?routeId=${row.routeId}`);
        const data = await response.json();
        
        stepGrid.resetData(data);
        
        // 레이아웃 새로고침
        setTimeout(() => stepGrid.refreshLayout(), 50);
    } catch (error) {
        console.error("상세 데이터 로드 실패:", error);
    }
}

function closeModal() {
    document.getElementById('detailModal').style.display = 'none';
}

function saveData() {
    const rowData = stepGrid.getData();
    const routeId = document.getElementById('detailRouteId').value;

    // DTO 구조에 맞게 데이터 가공 필요 시 여기서 처리
    const payload = {
        routeId: routeId,
        details: rowData
    };

    fetch('/api/process_mst/insert', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(payload)
    })
    .then(res => {
        if(res.ok) {
            alert("저장 성공!");
            closeModal();
            loadMainGrid();
        }
    })
    .catch(err => alert("저장 중 오류 발생"));
}