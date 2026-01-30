$(document).ready(function() {
    loadVacationList();
});


function loadVacationList(){
    $. ajax({
        url: '/attendance/vacation/list',
        type: 'GET',
        success: function (response){
            console.log(response);
            renderVacation(response);
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


function renderVacation(response){
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
