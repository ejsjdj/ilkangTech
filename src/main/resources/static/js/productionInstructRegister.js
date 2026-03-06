let memberList = [];

document.addEventListener("DOMContentLoaded", function () {
  const planeSelect = document.getElementById("planeSelect");
  const tbody = document.getElementById("processTableBody");
  const registerBtn = document.getElementById("registerBtn");
  const instructCodeInput = document.getElementById("instructCode");

  if (!registerBtn) {
    console.error("registerBtn을 찾을 수 없습니다.");
    return;
  }

  registerBtn.addEventListener("click", function () {
    console.log("🔥 버튼 클릭됨");
  });

  loadProductionPlanes();
  loadMembers();

  // 생산계획 변경 시 → 작업지시코드 자동 생성
  planeSelect.addEventListener("change", function () {
    const planeId = this.value;

    if (!planeId) {
      tbody.innerHTML = "";
      instructCodeInput.value = "";
      return;
    }

    // 🔥 작업지시코드 자동 생성 (임시)
    const autoCode = "INS-" + Date.now();
    instructCodeInput.value = autoCode;

    fetch(`/api/production/${planeId}/processes`)
      .then((res) => {
        if (!res.ok) throw new Error("공정 조회 실패");
        return res.json();
      })
      .then((data) => {
        console.log(data);
        tbody.innerHTML = "";

        data.forEach((process) => {
          let memberOptions = '<option value="">-- 담당자 선택 --</option>';

          memberList.forEach((member) => {
            memberOptions += `
              <option value="${member.id}">
                ${member.name}
              </option>
            `;
          });

          const tr = document.createElement("tr");

          tr.innerHTML = `
              <td>${process.sequence}</td>
              <td>${process.outPutItemId}</td>
              <td>${process.processCode}</td>
              <td>${process.processName}</td>
              <td>${process.productionQty}</td>
              <td>
                <input type="number"
                class="additionQty"
                value="0"
                min="0" />
                </td>
              <td>
              <select class="memberSelect"
                data-sequence="${process.sequence}"
                data-process-id="${process.processId}"
                data-production-qty="${process.productionQty}"
                data-output-item-id="${process.outPutItemId}">
                ${memberOptions}
              </select>
            </td>
          `;

          tbody.appendChild(tr);
        });
      })
      .catch((error) => console.error(error));
  });

  // 🔥 등록 버튼
  registerBtn.addEventListener("click", function () {
    const planeId = planeSelect.value;
    const instructCode = instructCodeInput.value;
    const instructQty = document.getElementById("instructQty").value;

    const startTime = document.getElementById("startTime")?.value || null;
    const endTime = document.getElementById("endTime")?.value || null;

    const workerData = [];

    const rows = document.querySelectorAll("#processTableBody tr");

    rows.forEach((row) => {
      const select = row.querySelector(".memberSelect");
      const additionInput = row.querySelector(".additionQty");
      const sequence = Number(select.dataset.sequence);
      const outPutItemId = Number(select.dataset.outputItemId);

      const processId = Number(select.dataset.processId);
      const productionQty = Number(select.dataset.productionQty);

      const memberId = 1;
      const additionQty = Number(additionInput.value || 0);

      workerData.push({
        processId: processId,
        memberId: memberId,
        productionQty: productionQty,
        additionQty: additionQty,
        startTime: startTime,
        endTime: endTime,
        outputItemId: outPutItemId,
        sequence: sequence
      });
    });

    const requestData = {
      instructCode: instructCode,
      productionId: Number(planeId),
      item: 1,
      instructQty: Number(instructQty),
      defective: 0,
      status: "READY",
      workers: workerData,
    };

    console.log("전송 데이터", requestData);

    fetch("/api/production_instruct/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(requestData),
    })
      .then((res) => {
        if (!res.ok) throw new Error("등록 실패");
        return res.json();
      })
      .then(() => {
        alert("작업지시 등록 완료");
        location.reload();
      })
      .catch((err) => {
        console.error(err);
        alert("등록 오류");
      });
  });
});

// 생산계획 목록
function loadProductionPlanes() {
  fetch("/api/production/plane/all")
    .then((res) => {
      if (!res.ok) throw new Error("생산계획 조회 실패");
      return res.json();
    })
    .then((data) => {
      const select = document.getElementById("planeSelect");

      select.innerHTML =
        '<option value="">-- 생산계획을 선택하세요 --</option>';

      data.forEach((plane) => {
        const option = document.createElement("option");
        option.value = plane.id;
        option.textContent = plane.planeCode;
        select.appendChild(option);
      });
    })
    .catch((error) => {
      console.error(error);
    });
}

// 작업자 목록
function loadMembers() {
  fetch("/api/member/list")
    .then((res) => {
      if (!res.ok) throw new Error("작업자 조회 실패");
      return res.json();
    })
    .then((data) => {
      memberList = data;
    })
    .catch((error) => {
      console.error(error);
    });
}
