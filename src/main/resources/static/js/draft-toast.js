let currentDraftId = null;

const grid = new tui.Grid({
    el: document.getElementById('draftGrid'),
    scrollX: false,
    scrollY: true,
    rowHeaders: ['rowNum'],
    pageOptions: {
        useClient: false,
        perPage: 10
    },
    columns: [
        { header: 'ID', name: 'draftId', width: 80 },
        { header: '제목', name: 'draftTitle' },
        { header: '시작일', name: 'draftStartDate' },
        { header: '종료일', name: 'draftEndDate' },
        { header: '상태', name: 'draftStatus' },
        {
            header: '상세',
            formatter: ({ row }) =>
                `<button onclick="openDetail(${row.draftId})">보기</button>`
        }
    ]
});
