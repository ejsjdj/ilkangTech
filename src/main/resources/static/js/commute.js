$(document).ready(function() {
    initPage(); // 초기화 실행
});

/**
 * 1. 초기 세팅 및 이벤트 바인딩
 */
function initPage() {
    // 오늘 날짜 세팅
    const today = getKSTDate('date');
    const dateFilter = document.getElementById('dateFilter');
    if (dateFilter) dateFilter.value = today;

    // 부서 선택(deptFilter)이나 날짜(dateFilter)가 바뀔 때마다 실행
    $('#deptFilter, #dateFilter').on('change', function() {
        // 전사원 관리 탭(tabAll)에 active 클래스가 있을 때만 자동 조회
        if ($('#tabAll').hasClass('active')) {
            console.log("필터 변경 감지: 전사원 리스트를 갱신합니다.");
            loadCommuteAllList();
        }

        if ($('#tabMy').hasClass('active')) {
            console.log("필터 변경 감지: 개인 리스트를 갱신합니다.");
            loadCommuteList();
        }
    });

    switchTab('MY'); // 초기 진입 시 내 출퇴근 로드
}


function getKSTDate(format = 'date') {
    const now = new Date();
    const offset = now.getTimezoneOffset() * 60000;
    const kstDate = new Date(now.getTime() - offset);

    if (format === 'month') {
        return kstDate.toISOString().substring(0, 7); // "2026-02"
    }
    return kstDate.toISOString().split('T')[0]; // "2026-02-05"
}

/**
 * 2. 탭 전환 로직
 */
function switchTab(tabType) {
    $('.tab-item').removeClass('active');
    const dateInput = document.getElementById('dateFilter');

    if (tabType === 'MY') {
        $('#tabMy').addClass('active');
        $('#deptFilterContainer').hide();
        dateInput.type = 'month';
        dateInput.value = getKSTDate('month'); // 수정
        loadCommuteList();
    } else {
        $('#tabAll').addClass('active');
        $('#deptFilterContainer').show();
        dateInput.type = 'date';
        dateInput.value = getKSTDate('date'); // 수정
        loadCommuteAllList();
    }
}

/**
 * 개인 데이터 로드 (월 단위)
 */
function loadCommuteList() {
    let monthVal = $('#dateFilter').val(); // "2026-02"
    if (!monthVal) return;

    $.ajax({
        url: '/attendance/commute/list',
        type: 'GET',
        data: {
            workDate: monthVal.length === 7 ? monthVal + "-01" : monthVal
        },
        success: renderListTable,
        error: () => showError('#tableContainer', '기록 로드 실패')
    });
}

/**
 * 부서별 데이터 로드 (월 단위)
 */
function loadCommuteAllList() {
    const params = {
        deptCode: $('#deptFilter').val(),
        workDate: $('#dateFilter').val(),
    };

    $.ajax({
        url: '/attendance/commute/all',
        type: 'GET',
        data: params,
        success: renderAllTable,
        error: () => alert("데이터 로드 실패")
    });
}

/**
 * 4. 테이블 렌더링 (개인 내역)
 */
function renderListTable(response) {

    let html = `
        <div class="table-scroll">
            <table class="emp-table">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>근무일</th>
                        <th>출근</th>
                        <th>퇴근</th>
                        <th>외근</th>
                        <th>복귀</th>
                        <th>관리</th>
                    </tr>
                </thead>
                <tbody>
    `;

    if (response?.length > 0) {
        response.forEach(item => {
            html += `
                <tr>
                    <td>${item.attendanceId || '-'}</td>
                    <td>${item.workDate || '-'}</td>
                    <td>${item.inTime || '-'}</td>
                    <td>${item.outTime || '-'}</td>
                    <td>${item.goOutTime || '-'}</td>
                    <td>${item.returnTime || '-'}</td>
                    <td>
                        <button class="btn-search"
                            onclick="openRequestUpdateModal('${item.attendanceId}', '${item.workDate}')">
                            수정
                        </button>
                    </td>
                </tr>`;
        });
    } else {
        html += `
            <tr>
                <td colspan="7" class="text-center">데이터가 없습니다.</td>
            </tr>`;
    }

    html += `
                </tbody>
            </table>
        </div>
    `;

    $('#tableContainer').html(html);
}

/**
 * 6. 수정 요청 모달 및 저장 로직
 */
function openRequestUpdateModal(attendanceId, workDate) {

    const modal = document.getElementById("commuteRequestModal");

    if (!modal) {
        console.error("모달 요소를 찾을 수 없습니다.");
        return;
    }

    $('#modalAttendanceId').val(attendanceId);
    $('#modalTargetDate').text(workDate);
    $('#hiddenTargetDate').val(workDate);  // 서버 전송용
    modal.style.display = "block";
}

