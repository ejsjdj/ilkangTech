$(document).ready(function() {
    getAppointmentList();
});



function getAppointmentList() {

    $.ajax({
        url: '/hr/appointment/list',
        type: 'GET',
        success: function (response) {
            console.log(response)
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

function renderTable(response){
    const container = $('#tableContainer');

    // 테이블 헤더 생성
        let html = `
            <div class="table-scroll">
                <table class="table emp-table">
                    <thead>
                        <tr>
                            <th>발령대상자 ${getSortIcon('memberId')}</th>
                            <th>승인자 ${getSortIcon('approvedId')}</th>
                            <th>이전 부서 ${getSortIcon('preDept')}</th>
                            <th>현재 부서 ${getSortIcon('currentDept')}</th>
                            <th>이전 직급 ${getSortIcon('preRank')}</th>
                            <th>현재 직급 ${getSortIcon('currentRank')}</th>
                            <th>근무 상태 ${getSortIcon('workStatus')}</th>
                            <th>승인일 ${getSortIcon('appointmentDate')}</th>
                        </tr>
                    </thead>
                    <tbody>
        `;

        // 데이터 행 생성
        if (!pageData.content || pageData.content.length === 0) {
            html += `<tr><td colspan="7" class="text-center" style="padding: 100px 0;">조회된 발령 내역이 없습니다.</td></tr>`;
        } else {
            pageData.content.forEach(item => {
                html += `
                    <tr>
                        <td>${item.appointmentDate || '-'}</td>
                        <td class="col-empno">${item.employeeNumber || '-'}</td>
                        <td>${item.name || '-'}</td>
                        <td><span class="status-badge">${item.appointmentType || '-'}</span></td>
                        <td class="text-muted" style="font-size: 0.85rem;">${item.prevInfo || '-'}</td>
                        <td class="fw-bold">${item.currInfo || '-'}</td>
                        <td class="text-center">
                            <a href="/hr/appointment/detail/${item.id}" class="action-btn">상세</a>
                        </td>
                    </tr>
                `;
            });
        }

        html += `</tbody></table></div>`;

        // 페이지네이션 추가
        html += renderPagination(pageData);

        container.html(html);




}


