let currentStep = null;
let selectedEmployee = null;
let employeeCache = [];
let currentPage = 0;
let loading = false;
let isLastPage = false;


// 결재자 제거
function removeApprover(step) {
    document.getElementById(`approver${step}`).value = '';
    document.getElementById(`display_approver${step}`).innerText = '결재자 없음';

    document.querySelector(`[data-step="${step}"] .btn-remove`).style.display = 'none';
    document.querySelector(`[data-step="${step}"] .btn-add`).style.display = 'block';
}

// 직원 목록 모달 오픈
function openEmployeeModal(step) {
    currentStep = step;
    selectedEmployee = null;
    currentPage = 0;
    isLastPage = false;
    employeeCache = [];

    document.getElementById('employeeList').innerHTML = '';
    document.getElementById('employeeModal').style.display = 'block';

    loadEmployeeList();
}

// 직원 목록 불러오기
async function loadEmployeeList() {
    if (loading || isLastPage) return;
    loading = true;

    const response = await fetch(`/account/getList?page=${currentPage}`);
    const data = await response.json();

    employeeCache.push(...data.content);
    renderEmployeeList(data.content);

    isLastPage = data.last;
    currentPage++;
    loading = false;
}

// 직원 목록 렌더링
function renderEmployeeList(list) {
    const ul = document.getElementById('employeeList');

    list.forEach(emp => {
        const li = document.createElement('li');
        li.className = 'employee-item';
        li.innerText = ` (${emp.department}) ${emp.position} ${emp.name}`;

        li.onclick = () => selectEmployee(emp);

        ul.appendChild(li);
    });
}

// 직원 선택
function selectEmployee(emp) {
    selectedEmployee = emp;

    document.getElementById('selectedEmployeeArea').innerText =
        `${emp.name} / ${emp.department}`;
}

// 결재라인 반영
function confirmEmployeeSelection() {
    if (!selectedEmployee || !currentStep) return;

//    for (let i = 1; i <= 3; i++) {
//            if (i !== currentStep) {
//                const val = document.getElementById(`approver${i}`).value;
//                if (val === selectedId) {
//                    alert("이미 결재라인에 포함된 직원입니다.");
//                    return;
//                }
//            }
//        }


    document.getElementById(`approver${currentStep}`).value = selectedEmployee.id;
    document.getElementById(`display_approver${currentStep}`).innerText =
        `${selectedEmployee.name} / ${selectedEmployee.department}`;

    document.querySelector(`[data-step="${currentStep}"] .btn-remove`).style.display = 'block';
    document.querySelector(`[data-step="${currentStep}"] .btn-add`).style.display = 'none';

    closeEmployeeModal();
}

function closeEmployeeModal() {
    document.getElementById('employeeModal').style.display = 'none';
}

