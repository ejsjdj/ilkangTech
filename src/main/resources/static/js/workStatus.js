$(document).ready(function() {
    const $deptSelect = $('#deptFilterContainer select');

    // 한국 시간 기준으로 yyyy-mm-dd 구하기
    const offset = new Date().getTimezoneOffset() * 60000;
    const today = new Date(Date.now() - offset).toISOString().split('T')[0];

    console.log("전송할 날짜:", today); // 콘솔에서 2026-02-05가 나오는지 꼭 확인하세요!

    function fetchData() {
        const deptCode = $deptSelect.val();
        loadAttendanceData(deptCode, today);
    }

    fetchData();
    $deptSelect.on('change', fetchData);
});

// 부서별 근무 현황 조회
function loadAttendanceData(deptCode, date) {
    $.ajax({
        url: '/attendance/work/list', // 본인의 API 주소로 수정
        type: 'GET',
        data: { deptCode: deptCode, workDate: date},
        success: function(response) {
            console.log(response);
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