$(document).ready(function () {
    // 초기 로드
    loadAppointmentList();
});

// 검색 실행
function handleSearch() {
    currentState.searchField = $('#searchInput').val();
    currentState.pageNum = 0;
    loadAppointmentList();
}

// 초기화
function resetSearch() {
    $('#searchInput').val('');
    currentState.searchField = '';
    currentState.pageNum = 0;
    loadAppointmentList();
}


function loadAppointmentList() {

    $.ajax({
        url: '/hr/appointment/list',
        type: 'GET',
        success: function (response) {
            renderTable(response)
        },
        error: function () {
            const container = document.getElementById('tableContainer');
            container.innerHTML =
                '<div class="empty-state">' +
                '<i class="bi bi-exclamation-circle"></i>' +
                '<p>발령 목록을 불러오는 중 오류가 발생했습니다.</p>' +
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
                        <th>발령대상자</th>
                        <th>승인자</th>
                        <th>이전 부서</th>
                        <th>현재 부서</th>
                        <th>이전 직급</th>
                        <th>현재 직급</th>
                        <th>근무 상태</th>
                        <th>승인일</th>
                    </tr>
                </thead>
                <tbody>
    `;

    if (response && response.length > 0) {
        response.forEach(item => {
            html += `
                <tr>
                    <td>${item.memberName || item.memberId || '-'}</td>
                    <td>${item.approverName || item.approverId || '-'}</td>
                    <td>${item.preDeptName || item.preDept || '-'}</td>
                    <td>${item.currentDeptName || item.currentDept || '-'}</td>
                    <td>${item.preRankName || item.preRank || '-'}</td>
                    <td>${item.currentRankName || item.currentRank || '-'}</td>
                    <td>${item.workStatus || '-'}</td>
                    <td>${item.appointmentDate || '-'}</td>
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


