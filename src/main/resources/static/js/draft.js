// 전역 변수 설정
let currentDraftId = null;
let editor; // 작성용 에디터 객체
let viewer; // 상세보기용 뷰어 객체

// 페이지 로드 시 에디터 초기화
document.addEventListener('DOMContentLoaded', function() {
    if (document.getElementById('draftEditor')) {
        editor = new toastui.Editor({
            el: document.getElementById('draftEditor'),
            height: '400px',
            initialEditType: 'wysiwyg',
            previewStyle: 'vertical'
        });
    }
});

/* 상단 네비게이터 전환 */
function switchTab(tabType) {
    const form = document.querySelector('.search-form');
    form.querySelector('input[name="type"]').value = tabType;
    form.submit();
}

/* 문서 등록 모달 열기 */
function openModal() {
    const modal = document.getElementById('draftModal');
    modal.style.display = 'block';

    // 에디터 내용 초기화
    if(editor) editor.setMarkdown("");

    const today = new Date().toISOString().split('T')[0];
    document.getElementById('startDate').value = today;
}

/* 모달 닫기 */
function closeModal() {
    document.getElementById('draftModal').style.display = 'none';
}

/* 상세보기 모달 열기 */
function draftDetail(draftId) {
    currentDraftId = draftId;
    const modal = document.getElementById("draftDetailModal");
    modal.style.display = 'block';

    fetch(`/draft/detail?draftId=${draftId}`)
        .then(response => response.json())
        .then(data => {
            const buttonGroup = document.getElementById('detail_Button_Group');
            const loginId = String(data.userId);
            const writerId = String(data.detailWriterId);
            const roles = data.detailRoles || [];

            // 1. 텍스트 및 제목 세팅
            document.getElementById('detailTitle_view').value = data.detailTitle || "";

            // 2. TOAST UI Viewer 로 렌더링 (매번 새로 생성하여 내용 교체)
            const viewerEl = document.getElementById('detailContent_view');
            viewerEl.innerHTML = ''; // 초기화
            viewer = toastui.Editor.factory({
                el: viewerEl,
                viewer: true,
                initialValue: data.detailContent || ""
            });

            // 3. 날짜 세팅
            if (data.detailStartDate) document.getElementById('detailStartDate_view').value = data.detailStartDate.substring(0, 10);
            if (data.detailEndDate) document.getElementById('detailEndDate_view').value = data.detailEndDate.substring(0, 10);

            // 4. 파일 목록
            renderFileList(data.detailFile || []);

            // 5. 버튼 권한 체크
            if (buttonGroup) {
                const isManager = roles.includes('CEO') || roles.includes('HR');
                buttonGroup.style.display = (writerId !== loginId || isManager) ? 'block' : 'none';
            }
            renderApprovalLine(data.approvalLine);
        })
        .catch(err => console.error("데이터 로드 중 에러:", err));
}

/* 파일 목록 렌더링 */
async function renderFileList(fileList){
    const fileContainer = document.getElementById('fileListContainer');
    fileContainer.innerHTML = '';
    fileList.forEach(file => {
        const link = document.createElement('a');
        link.href = `/file/download/${file.fileId}`;
        link.innerText = `📎 파일 다운로드 (ID: ${file.fileId})`;
        link.className = 'download-link';
        link.style.display = 'block';
        link.style.color = 'black';
        link.style.marginBottom = '4px';
        fileContainer.appendChild(link);
    });
}

/* 결재라인 렌더링 (기존 로직 유지) */
function renderApprovalLine(approvalLine) {
    for (let i = 1; i <= 3; i++) {
        const displayDiv = document.getElementById(`detail_display_approver${i}`);
        if (displayDiv) displayDiv.innerHTML = '<span style="color:#ccc">대기 중</span>';
    }
    if (!approvalLine) return;
    approvalLine.forEach(item => {
        const seq = item.sequence;
        const displayDiv = document.getElementById(`detail_display_approver${seq}`);
        if (displayDiv) {
            let statusColor = "#888";
            if (item.status === "승인") statusColor = "#28a745";
            if (item.status === "반려") statusColor = "#dc3545";
            if (item.status === "진행") statusColor = "#007bff";
            displayDiv.innerHTML = `
                <div style="font-weight:bold">${item.name} ${item.position}</div>
                <div style="font-size:0.85em;color:${statusColor}">[${item.status}]</div>
            `;
        }
    });
}

function closeDetailModal() {
    document.getElementById('draftDetailModal').style.display = 'none';
}

/* 결재라인 불러오기 */
function loadApprovalLine(draftType) {
    if (!draftType) return;
    fetch(`/draft/type?type=${draftType}`)
        .then(response => response.json())
        .then(data => {
            for(let i=1; i<=3; i++) {
                document.getElementById(`approver${i}`).value = '';
                document.getElementById(`display_approver${i}`).innerText = '미지정';
                document.getElementById(`display_approver${i}`).style.color = "#888";
            }
            data.sort((a, b) => a.sequence - b.sequence);
            data.forEach((item) => {
                const seq = item.sequence;
                const hiddenInput = document.getElementById(`approver${seq}`);
                const displayDiv = document.getElementById(`display_approver${seq}`);
                if (hiddenInput && displayDiv) {
                    hiddenInput.value = item.id;
                    displayDiv.innerText = `${item.name} ${item.position}`;
                    displayDiv.style.color = "#007bff";
                    displayDiv.style.fontWeight = "bold";
                }
            });
        });
}

