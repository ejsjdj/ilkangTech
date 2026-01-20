console.log("전자결재 리스트 페이지 로드됨");

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
function draftDetail(draftId){
    const modal = document.getElementById("draftDetailModal");
    modal.style.display = 'block'

    console.log("문서 ID " + draftId);

    fetch(`/draft/detail?draftId=${draftId}`)
}


/*모달 닫기*/
function closeModal() {
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



/*결재 등록하기*/
async function postDraft(){

    const draftData = {
        draftTitle: document.getElementById('draftTitle').value,
        draftContent: document.getElementById('draftContent').value,
        draftType: document.getElementById('draftType').value,
        draftFile: document.getElementById('draftFile').value,
        draftStartDate: document.getElementById('startDate').value,
        draftEndDate: document.getElementById('endDate').value,
        draftStatus: "WAT",
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