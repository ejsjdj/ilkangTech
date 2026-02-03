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

// 발령 데이터 가공
function registAppointment() {
    // 1. 현재 화면에 입력/선택된 값 가져오기
    const userName = document.getElementById('hiddenName').value;
    const newDept = document.querySelector('select[name="department"]').value;
    const currentDept = document.getElementById('currentDeptDisplay').value;
    const newRank = document.querySelector('select[name="position"]').value;
    const currentRank = document.getElementById('currentRankDisplay').value;

    // --- 제목 생성 로직 시작 ---
    let generatedTitle = '';
    let generatedContent = '';
    const isDeptChanged = (newDept && newDept !== currentDept);
    const isRankChanged = (newRank && newRank !== currentRank);

    if (isDeptChanged && isRankChanged) {
        generatedTitle += `[인사이동]${currentDept} ${userName} 직원 영전 요청의 건`;
        generatedContent += `해당 인사의 업무 성과 달성 및 조직개편으로 인한 인사 발령(${currentDept} -> ${newDept}, ${currentRank} -> ${newRank})이 있으니, 담당자분들은 확인하시고 승인/반려 해주시기 바랍니다.`;
    } else if (isDeptChanged) {
        generatedTitle += `[인사이동]${currentDept} ${userName} 직원 전보/전직 요청의 건`;
        generatedContent += `조직 개편으로 인한 인사발령(${currentDept} -> ${newDept}, ${currentRank} -> ${newRank})을 요청하오니, 담당자분들은 확인하시고 승인/반려 해주시기 바랍니다.`;
    } else if (isRankChanged) {
        generatedTitle += `[인사이동]${currentDept} ${userName} 직원 승진 요청의 건`;
        generatedContent += `인사평가 결과 목표 및 역량 달성으로 인한 인사 발령(${currentDept} -> ${newDept}, ${currentRank} -> ${newRank})을 요청하오니, 담당자분들은 확인하시고 승인/반려 해주시기 바랍니다.`;
    } else {
        generatedTitle += `인사 발령 요청서`; // 변경사항이 없는 기본 케이스
    }

    postDraft(generatedTitle, generatedContent)


}

/* 결재 문서 등록 */
async function postDraft(generatedTitle, generatedContent){

    const baseDate = document.getElementById('selectedDate').value;
    const startDate = today();
    const endDate = plusDays(startDate, 3);


    // 전자결재 등록
    const appointmentRegistDTO = {
        memberId: document.getElementById('selectedMemberId').value,
        newDept: document.querySelector('select[name="department"]').value,
        newRank: document.querySelector('select[name="position"]').value,
        workStatus: document.querySelector('select[name="bank"]').value, // select의 name
        approveStatus: "대기"
    };

    // 2. 통합 데이터 생성
    const draftData = {
        draftTitle: generatedTitle,
        draftContent: generatedContent,
        draftType: 'APP',
        draftStartDate: startDate,
        draftEndDate: endDate,
        draftStatus: "대기",
        // [중요] 상세 정보를 객체 형태로 포함
        appointmentRegistDTO: appointmentRegistDTO
    };



    // 3. appointment 등록
    // userId, newDept, newRank, workStatus`;

    console.log("발령 데이터 : ", draftData);

    // 전송
    try {
        const response = await fetch('/draft/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(draftData)
        });

        // 3. 응답 처리
        if (response.ok) {
            alert('발령 등록이 완료되었습니다. 전자결재 탭을 확인하세요.');
        } else {
            const errorText = await response.text();
            console.error('Error:', errorText);
            alert('발령 등록에 실패했습니다. 다시 시도해주세요');
        }

    } catch (error) {
        console.error('Fetch error:', error);
        alert('서버와 통신 중 오류가 발생했습니다. 재접속 해주세요');
    }
}


/* 날짜 계산 */
function plusDays(dateStr, days) {
  const d = new Date(dateStr);
  d.setDate(d.getDate() + days);
  return d.toISOString().slice(0, 10);
}

/* 오늘날짜 정의 */
function today() {
  const d = new Date();
  const yyyy = d.getFullYear();
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const dd = String(d.getDate()).padStart(2, '0');
  return `${yyyy}-${mm}-${dd}`;
}
