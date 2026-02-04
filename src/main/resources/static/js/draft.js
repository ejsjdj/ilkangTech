// 문서 ID 전역변수
let currentDraftId = null;

/*모달 열기*/
function openModal() {
    const modal = document.getElementById('draftModal');
    modal.style.display = 'block'

    const today = new Date().toISOString().split('T')[0];
    document.getElementById('startDate').value = today;
}

/*모달 닫기*/
function closeModal() {
    const modal = document.getElementById('draftModal');
    modal.style.display = 'none'
}

/*상세보기 모달 열기*/
function draftDetail(draftId) {
    currentDraftId = draftId;

    const modal = document.getElementById("draftDetailModal");
    modal.style.display = 'block';

    console.log("문서 ID 조회 시작: " + draftId);

    fetch(`/draft/detail?draftId=${draftId}`)
        .then(response => response.json())
        .then(data => {
            const buttonGroup = document.getElementById('detail_Button_Group');
            const loginId = String(data.userId);
            const writerId = String(data.detailWriterId);
            const roles = data.detailRoles || [];
            console.log("로그인 유저 ID:", loginId);
            console.log("작성자 ID:", writerId);
            console.log("유저 권한:", roles);

            // 데이터 매핑
            const titleView = document.getElementById('detailTitle_view');
            const contentView = document.getElementById('detailContent_view');
            const startView = document.getElementById('detailStartDate_view');
            const endView = document.getElementById('detailEndDate_view');
            const fileView = document.getElementById('fileListContainer');


            // 1. 텍스트 값 세팅
            if (titleView) titleView.value = data.detailTitle || "";
            if (contentView) contentView.value = data.detailContent || "";

            // 2. 날짜 세팅
            if (startView && data.detailStartDate) {
                startView.value = data.detailStartDate.substring(0, 10);
            }
            if (endView && data.detailEndDate) {
                endView.value = data.detailEndDate.substring(0, 10);
            }

            // 3. 파일 목록 렌더링
            if (data.detailFile && data.detailFile.length > 0) {
                renderFileList(data.detailFile);
            } else {
                renderFileList([]);
            }

            if (buttonGroup) {
                const isManager = roles.includes('CEO') || roles.includes('HR');

                // ✔ 작성자가 아니거나 ✔ 관리자라면 버튼 표시
                if (writerId !== loginId || isManager) {
                    buttonGroup.style.display = 'block';
                } else {
                    buttonGroup.style.display = 'none';
                }
            }
        })
        .catch(err => console.error("데이터 로드 중 에러:", err));
}

/*파일 다운로드*/
async function renderFileList(fileList){
    const fileContainer = document.getElementById('fileListContainer'); // div나 ul 태그
    fileContainer.innerHTML = '';

    if (fileList && fileList.length > 0) {
        fileList.forEach(file => {
            const link = document.createElement('a');
            link.href = `/file/download/${file.fileId}`;
            link.innerText = `📎 파일 다운로드 (ID: ${file.fileId})`;
            link.style.display = 'block';
            link.style.color = 'black';
            link.style.marginBottom = '4px';
            link.className = 'download-link';

            fileContainer.appendChild(link);
        });
    } else {
        fileContainer.innerText = "첨부된 파일이 없습니다.";
    }

    console.log(fileContainer.innerHTML);
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
            // 초기화
            for(let i=1; i<=3; i++) {
                document.getElementById(`approver${i}`).value = '';
                document.getElementById(`display_approver${i}`).innerText = '미지정';
            }

            data.sort((a, b) => a.sequence - b.sequence);

            data.forEach((item) => {
                const seq = item.sequence;
                const hiddenInput = document.getElementById(`approver${seq}`);
                const displayDiv = document.getElementById(`display_approver${seq}`);

                if (hiddenInput && displayDiv) {
                    // 서버로 보낼 값 (ID 등)
                    hiddenInput.value = item.id;
                    // 화면에 보여줄 값 (직급 + 성함)
                    displayDiv.innerText = `${item.name} ${item.position}`;
                    displayDiv.style.color = "#007bff"; // 지정된 사람은 파란색으로 강조
                    displayDiv.style.fontWeight = "bold";
                }
            });
        })
        .catch(error => console.error('결재 라인 로드 실패:', error));
}

/*결재 승인하기*/
async function decideApprove(decision){
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
try {
        let uploadedFile = null;

        const fileInput = document.getElementById("draftFile");

        // 파일 업로드 (있을 때만)
        if (fileInput.files.length > 0) {
            uploadedFile = await uploadFile();
        } else {
            alert("첨부파일을 등록하세요.")
        }

        // 결재 데이터 구성 (JSON)
        const draftData = {
            draftTitle: document.getElementById('draftTitle').value,
            draftContent: document.getElementById('draftContent').value,
            draftType: document.getElementById('draftType').value,

            // file 객체 → fileId만
            draftFile: [{ fileId: uploadedFile.fileId }],

            draftStartDate: document.getElementById('startDate').value,
            draftEndDate: document.getElementById('endDate').value,
            draftTotalDate: calculateDays(),
            draftStatus: "대기",

            draftApprover: [
                document.getElementById('approver1').value,
                document.getElementById('approver2').value,
                document.getElementById('approver3').value
            ]
        };

        console.log(draftData);

        // 결재 등록
        const response = await fetch('/draft/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(draftData)
        });

        if (!response.ok) {
            throw new Error(await response.text());
        }

        alert('결재 등록이 완료되었습니다.');

    } catch (error) {
        alert('등록 중 오류가 발생했습니다.');
    }
}

/*파일 전송*/
async function uploadFile(){
    const fileInput = document.getElementById("draftFile");

    const formData = new FormData();
    formData.append("file", fileInput.files[0]);

    const res = await fetch("/file/upload", {
         method: "POST",
         body: formData
    });

        return await res.json();
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