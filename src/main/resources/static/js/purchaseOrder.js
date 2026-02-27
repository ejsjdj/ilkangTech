document.addEventListener('DOMContentLoaded', function () {

    const Grid = tui.Grid;

    const grid = new Grid({
        el: document.getElementById('grid'),
        bodyHeight: 400,
        scrollX: false,
        scrollY: false,
        pageOptions: {
            useClient: true,
            perPage: 10
        },
        columns: [
            { header: '발주번호', name: 'purchaseOrderCode' },
            { header: '거래처명', name: 'company' },
            { header: '담당자', name: 'companyManager' },
            { header: '전화번호', name: 'phone' },
            { header: '발주담당자', name: 'name' },
            { header: '상태', name: 'status' },
            { header: '발주일자', name: 'orderDate' },
            { header: '총금액', name: 'amount' },
            {
                header: '상세',
                name: 'detail',
                align: 'center',
                formatter: () => {
                    return '<button class="detail-btn">상세보기</button>';
                }
            }
        ]
    });

    grid.on('click', ev => {

    if (ev.columnName === 'detail') {

        const rowData = grid.getRow(ev.rowKey);

        console.log("선택된 ID:", rowData.id);

        fetch(`/api/sales/procurement_deail?purchaseOrderId=${rowData.id}`)
            .then(res => res.json())
            .then(detailData => {
                console.log("상세 데이터:", detailData);
                // 여기서 모달 띄우거나 화면 이동
            });
        }
    });

    fetch('/api/sales/procurement')
        .then(res => res.json())
        .then(data => {
            console.log("데이터:", data);
            grid.resetData(data.content);  
        });

});

function goRegister() {
    location.href = '/sales/procurement/register';
}