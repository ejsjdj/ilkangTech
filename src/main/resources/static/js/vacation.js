$(document).ready(function () {
    setCurrentMonth();      // 최초 1회만
    loadCurrentTabData();

    $('#dateFilter').on('change', function () {
        loadCurrentTabData();
    });
});


function setCurrentMonth() {
    const now = new Date();
    const month = now.toISOString().slice(0, 7); // yyyy-MM
    $('#dateFilter').val(month);
}

function loadCurrentTabData() {
    if ($('#tabMyVac').hasClass('active')) {
        loadVacationList();
    } else {
        loadVacationAllList();
    }
}
/**
 * 탭 전환 로직 (수정됨)
 */
function switchTab(tabType) {
    $('.tab-item').removeClass('active');

    if (tabType === 'MY') {
        $('#tabMyVac').addClass('active');
        $('#vacationContainer').show();
        loadVacationList();
    } else {
        $('#tabAllVac').addClass('active');
        $('#vacationContainer').hide();
        loadVacationAllList();
    }
}

// 부서별 휴가 현황 조회
function loadVacationAllList() {
    let monthVal = $('#dateFilter').val();
    if (!monthVal) return;

    $.ajax({
        url: '/attendance/vacation/all',
        type: 'GET',
        data: {
            workDate: monthVal.length === 7 ? monthVal + "-01" : monthVal
        },
        success: function (response) {
            console.log('ALL VACATION', response);
            renderVacationAllList(response);
        },
        error: function () {
            $('#vacationListContainer').html(`
                <div class="text-center text-danger py-4">
                    휴가 목록을 가져올 수 없습니다.
                </div>
            `);
        }
    });
}


// 개인 휴가 현황 로드
function loadVacationList(){
    let monthVal = $('#dateFilter').val();
    if (!monthVal) return;

    $. ajax({
        url: '/attendance/vacation/list',
        type: 'GET',
        data: {
            workDate: monthVal.length === 7 ? monthVal + "-01" : monthVal
        },
        success: function (response){
            console.log(response);
            renderVacation(response);
            renderVacationList(response);
        },
        error: function (){
            const container = document.getElementById('vacationContainer');
            container.innerHTML =
                '<div class="empty-state">' +
                '<i class="bi bi-exclamation-circle"></i>' +
                '<p>휴가 목록을 가져올 수 없습니다.</p>' +
                '</div>';
        }
    });
}

// 개인 휴가 현황 표시
function renderVacation(response){
    console.log(response);
    const container = $('#vacationContainer');

    if (response && response.length > 0) {
        const item = response[0];

        const html = `
            <div class="vacation-card-grid">
                <div class="vacation-card">
                    <p class="card-label">총 연차</p>
                    <p class="card-value">${item.totalLeave ?? '-'}</p>
                </div>

                <div class="vacation-card">
                    <p class="card-label">사용 연차</p>
                    <p class="card-value">${item.usedLeave ?? '-'}</p>
                </div>

                <div class="vacation-card highlight">
                    <p class="card-label">잔여 연차</p>
                    <p class="card-value">${item.remainLeave ?? '-'}</p>
                </div>
            </div>
        `;

        container.html(html);
    } else {
        container.html(`
            <div class="text-center text-muted py-4">
                데이터가 없습니다.
            </div>
        `);
    }
}

function renderVacationList(response){
 const container = $('#vacationListContainer');

    // 1. 테이블 헤더 시작
    let html = `
        <div class="table-scroll">
            <table class="table emp-table">
                <thead>
                    <tr>
                        <th>시작일</th>
                        <th>종료일</th>
                    </tr>
                </thead>
                <tbody>
    `;

    if (response && response.length > 0) {
        response.forEach(item => {
            html += `
                <tr>
                    <td>${item.startDate || '-'}</td>
                    <td>${item.endDate || '-'}</td>
                </tr>
            `;
        });
    } else {
        html += `<tr><td colspan="8" class="text-center">데이터가 없습니다.</td></tr>`;
    }

    // 3. 테이블 닫기
    html += `</tbody></table></div>`;

    // 4. 화면에 렌더링
    container.html(html);
}


function renderVacationAllList(response){
 const container = $('#vacationListContainer');

    // 1. 테이블 헤더 시작
    let html = `
        <div class="table-scroll">
            <table class="table emp-table">
                <thead>
                    <tr>
                        <th>이름</th>
                        <th>시작일</th>
                        <th>종료일</th>
                        <th>총 일수</th>
                        <th>상태</th>
                    </tr>
                </thead>
                <tbody>
    `;

    if (response && response.length > 0) {
        response.forEach(item => {
            html += `
                <tr>
                    <td>${item.name || '-'}</td>
                    <td>${item.startDate || '-'}</td>
                    <td>${item.endDate || '-'}</td>
                    <td>${item.totalDate || '-'}</td>
                    <td>${item.status || '-'}</td>
                </tr>
            `;
        });
    } else {
        html += `<tr><td colspan="8" class="text-center">데이터가 없습니다.</td></tr>`;
    }

    // 3. 테이블 닫기
    html += `</tbody></table></div>`;

    // 4. 화면에 렌더링
    container.html(html);
}