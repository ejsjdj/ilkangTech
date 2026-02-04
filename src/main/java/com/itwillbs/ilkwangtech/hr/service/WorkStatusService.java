package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.account.entity.Department;
import com.itwillbs.ilkwangtech.account.entity.Position;
import com.itwillbs.ilkwangtech.account.repository.DepartmentRepository;
import com.itwillbs.ilkwangtech.account.repository.PositionRepository;
import com.itwillbs.ilkwangtech.hr.dto.WorkStatusDTO;
import com.itwillbs.ilkwangtech.hr.entity.Attendance;
import com.itwillbs.ilkwangtech.hr.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkStatusService {

    private final AttendanceRepository attendanceRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    public List<WorkStatusDTO> getWorkStatus(Integer deptCode, LocalDate workDate){

        List<Attendance> workStatus = attendanceRepository.findByDepartmentAndDate(deptCode, workDate);

        // 1. 마스터 정보 Map 생성 (루프 밖에서 한 번만 실행)
        Map<Integer, String> deptMap = departmentRepository.findAll().stream()
                .collect(Collectors.toMap(Department::getId, Department::getDepartmentName, (e, r) -> e));

        Map<Integer, String> positionMap = positionRepository.findAll().stream()
                .collect(Collectors.toMap(Position::getId, Position::getPositionName, (e, r) -> e));

        return workStatus.stream()
                .map(attendance -> {
                    // 멤버 객체를 자주 쓰므로 변수로 추출
                    var member = attendance.getMember();

                    return WorkStatusDTO.builder()
                            .name(member.getName())
                            .department(deptMap.getOrDefault(member.getDepartment(), "소속없음"))
                            .position(positionMap.getOrDefault(member.getPosition(), "직급없음"))
                            .status(attendance.getStatus().getDescription())
                            .build();
                })
                .toList();
    }
}
