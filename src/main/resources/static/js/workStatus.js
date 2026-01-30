$(document).ready(function() {
    // 1. 드롭다운 선택 상자 가져오기
    const $deptSelect = $('#deptFilterContainer select');

    // 2. 오늘 날짜 구하기
    const today = new Date().toISOString().split('T')[0];

    // 3. 데이터를 가져오는 공통 함수
    function fetchData() {
        const deptCode = $deptSelect.val();
        const date = today;

        loadAttendanceData(deptCode, date);

    }

    // 4. 초기 실행
    fetchData();

    // 5. 부서 변경
    $deptSelect.on('change', fetchData);

});

// 부서별 근무 현황 조회
function loadAttendanceData(deptCode, date) {
    $.ajax({
        url: '/attendance/work/list', // 본인의 API 주소로 수정
        type: 'GET',
        data: { deptCode: deptCode, workDate: date},
        success: function(response) {
            renderWorkStatus(response);
        }
    });
}

// 테이블에 표시
function renderWorkStatus(response){
    const container = $('#workStatusContainer');

    let html = `
        <table class="table emp-table">
            <thead>
                <tr>
                    <th class="col-status">이름</th>
                    <th class="col-status">부서</th>
                    <th class="col-status">직급</th>
                    <th class="col-status">상태</th>
                    <th class="col-status">관리</th>
                </tr>
            </thead>
            <tbody>
    `;

    if (response && response.length > 0) {
        response.forEach(item => {
            html += `
                <tr>
                    <td>${item.name ?? '-'}</td>
                    <td>${item.department ?? '-'}</td>
                    <td>${item.position ?? '-'}</td>
                    <td><span class="status-badge status-active">${item.status ?? '-'}</span></td>
                    <td><a class="action-btn action-view">상세</a></td>
                </tr>
            `;
        });
    } else {
        html += `
            <tr>
                <td colspan="3" class="text-center text-muted py-4">
                    데이터가 없습니다.
                </td>
            </tr>
        `;
    }

    html += `</tbody></table>`;
    container.html(html);
}