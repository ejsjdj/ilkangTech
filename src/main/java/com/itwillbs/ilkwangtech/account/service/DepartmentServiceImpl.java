package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.OrgChartDTO;
import com.itwillbs.ilkwangtech.account.entity.Department;
import com.itwillbs.ilkwangtech.account.entity.Position;
import com.itwillbs.ilkwangtech.account.repository.DepartmentRepository;
import com.itwillbs.ilkwangtech.account.repository.PositionRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final MemberRepository memberRepository;
    private final PositionRepository positionRepository;

    @Override
    public List<Department> getActiveDepartments() {
        return departmentRepository.findByIsActiveTrue();
    }

    @Override
    public OrgChartDTO getOrgChartData() {
        List<Department> allDepts = departmentRepository.findAll();
        List<Position> allPositions = positionRepository.findAll();
        Map<Integer, String> posMap = allPositions.stream()
                .collect(Collectors.toMap(Position::getId, Position::getPositionName, (existing, replacement) -> existing));

        // 대표이사 찾기 (부서 0 소속 중 가장 높은 직급)
        List<Member> allMembers = memberRepository.findAll();
        List<Member> ceos = allMembers.stream()
                .filter(m -> m.getDepartment() != null && m.getDepartment().equals(0))
                .sorted((m1, m2) -> {
                    if (m1.getPosition() == null) return 1;
                    if (m2.getPosition() == null) return -1;
                    return m1.getPosition().compareTo(m2.getPosition());
                })
                .collect(Collectors.toList());
        
        String ceoName = "미지정";
        String ceoTitle = "대표이사";
        if (!ceos.isEmpty()) {
            ceoName = ceos.get(0).getName();
            ceoTitle = posMap.getOrDefault(ceos.get(0).getPosition(), "대표이사");
        }
        OrgChartDTO.CeoDTO ceoDTO = OrgChartDTO.CeoDTO.builder()
                .name(ceoName)
                .title(ceoTitle)
                .build();

        List<OrgChartDTO.DivisionDTO> divisions = new ArrayList<>();

        // 상위 부서들 (parent_department가 0이거나 null인 부서들 중 0을 제외)
        List<Department> mainDepts = allDepts.stream()
                .filter(d -> d.getId() != 0 && (Integer.valueOf(0).equals(d.getParentDepartment()) || d.getParentDepartment() == null))
                .filter(Department::isActive)
                .sorted((d1, d2) -> d1.getId().compareTo(d2.getId())) // 상위 부서 ID 순 정렬 추가
                .collect(Collectors.toList());

        for (Department dept : mainDepts) {
            String theme = dept.getId() >= 100 ? "green" : "blue";
            String stringId = "dept_" + dept.getId();
            
            // 특정 ID에 대한 예외 처리 (하위 호환성 및 CSS 적용용)
            if (dept.getId() == 1) stringId = "admin";
            else if (dept.getId() == 100) stringId = "production";

            divisions.add(createDivisionDTO(dept.getId(), stringId, theme, allDepts, posMap, allMembers));
        }

        return OrgChartDTO.builder()
                .ceo(ceoDTO)
                .divisions(divisions)
                .build();
    }

    private OrgChartDTO.DivisionDTO createDivisionDTO(Integer divId, String stringId, String theme, List<Department> allDepts, Map<Integer, String> posMap, List<Member> allMembers) {
        Department divDept = allDepts.stream().filter(d -> d.getId().equals(divId)).findFirst().orElse(null);
        String divName = (divDept != null) ? divDept.getDepartmentName() : "부서_" + divId;

        // 본부장 찾기 (해당 부서 소속 중 가장 높은 직급)
        List<Member> divMembers = allMembers.stream()
                .filter(m -> m.getDepartment() != null && m.getDepartment().equals(divId))
                .sorted((m1, m2) -> {
                    if (m1.getPosition() == null) return 1;
                    if (m2.getPosition() == null) return -1;
                    return m1.getPosition().compareTo(m2.getPosition());
                })
                .collect(Collectors.toList());
        
        String headName = "미지정";
        String headTitle = "";
        if (!divMembers.isEmpty()) {
            headName = divMembers.get(0).getName();
            headTitle = posMap.getOrDefault(divMembers.get(0).getPosition(), "");
        }

        // 하위 부서들 (3단계 팀들)
        List<OrgChartDTO.DepartmentDTO> departments = allDepts.stream()
                .filter(d -> d.getParentDepartment() != null && d.getParentDepartment().equals(divId))
                .filter(Department::isActive) // 활성화된 부서만 표시
                .sorted((d1, d2) -> d1.getId().compareTo(d2.getId())) // 부서 ID 순 정렬 추가
                .map(d -> {
                    List<Member> members = allMembers.stream()
                            .filter(m -> m.getDepartment() != null && m.getDepartment().equals(d.getId()))
                            .sorted((m1, m2) -> {
                                if (m1.getPosition() == null) return 1;
                                if (m2.getPosition() == null) return -1;
                                return m1.getPosition().compareTo(m2.getPosition());
                            })
                            .collect(Collectors.toList());

                    return OrgChartDTO.DepartmentDTO.builder()
                            .id(d.getId())
                            .name(d.getDepartmentName())
                            .members(members.stream()
                                    .map(m -> OrgChartDTO.MemberDTO.builder()
                                            .name(m.getName())
                                            .title(posMap.getOrDefault(m.getPosition(), ""))
                                            .build())
                                    .collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList());

        return OrgChartDTO.DivisionDTO.builder()
                .id(stringId)
                .name(divName)
                .headName(headName)
                .headTitle(headTitle)
                .theme(theme)
                .departments(departments)
                .build();
    }
}
