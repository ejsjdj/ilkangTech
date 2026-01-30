let currentData = [];

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

function loadAttendanceData() {

    const deptCode = document.querySelector('select[name="department"]').value;
    const workDate = document.getElementById('dateFilter').value;

    $.ajax({
        url: '/attendance/commute/all',
        type: 'GET',
        data: {
            deptCode: deptCode,
            workDate: workDate
        },
        success: function (response) {
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
                        <th>NO.</th>
                        <th>이름</th>
                        <th>출근 시간</th>
                        <th>퇴근 시간</th>
                        <th>외근 시간</th>
                        <th>복귀 시간</th>
                        <th>관리</th>
                    </tr>
                </thead>
                <tbody>
    `;

    if (response && response.length > 0) {
        response.forEach(item => {
            html += `
                <tr>
                    <td>${item.id || '-'}</td>
                    <td>${item.name || '-'}</td>
                    <td>${item.inTime || '-'}</td>
                    <td>${item.outTime || '-'}</td>
                    <td>${item.goOutTime || '-'}</td>
                    <td>${item.returnTime || '-'}</td>
                    <td>
                       <button class="btn-search" onclick="openEditTimeModal('${item.id}', '${item.inTime || ''}', '${item.outTime || ''}', '${item.goOutTime || ''}', '${item.returnTime || ''}')"> 수정 </button>
                    </td>
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

function openEditTimeModal(id, inTime, outTime, goOutTime, returnTime) {
    const modal = document.getElementById("commuteModal");

    console.log(inTime, outTime, goOutTime, returnTime)

    if (modal) {
            // 모달 내 특정 위치에 사원 ID 등을 미리 세팅할 수 있습니다.
            document.getElementById("modalEmpIdDisplay").innerText = id;
            document.getElementById("attendanceId").value = id;
            document.getElementById("modalInTime").value = inTime;
            document.getElementById("modalOutTime").value = outTime;
            document.getElementById("modalGoOutTime").value = goOutTime;
            document.getElementById("modalReturnTime").value = returnTime;

            modal.style.display = "block";
        }

    console.log("수정할 출퇴근 번호: ", id);
}


// 2. 모달 닫기
function closeModal() {
    const modal = document.getElementById("commuteModal");
    if (modal) {
        modal.style.display = "none";
        document.body.style.overflow = "auto"; // 배경 스크롤 복원
    }
}

// 4. 저장
function saveTime() {
    // 1. 값 읽어오기 (컨트롤러 파라미터명과 key를 일치시킵니다)
    const params = {
        attendanceId: document.getElementById("attendanceId").value,
        inTime: document.getElementById("modalInTime").value,
        outTime: document.getElementById("modalOutTime").value,
        goOutTime: document.getElementById("modalGoOutTime").value,
        returnTime: document.getElementById("modalReturnTime").value
    };

    console.log("전송 데이터:", params);

    // 2. 서버로 전송 (AJAX - @RequestParam 방식에 맞춤)
    $.ajax({
        url: '/attendance/commute/update',
        type: 'POST',
        // contentType을 지정하지 않아야 기본 폼 전송 방식(application/x-www-form-urlencoded)으로 전송됩니다.
        data: params,
        success: function(response) {
            alert("수정이 완료되었습니다.");
            closeModal();
            loadAttendanceData();
        },
        error: function(xhr) {
            console.error(xhr);
            alert("수정 중 오류가 발생했습니다.");
        }
    });
}