/* 결재 승인/반려 */
async function decideApprove(decision){
    fetch('/draft/decide', {
        method: 'PUT',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({ draftId: currentDraftId, decision: decision })
    }).then(() => {
        alert(`${decision} 처리가 완료되었습니다.`);
        location.reload();
    });
}

/* 결재 등록하기 */
async function postDraft(){
    try {
        const fileInput = document.getElementById("draftFile");
        let draftFileArray = [];

        if (fileInput.files.length > 0) {
            const result = await uploadFile();
            draftFileArray.push({ fileId: result.fileId });
        }

        const draftData = {
            draftTitle: document.getElementById('draftTitle').value,
            draftContent: editor.getHTML(),
            draftType: document.getElementById('draftType').value,
            draftFile: draftFileArray,
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

        const response = await fetch('/draft/register', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(draftData)
        });

        if (!response.ok) throw new Error(await response.text());
        alert('결재 등록이 완료되었습니다.');
        location.reload();

    } catch (error) {
        console.error(error);
        alert('등록 중 오류가 발생했습니다.');
    }
}

/* 파일 업로드 */
async function uploadFile(){
    const fileInput = document.getElementById("draftFile");
    const formData = new FormData();
    formData.append("file", fileInput.files[0]);
    const res = await fetch("/file/upload", { method: "POST", body: formData });
    return await res.json();
}

/* 날짜 계산 및 주말 제외 (기존 로직 유지) */
function calculateDays() {
    const start = document.getElementById('startDate').value;
    const end = document.getElementById('endDate').value;
    if (start && end) {
        const startDate = new Date(start);
        const endDate = new Date(end);
        const totalBusinessDays = getBusinessDays(startDate, endDate);
        if (totalBusinessDays < 0) {
            alert("종료일은 시작일보다 빠를 수 없습니다.");
            document.getElementById('endDate').value = "";
            return;
        }
        return totalBusinessDays;
    }
}

function getBusinessDays(startDate, endDate) {
    let count = 0;
    let curDate = new Date(startDate);
    while (curDate <= endDate) {
        const dayOfWeek = curDate.getDay();
        if (dayOfWeek !== 0 && dayOfWeek !== 6) count++;
        curDate.setDate(curDate.getDate() + 1);
    }
    return count;
}

/* --- TOAST UI Grid 초기화 --- */
document.addEventListener('DOMContentLoaded', function() {
    if (document.getElementById('grid')) {
        // 데이터가 없는 경우 처리
        console.log('rawData:', rawData); // ✅ 여기

        const gridData = (rawData || []).map(item => ({
            id: item.draft_id,
            title: item.draft_title,
            start: item.draft_startDate ? item.draft_startDate.split('T')[0] : "-",
            end: item.draft_endDate ? item.draft_endDate.split('T')[0] : "-",
            status: item.draft_status
        }));

        const grid = new tui.Grid({
            el: document.getElementById('grid'),
            data: gridData,
            scrollX: false,
            scrollY: false,
            columns: [
                { header: '문서번호', name: 'id', width: 80, align: 'center' },
                {
                    header: '제목',
                    name: 'title',
                    sortable: true,
                    formatter: (props) => `<span style="color:#007aff; cursor:pointer; font-weight:bold;">${props.value}</span>`
                },
                { header: '시작일', name: 'start', sortable: true, width: 150, align: 'center' },
                { header: '종료일', name: 'end', sortable: true, width: 150, align: 'center' },
                {
                    header: '상태',
                    name: 'status',
                    filter: {
                        type: 'select',
                        options: {
                          listItems: [
                            { text: '대기', value: '대기' },
                            { text: '승인', value: '승인' },
                            { text: '반려', value: '반려' }
                          ]
                        }
                      },
                    width: 100,
                    align: 'center',
                    formatter: (props) => {
                        let cls =
                          props.value === '대기' ? 'badge-wait' :
                          props.value === '승인' ? 'badge-approve' :
                          'badge-reject';

                        return `<span class="status-badge ${cls}">${props.value}</span>`;
                      }
                },
                {
                    header: '보기',
                    name: 'action',
                    width: 80,
                    align: 'center',
                    formatter: () => `<button class="btn-grid-detail" style="border:1px solid #ddd; background:#fff; cursor:pointer; padding:2px 8px; font-size:12px;">조회</button>`
                }
            ],
            columnOptions: {
                resizable: true
            }
        });

        // 클릭 이벤트 (상세보기)
        grid.on('click', (ev) => {
            const { rowKey, columnName } = ev;
            if (rowKey !== undefined) {
                const rowData = grid.getRow(rowKey);
                console.log(rowData);
                // 제목 혹은 버튼 클릭 시
                if (columnName === 'title' || columnName === 'action') {
                    draftDetail(rowData.id);
                }
            }
        });

        // Grid 테마 적용
        tui.Grid.applyTheme('clean');
    }
});

