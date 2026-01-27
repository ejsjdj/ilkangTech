function getAppointmentList() {

    $.ajax({
        url: '/hr/appointment',
        type: 'GET',
        success: function (response) {
            console.log(response)
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

$(document).ready(function() {
    getAppointmentList();
});