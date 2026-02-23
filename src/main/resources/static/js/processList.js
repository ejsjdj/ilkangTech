/**
 * 공정 관리 그리드 모듈
 */
const GridManager = {
    mainGrid: null,
    stepGrid: null,

    init() {
        try {
            this.initMainGrid();
            this.initStepGrid();
            this.bindEvents();
            console.log("그리드 초기화 완료");
        } catch (e) {
            console.error("그리드 초기화 중 치명적 에러:", e);
        }
    },

    initMainGrid() {
        const el = document.getElementById('mainGrid');
        if (!el) {
            console.error("#mainGrid 요소를 찾을 수 없습니다.");
            return;
        }

        this.mainGrid = new tui.Grid({
            el: el,
            // ... 나머지 설정 동일
            data: {
                api: { readData: { url: '/api/process_mst', method: 'GET' } },
                serializer(params) {
                    const searchParams = new URLSearchParams();
                    searchParams.append('page', params.page - 1);
                    searchParams.append('size', params.perPage);

                    // Optional Chaining(?.)을 사용하여 요소가 없어도 에러 방지
                    searchParams.append('routeName', document.getElementById('searchRouteName')?.value || '');
                    searchParams.append('itemId', document.getElementById('searchRouteType')?.value || '');

                    return searchParams.toString();
                }
            },
            columns: [
                { header: '라우트ID', name: 'routeId', align: 'center' },
                { header: '제품코드', name: 'itemId', align: 'center' },
                { header: '라우트명', name: 'routeName' },
                {
                    header: '상세보기',
                    name: 'detail',
                    formatter: () => '<button class="btn btn-sm btn-info text-white">상세</button>'
                }
            ]
        });
    },

    initStepGrid() {
        const el = document.getElementById('stepGrid');
        if (!el) return;
        this.stepGrid = new tui.Grid({ el: el, columns: [ /* ... */ ] });
    },

    bindEvents() {
        if (!this.mainGrid) return;
        this.mainGrid.on('click', (ev) => {
            if (ev.columnName === 'detail') {
                this.openModal(this.mainGrid.getRow(ev.rowKey));
            }
        });
    },

    // 핵심: readData 호출 전 null 체크 로직 추가
    search() {
        if (this.mainGrid) {
            this.mainGrid.readData(1);
        } else {
            console.error("메인 그리드가 초기화되지 않아 검색을 수행할 수 없습니다.");
            // 사용자에게 알림
            alert("그리드를 로딩 중입니다. 잠시만 기다려주세요.");
        }
    }
};

// DOM 로드 완료 후 실행
document.addEventListener('DOMContentLoaded', () => GridManager.init());

// 전역 함수
function loadMainGrid() { 
    GridManager.search(); 
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

function closeModal() { document.getElementById('detailModal').style.display = 'none'; }

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