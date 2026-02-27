document.addEventListener("DOMContentLoaded", function () {

    const select = document.getElementById('purchaseRequestSelect');

    // 1️⃣ 구매요청 목록 조회
    fetch('/purchase_request_list')
        .then(response => response.json())
        .then(data => {

            data.forEach(pr => {

                const option = document.createElement('option');
                option.value = pr.id;
                option.textContent = pr.purchaseRequestCode;

                select.appendChild(option);
            });

        })
        .catch(error => {
            console.error('구매요청 목록 조회 실패:', error);
        });

    // 2️⃣ 드롭다운 선택 시 상세조회
    select.addEventListener('change', function () {

        const selectedId = this.value;

        if (!selectedId) return;

        fetch(`/purchase_request_detail?requestId=${selectedId}`)
            .then(response => response.json())
            .then(detail => {

                console.log("=== 구매요청 상세 ===");
                console.log("ID:", detail.id);
                console.log("코드:", detail.purchaseRequestCode);
                console.log("요청자:", detail.memberName);
                console.log("요청일:", detail.requestDate);
                console.log("납기일:", detail.dueDate);
                console.log("납품장소:", detail.deliveryLocate);
                console.log("계약구분:", detail.contractType);
                console.log("제작구분:", detail.produceType);

                console.log("=== 라인 목록 ===");
                detail.lines.forEach(line => {
                    console.log("라인ID:", line.id,
                                "품목ID:", line.itemId,
                                "수량:", line.quantity);
                });

            })
            .catch(error => {
                console.error('구매요청 상세조회 실패:', error);
            });

    });

});