function requestUpdateTime() {
    const params = {
        attendanceId: $('#modalAttendanceId').val(),
        context: $('#context').val(),
        contextDetail: $('#contextDetail').val(),
        targetDate: $('#hiddenTargetDate').val(),
    };

    console.log("수정 요청 데이터 : ", params);

    $.ajax({
        url: '/attendance/commute/request',
        type: 'POST',
        data: params,
        success: function() {
            alert("수정 요청이 완료되었습니다.");
            closeModal();
            $('.tab-item#tabMy').hasClass('active') ? loadCommuteList() : loadCommuteAllList();
        },
        error: () => alert("요청 중 오류가 발생했습니다.")
    });
}

/**
 * 5. 테이블 렌더링 (전사원 관리)
 */
function renderAllTable(response) {
    let html = `
        <div class="table-scroll">
            <table class="table emp-table">
                <thead>
                    <tr>
                        <th>NO.</th><th>이름</th><th>출근</th><th>퇴근</th><th>외근</th><th>복귀</th><th>관리</th>
                    </tr>
                </thead>
                <tbody>
    `;

    if (response?.length > 0) {
        response.forEach(item => {
            // 수정 버튼 클릭 시 전달할 인자들을 안전하게 처리
            const args = `'${item.id}', '${item.inTime || ''}', '${item.outTime || ''}', '${item.goOutTime || ''}', '${item.returnTime || ''}'`;
            html += `
                <tr>
                    <td>${item.id}</td>
                    <td>${item.name}</td>
                    <td>${item.inTime || '-'}</td>
                    <td>${item.outTime || '-'}</td>
                    <td>${item.goOutTime || '-'}</td>
                    <td>${item.returnTime || '-'}</td>
                    <td><button class="btn-search" onclick="openEditTimeModal(${args})">수정</button></td>
                </tr>`;

        });
    } else {
        html += `<tr><td colspan="7" class="text-center">데이터가 없습니다.</td></tr>`;
    }
    $('#tableContainer').html(html + `</tbody></table></div>`);
}


/**
 * 7. 수정 모달 및 저장 로직
 */
function openEditTimeModal(id, inTime, outTime, goOutTime, returnTime) {
    const modal = document.getElementById("commuteModal");
    if (!modal) return;

    // 1. 기본 정보 세팅
    $('#modalEmpIdDisplay').text(id);
    $('#attendanceId').val(id);

    // 2. 각 필드별 값 세팅 및 비활성화 로직
    const timeFields = {
        '#modalInTime': inTime,
        '#modalOutTime': outTime,
        '#modalGoOutTime': goOutTime,
        '#modalReturnTime': returnTime
    };

    Object.entries(timeFields).forEach(([selector, value]) => {
        const $el = $(selector);
        $el.val(value); // 값 할당

        // 값이 null, undefined, 또는 빈 문자열일 경우 비활성화
        if (!value || value.trim() === '' || value === 'null') {
            $el.prop('disabled', true);
            $el.css('background-color', '#f5f5f5'); // 비활성화 시 시각적 표시 (선택사항)
        } else {
            $el.prop('disabled', false);
            $el.css('background-color', '#ffffff'); // 활성화 시 배경색 초기화
        }
    });

    modal.style.display = "block";
}

function closeModal() {
    $("#commuteModal").hide();
    $("#commuteRequestModal").hide();
    $("body").css("overflow", "auto");
}

function showError(containerId, message) {
    $(containerId).html(`
        <div class="empty-state">
            <i class="bi bi-exclamation-circle"></i>
            <p>${message}</p>
        </div>
    `);
}

/**
 * 8. 관리자용 출퇴근 시간 수정 실행
 */
function saveTime() {
    // 1. 데이터 수집 (RequestParam 형식이므로 일반 객체로 생성)
    const attendanceId = $('#attendanceId').val();
    const inTime = $('#modalInTime').val();
    const outTime = $('#modalOutTime').val();
    const goOutTime = $('#modalGoOutTime').val();
    const returnTime = $('#modalReturnTime').val();

    if (!attendanceId) {
        alert("출퇴근 기록 식별자(ID)가 없습니다.");
        return;
    }

    if (!confirm("출퇴근 시간을 수정하시겠습니까?")) return;

    // 2. AJAX 전송
    $.ajax({
        url: '/attendance/commute/update',
        type: 'POST', // 컨트롤러의 @PostMapping과 일치시킴
        // contentType을 설정하지 않아야 기본값인 application/x-www-form-urlencoded로 전송됨
        data: {
            attendanceId: attendanceId,
            inTime: inTime,
            outTime: outTime,
            goOutTime: goOutTime,
            returnTime: returnTime
        },
        success: function() {
            alert("수정이 완료되었습니다.");
            closeModal();
            // 현재 보고 있는 리스트 새로고침
            if ($('#tabAll').hasClass('active')) {
                loadCommuteAllList();
            } else {
                loadCommuteList();
            }
        },
        error: function(xhr) {
            console.error("수정 실패:", xhr);
            alert("수정 중 오류가 발생했습니다.");
        }
    });
}
