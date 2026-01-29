/*<![CDATA[*/
const successMessage = /*[[${successMessage}]]*/ null;
const errorMessage = /*[[${errorMessage}]]*/ null;

if (successMessage) {
    Swal.fire({
        icon: 'success',
        title: '등록 완료',
        text: successMessage,
        confirmButtonColor: '#4f46e5'
    });
}

if (errorMessage) {
    Swal.fire({
        icon: 'error',
        title: '등록 실패',
        text: errorMessage,
        confirmButtonColor: '#4f46e5'
    });
}
/*]]>*/

// 1. 상태 전역 변수 선언
let currentState = {pageNum: 0, searchField: '', sortBy: 'id', direction: 'DESC'};

// 2. 페이지 로드 시 자동 실행
$(document).ready(function () {
    getEmployeeList();
});

// 3. 데이터 가져오기 함수
function getEmployeeList(pageNum = 0, searchField = '', sortBy = 'id', direction = 'DESC') {
    currentState = {pageNum, searchField, sortBy, direction};
    $.ajax({
        url: '/account/getList',
        type: 'GET',
        data: currentState,
        success: function (response) {
            renderEmployeeTable(response);
        },
        error: function () {
            $('#tableContainer').html('<p>데이터를 불러오지 못했습니다.</p>');
        }
    });
}


// 발령자 선택
function renderEmployeeTable(pageData) {
    const container = document.getElementById('tableContainer');
    let html = '';

    if (!pageData || !pageData.content || pageData.content.length === 0) {
        html = '<div class="p-3 text-center text-muted">조회된 직원이 없습니다.</div>';
    } else {
        pageData.content.forEach(emp => {
            // 클릭 시 선택 함수(selectEmployee) 호출
            html += `
                            <div class="emp-item" onclick="selectEmployee(this, '${emp.id}', '${emp.name}', '${emp.department}', '${emp.position}')">
                                <div class="emp-info-left">
                                    <span class="emp-info-main">${emp.name}</span>
                                    <span class="emp-info-sub">(${emp.employeeNumber || '-'})</span>
                                </div>
                                <div class="emp-info-right">
                                    <span class="emp-info-sub">${emp.department} / ${emp.position}</span>
                                </div>
                            </div>
                        `;
        });
    }

    container.innerHTML = html;
}


// 직원 선택 시 호출되는 함수
function selectEmployee(element, id, name, dept, pos) {
    // 1. 모든 아이템에서 active 클래스 제거 후 현재 아이템에 추가
    $('.emp-item').removeClass('active');
    $(element).addClass('active');

    // 2. Hidden input에 ID 값 설정 (폼 전송용)
    document.getElementById('selectedMemberId').value = id;

    // 3. 화면에 선택된 직원 정보 표시
    document.getElementById('selectedEmployeeDisplay').innerHTML =
        `<i class="bi bi-check-circle-fill"></i> 선택됨: ${name} (${dept} / ${pos})`;


    // 4. 선택된 직급, 부서 연결
    document.getElementById('currentDeptDisplay').value = dept;
    document.getElementById('currentRankDisplay').value = pos;
    document.getElementById('currentStatusDisplay').value = status;
}

// 발령 등록
function registAppointment() {
    // 1. 현재 화면에 입력/선택된 값 가져오기
    const userId = document.getElementById('selectedMemberId').value;
    const newDept = document.querySelector('select[name="department"]').value;
    const newRank = document.querySelector('select[name="position"]').value;
    const workStatus = document.querySelector('select[name="bank"]').value; // HTML의 name에 맞춤

    // 3. 컨트롤러 주소에 파라미터를 붙여서 이동 (GET 방식)
    // 주소 형식: /주소?userId=1&newDept=2...
    location.href = `/hr/appointment/insertData?userId=${userId}&newDept=${newDept}&newRank=${newRank}&workStatus=${encodeURIComponent(workStatus)}`;
}