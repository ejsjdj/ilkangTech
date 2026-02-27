document.addEventListener('DOMContentLoaded', function () {

    try {
        console.log("DOM 로드 완료");

        const gridElement = document.getElementById('grid');
        console.log("grid element:", gridElement);

        const grid = new tui.Grid({
            el: gridElement,
            columns: []
        });

        console.log("grid 생성 완료");

        fetch('/api/sales/procurement')
            .then(res => res.json())
            .then(data => console.log("데이터 도착", data));

    } catch (e) {
        console.error("에러 발생:", e);
    }

});