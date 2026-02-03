$(document).ready(function() {
    initPage(); // 초기화 실행
});

/**
 * 1. 초기 세팅 및 이벤트 바인딩
 */
function initPage() {
    // 오늘 날짜 세팅
    const today = new Date().toISOString().split('T')[0];
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

/**
 * 2. 탭 전환 로직
 */
function switchTab(tabType) {
    $('.tab-item').removeClass('active');
    const dateInput = document.getElementById('dateFilter');
    const now = new Date();
    
    if (tabType === 'MY') {
        $('#tabMy').addClass('active');
        $('#deptFilterContainer').hide();

        dateInput.type = 'month';
        dateInput.value = now.toISOString().substring(0, 7); // "2026-02"

        loadCommuteList();
    } else {
        $('#tabAll').addClass('active');
        $('#deptFilterContainer').show();

        dateInput.type = 'date';
        dateInput.value = now.toISOString().split('T')[0]; // "2026-02-01"

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
            <table class="table emp-table">
                <thead>
                    <tr>
                        <th>날짜</th><th>출근</th><th>퇴근</th><th>외근</th><th>복귀</th><th>관리</th>
                    </tr>
                </thead>
                <tbody>
    `;

    if (response?.length > 0) {
        response.forEach(item => {
            const args = `'${item.workDate}'`;
            html += `
                <tr>
                    <td>${item.attendanceId || '-'}</td>
                    <td>${item.workDate || '-'}</td>
                    <td>${item.inTime || '-'}</td>
                    <td>${item.outTime || '-'}</td>
                    <td>${item.goOutTime || '-'}</td>
                    <td>${item.returnTime || '-'}</td>
                    <td>
                    <button class="btn-search" onclick="openRequestUpdateModal('${item.attendanceId}', '${item.workDate}')">수정</button>
                    </td>
                </tr>`;
        });
    } else {
        html += `<tr><td colspan="5" class="text-center">데이터가 없습니다.</td></tr>`;
    }
    $('#tableContainer').html(html + `</tbody></table></div>`);

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

    $('#modalEmpIdDisplay').text(id);
    $('#attendanceId').val(id);
    $('#modalInTime').val(inTime);
    $('#modalOutTime').val(outTime);
    $('#modalGoOutTime').val(goOutTime);
    $('#modalReturnTime').val(returnTime);

    modal.style.display = "block";
}

function saveTime() {
    const params = {
        attendanceId: $('#attendanceId').val(),
        inTime: $('#modalInTime').val(),
        outTime: $('#modalOutTime').val(),
        goOutTime: $('#modalGoOutTime').val(),
        returnTime: $('#modalReturnTime').val()
    };

    $.ajax({
        url: '/attendance/commute/update',
        type: 'POST',
        data: params,
        success: function() {
            alert("수정이 완료되었습니다.");
            closeModal();
            // 현재 활성화된 탭에 따라 리스트 갱신
            $('.tab-item#tabMy').hasClass('active') ? loadCommuteList() : loadCommuteAllList();
        },
        error: () => alert("수정 중 오류가 발생했습니다.")
    });
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

// 페이지네이션
//function renderPagination(pageData) {
//    let html = '<div class="pagination-wrapper" style="text-align:center; margin-top:20px;">';
//
//    // 이전 버튼
//    if (!pageData.first) {
//        html += `<button class="btn-page" onclick="loadCommuteAllList(${pageData.number - 1})">이전</button>`;
//    }
//
//    // 페이지 번호 루프
//    for (let i = 0; i < pageData.totalPages; i++) {
//        const isCurrent = (i === pageData.number);
//        html += `
//            <button class="btn-page ${isCurrent ? 'active' : ''}"
//                    onclick="loadCommuteAllList(${i})"
//                    style="margin: 0 3px; padding: 5px 10px; ${isCurrent ? 'background:#4f46e5; color:white;' : ''}">
//                ${i + 1}
//            </button>`;
//    }
//
//    // 다음 버튼
//    if (!pageData.last) {
//        html += `<button class="btn-page" onclick="loadCommuteAllList(${pageData.number + 1})">다음</button>`;
//    }
//
//    html += '</div>';
//    $('#paginationContainer').html(html);
//}