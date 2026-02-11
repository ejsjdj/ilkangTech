let currentState = {
    pageNum: 0,
    searchField: ''
};


$(document).ready(function () {
    // 초기 로드
    loadAppointmentList();
});

// 검색 실행
function handleSearch() {
    currentState.searchField = $('#searchInput').val();
    currentState.pageNum = 0;
    loadAppointmentList();
}

// 초기화
function resetSearch() {
    $('#searchInput').val('');
    currentState.searchField = '';
    currentState.pageNum = 0;
    loadAppointmentList();
}

// 조회 시도
function loadAppointmentList(page = 0) {
    currentState.pageNum = page;


    $.ajax({
        url: '/hr/appointment/list',
        type: 'GET',
        data: {
            page: page,
            size: 10,
            searchField: currentState.searchField
        },
        success: function (response) {
            renderTable(response)
        },
        error: function () {
            const container = document.getElementById('tableContainer');
            container.innerHTML =
                '<div class="empty-state">' +
                '<i class="bi bi-exclamation-circle"></i>' +
                '<p>발령 목록을 불러오는 중 오류가 발생했습니다.</p>' +
                '</div>';
        }
    });
}

function renderTable(pageData) {
    const container = $('#tableContainer');

    // 테이블 헤더 시작
    let html = `
        <div class="table-scroll">
            <table class="table emp-table">
                <thead>
                    <tr>
                        <th>발령대상자</th>
                        <th>이전 부서</th>
                        <th>현재 부서</th>
                        <th>이전 직급</th>
                        <th>현재 직급</th>
                        <th>근무 상태</th>
                        <th>상태</th>
                        <th>승인일</th>
                    </tr>
                </thead>
                <tbody>
    `;

    if (pageData && pageData.content && pageData.content.length > 0) {
        pageData.content.forEach(item => {
            html += `<tr>
                <td>${item.memberId || '-'}</td>
                <td>${item.preDept || '-'}</td>
                <td>${item.currentDept || '-'}</td>
                <td>${item.preRank || '-'}</td>
                <td>${item.currentRank || '-'}</td>
                <td>${item.workStatus || '-'}</td>
                <td>${item.apprveStatus || '-'}</td>
                <td>${item.approveDate || '-'}</td>
            </tr>`;
        });
    } else {
        html += `<tr><td colspan="8" class="text-center">데이터가 없습니다.</td></tr>`;
    }

    html += `</tbody></table></div>`;

    // 페이지네이션 블록
    let paginationHtml = `
            <div class="pagination-wrapper" style="display: flex; align-items: center; margin-top: 20px;">
                <ul class="pagination" style="display: flex; list-style: none; padding: 0;">
        `;


    if (pageData) {
        // [이전] 버튼
        if (!pageData.first) {
            paginationHtml += `<li><a href="#" class="page-link" onclick="goToPageByPageNum(${pageData.number - 1}); return false;"><i class="bi bi-chevron-left"></i></a></li>`;
        } else {
            // disabled 상태에서도 아이콘이 보이도록 수정
            paginationHtml += `<li class="disabled"><span class="page-link" style="color: #ccc; cursor: not-allowed;"><i class="bi bi-chevron-left"></i></span></li>`;
        }

        // [숫자] 버튼
        const blockSize = 5;
        const startPage = Math.floor(pageData.number / blockSize) * blockSize;
        const endPage = Math.min(startPage + blockSize, pageData.totalPages);

        for (let i = startPage; i < endPage; i++) {
            if (i === pageData.number) {
                paginationHtml += `
                    <li class="active">
                        <span style="background-color: #2563eb; color: #ffffff; font-weight: bold; padding: 5px 12px; border-radius: 4px; margin: 0 5px;">
                            ${i + 1}
                        </span>
                    </li>`;
            } else {
                paginationHtml += `
                    <li>
                        <a href="#" style="padding: 5px 12px; color: #64748b; text-decoration: none;" onclick="goToPageByPageNum(${i}); return false;">
                            ${i + 1}
                        </a>
                    </li>`;
            }
        }

        // [다음] 버튼
        if (!pageData.last) {
            paginationHtml += `<li><a href="#" onclick="goToPageByPageNum(${pageData.number + 1}); return false;"><i class="bi bi-chevron-right"></i></a></li>`;
        } else {
            paginationHtml += `<li class="disabled"><span style="color: #ccc;"><i class="bi bi-chevron-right"></i></span></li>`;
        }
    }

    paginationHtml += `
            </ul>
            <div style="margin-left: 20px; font-size: 13px; color: #94a3b8; font-weight: 500;">
                Total ${pageData ? pageData.totalElements : 0} Appointments
            </div>
        </div>
    `;

    // 최종 출력
    container.html(html + paginationHtml);
}

function goToPageByPageNum(pageNum) {
    loadAppointmentList(pageNum);
}

// 검색
function handleSearch(){
    currentState.searchField = $('#searchInput').val();
    currentState.pageNum = 0;
    loadAppointmentList(0);
}

// 초기화
function resetSearch() {
    $('#searchInput').val('');
    currentState.searchField = '';
    currentState.pageNum = 0;
    loadAppointmentList(0);
}



