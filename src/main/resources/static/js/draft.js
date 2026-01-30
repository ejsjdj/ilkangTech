// 문서 ID 전역변수
let currentDraftId = null;

/*모달 열기*/
function openModal() {
    const modal = document.getElementById('draftModal');
    modal.style.display = 'block'
}

/*모달 닫기*/
function closeModal() {
    const modal = document.getElementById('draftModal');
    modal.style.display = 'none'
}

/*상세보기 모달 열기*/
function draftDetail(draftId) {
    // 모달 열때 문서 번호 전역변수에 저장
    currentDraftId = draftId;

    const modal = document.getElementById("draftDetailModal");
    modal.style.display = 'block'

    console.log("문서 ID " + draftId);

    fetch(`/draft/detail?draftId=${draftId}`)
        .then(response => response.json())
        .then(data => {
            console.log("받아온 데이터: ", data);

            const titleView = document.getElementById('detailTitle_view');
            const contentView = document.getElementById('detailContent_view');
            const startView = document.getElementById('detailStartDate_view');
            const endView = document.getElementById('detailEndDate_view');

            if (titleView) titleView.value = data.detailTitle || "";
            if (contentView) contentView.value = data.detailContent || "";

            if (startView && data.detailStartDate) {
                startView.value = data.detailStartDate.substring(0, 10);
            }
            if (endView && data.detailEndDate) {
                endView.value = data.detailEndDate.substring(0, 10);
            }
        });
}

/*모달 닫기*/
function closeDetailModal() {
    const modal = document.getElementById('draftDetailModal');
    modal.style.display = 'none'
}

/*결재라인 불러오기*/
function loadApprovalLine(draftType) {
    if (!draftType) return;

    fetch(`/draft/type?type=${draftType}`)
        .then(response => response.json())
        .then(data => {
            // 1. 기존 input들 초기화 (이전 데이터 삭제)
            document.getElementById('approver1').value = '';
            document.getElementById('approver2').value = '';
            document.getElementById('approver3').value = '';

            // 2. sequence 기준으로 오름차순 정렬 (1, 2, 3...)
            data.sort((a, b) => a.sequence - b.sequence);

            // 3. 정렬된 데이터를 각 input에 할당
            data.forEach((item) => {
                // sequence가 1이면 approver1, 2이면 approver2에 넣음
                const inputId = `approver${item.sequence}`;
                const targetInput = document.getElementById(inputId);

                if (targetInput) {
                    targetInput.value = `${item.id} ${item.position} ${item.name} ${item.sequence}`;
                }
            });
        })
        .catch(error => console.error('결재 라인 로드 실패:', error));
}

/*결재 승인하기*/
function decideApprove(decision){
    console.log(decision);
    fetch('/draft/decide', {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({
            draftId: currentDraftId,
            decision: decision
        })
    })
}

/*결재 등록하기*/
async function postDraft(){

    const draftData = {
        draftTitle: document.getElementById('draftTitle').value,
        draftContent: document.getElementById('draftContent').value,
        draftType: document.getElementById('draftType').value,
        draftFile: document.getElementById('draftFile').value,
        draftStartDate: document.getElementById('startDate').value,
        draftEndDate: document.getElementById('endDate').value,
        draftTotalDate: calculateDays(),
        draftStatus: "대기",
    }

    const draftApprover = [
        document.getElementById('approver1').value,
        document.getElementById('approver2').value,
        document.getElementById('approver3').value
    ];

    draftData.draftApprover = draftApprover;

    console.log(draftData);

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
            alert('결재 등록이 완료되었습니다.');
        } else {
            const errorText = await response.text();
            console.error('Error:', errorText);
            alert('등록에 실패했습니다.');
        }

    } catch (error) {
        console.error('Fetch error:', error);
        alert('서버와 통신 중 오류가 발생했습니다.');
    }
}

/*날짜 계산*/
function calculateDays() {
    const start = document.getElementById('startDate').value;
    const end = document.getElementById('endDate').value;

    if (start && end) {
        const startDate = new Date(start);
        const endDate = new Date(end);

        // 날짜 차이 계산 (밀리초 단위)
        const diffInMs = getBusinessDays(startDate, endDate);

        // 일 단위로 변환 (1000ms * 60s * 60m * 24h)
        const diffInDays = diffInMs / (1000 * 60 * 60 * 24);

        if (diffInDays < 0) {
            alert("종료일은 시작일보다 빠를 수 없습니다.");
            document.getElementById('endDate').value = "";
            return;
        }

        // 당일 포함을 위해 +1을 해줍니다.
        const totalBusinessDays = getBusinessDays(startDate, endDate);

        console.log("총 평일(휴가) 일수:", totalBusinessDays);
        return totalBusinessDays;
    }
}


/*주말 제외*/
function getBusinessDays(startDate, endDate) {
    let count = 0;
    let curDate = new Date(startDate);

    while (curDate <= endDate) {
        const dayOfWeek = curDate.getDay();
        if (dayOfWeek !== 0 && dayOfWeek !== 6) {
            count++;
        }
        curDate.setDate(curDate.getDate() + 1);
    }
    return count;
}