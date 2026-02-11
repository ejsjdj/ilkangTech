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
            let statusClass = '';
            let actionBtnHtml = ''; // 버튼을 담을 변수
            const status = item.status ?? '-';
            const mId = item.memberId ?? ''; // 사원 ID
            const empId = item.id; // 근태 ID (API 호출용)

            // 1. 상태별 클래스 및 버튼 분기
            if (status === '출근') {
                statusClass = 'status-on';
                // 출근 상태일 때 -> 퇴근 버튼
                actionBtnHtml = `<button class="btn-action btn-off" onclick="handleWorkAction('${mId}', 'off')">퇴근</button>`;
            } else if (status === '외근') {
                statusClass = 'status-out';
                // 외근 상태일 때 -> 복귀 버튼
                actionBtnHtml = `<button class="btn-action btn-return" onclick="handleWorkAction('${mId}', 'return')">복귀</button>`;
            } else if (status === '퇴근') {
                statusClass = 'status-off';
                actionBtnHtml = `<span class="text-muted">종료</span>`;
            } else {
                statusClass = 'status-default';
                actionBtnHtml = `<a class="action-btn action-view">상세</a>`;
            }

            html += `
                <tr>
                    <td>${item.name ?? '-'}</td>
                    <td>${item.department ?? '-'}</td>
                    <td>${item.position ?? '-'}</td>
                    <td><span class="status-badge ${statusClass}">${status}</span></td>
                    <td>${actionBtnHtml}</td>
                </tr>
            `;
        });
    } else {
        html += `<tr><td colspan="5" class="text-center text-muted py-4">데이터가 없습니다.</td></tr>`;
    }

    html += `</tbody></table>`;
    container.html(html);
}

function handleWorkAction(mId, type) {
    let url = '';
    let confirmMsg = '';

    if (type === 'off') {
        url = '/attendance/work/updateGoOut';    // 퇴근 엔드포인트
        confirmMsg = '퇴근 처리하시겠습니까?';
    } else if (type === 'return') {
        url = '/attendance/work/updateComeBack'; // 복귀 엔드포인트
        confirmMsg = '복귀 처리하시겠습니까?';
    }

    if (!confirm(confirmMsg)) return;

    $.ajax({
        url: url,
        type: 'POST',
        data: { userId: mId },
        success: function(res) {
            console.log(res);
            alert('처리되었습니다.');
            // 현재 리스트 새로고침
            const $deptSelect = $('#deptFilterContainer select');
            loadAttendanceData($deptSelect.val(), new Date().toISOString().split('T')[0]);
        },
        error: function(err) {
            alert('오류가 발생했습니다.');
        }
    });
}