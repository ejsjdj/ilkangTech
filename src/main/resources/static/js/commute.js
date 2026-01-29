$(document).ready(function() {
    // 처음 페이지 진입 시 '전사원' 탭이 기본이라면
    switchTab('MY');
});

// 드롭다운 값이 바뀔 때마다 전사원 리스트 갱신
$('#deptFilter').on('change', function() {
    loadCommuteAllList();
});

function loadCommuteList() {

    $.ajax({
        url: '/attendance/commute/list',
        type: 'GET',
        success: function (response) {
            renderTable(response)
        },
        error: function () {
            const container = document.getElementById('tableContainer');
            container.innerHTML =
                '<div class="empty-state">' +
                '<i class="bi bi-exclamation-circle"></i>' +
                '<p>출퇴근 기록을 불러오는 중 오류가 발생했습니다.</p>' +
                '</div>';
        }
    });
}

function renderTable(response) {
    const container = $('#tableContainer');

    // 1. 테이블 헤더 시작
    let html = `
        <div class="table-scroll">
            <table class="table emp-table">
                <thead>
                    <tr>
                        <th>출근 시간</th>
                        <th>퇴근 시간</th>
                        <th>외근 시간</th>
                        <th>복귀 시간</th>
                    </tr>
                </thead>
                <tbody>
    `;

    if (response && response.length > 0) {
        response.forEach(item => {
            html += `
                <tr>
                    <td>${item.inTime || '-'}</td>
                    <td>${item.outTime || '-'}</td>
                    <td>${item.goOutTime || '-'}</td>
                    <td>${item.returnTime || '-'}</td>
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

function switchTab(tabType) {
    // 모든 탭에서 active 클래스 제거
    $('.tab-item').removeClass('active');

    if (tabType === 'MY') {
        // 1. 내 출퇴근 탭일 때
        $('#tabMy').addClass('active');
        $('#deptFilterContainer').hide(); // 드롭다운 숨기기
        loadCommuteList();               // 개인 데이터 로드
    } else {
        // 2. 전사원 탭일 때
        $('#tabAll').addClass('active');
        $('#deptFilterContainer').show(); // 드롭다운 보이기
        loadCommuteAllList();            // 전사원 데이터 로드
    }
}

function loadCommuteAllList() {

    $.ajax({
        url: '/attendance/commute/all',
        type: 'GET',
        success: function (response) {
            renderAllTable(response)
        },
        error: function () {
            const container = document.getElementById('tableContainer');
            container.innerHTML =
                '<div class="empty-state">' +
                '<i class="bi bi-exclamation-circle"></i>' +
                '<p>출퇴근 기록을 불러오는 중 오류가 발생했습니다.</p>' +
                '</div>';
        }
    });
}

// 발령 등록
function loadAttendanceData() {
    // 1. 현재 화면에 입력/선택된 값 가져오기

    const deptCode = document.querySelector('select[name="department"]').value;
    const workDate = document.getElementById('dateFilter').value;

    // 3. 컨트롤러 주소에 파라미터를 붙여서 이동 (GET 방식)
    // 주소 형식: /주소?userId=1&newDept=2...
    // 페이지 이동(href)이 아니라, 데이터 요청(ajax)을 합니다.
    $.ajax({
        url: '/attendance/commute/all', // 컨트롤러 @GetMapping 주소
        type: 'GET',
        data: {
            deptCode: deptCode,
            workDate: workDate
        },
        success: function (response) {
            // 서버에서 받은 리스트로 테이블만 새로 그림
            renderAllTable(response);
        },
        error: function () {
            alert("데이터를 가져오는데 실패했습니다.");
        }
    });
}

function renderAllTable(response) {
    const container = $('#tableContainer');

    // 1. 테이블 헤더 시작
    let html = `
        <div class="table-scroll">
            <table class="table emp-table">
                <thead>
                    <tr>
                        <th>이름</th>
                        <th>출근 시간</th>
                        <th>퇴근 시간</th>
                        <th>외근 시간</th>
                        <th>복귀 시간</th>
                    </tr>
                </thead>
                <tbody>
    `;

    if (response && response.length > 0) {
        response.forEach(item => {
            html += `
                <tr>
                    <td>${item.name || '-'}</td>
                    <td>${item.inTime || '-'}</td>
                    <td>${item.outTime || '-'}</td>
                    <td>${item.goOutTime || '-'}</td>
                    <td>${item.returnTime || '-'}</td>
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

