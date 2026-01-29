package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.dto.AppointmentDTO;
import com.itwillbs.ilkwangtech.Hr.entity.AppointmentEntity;
import com.itwillbs.ilkwangtech.Hr.repository.AppointmentRepostiory;
import com.itwillbs.ilkwangtech.account.entity.Department;
import com.itwillbs.ilkwangtech.account.entity.Position;
import com.itwillbs.ilkwangtech.account.repository.DepartmentRepository;
import com.itwillbs.ilkwangtech.account.repository.PositionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AppointmentService {

    private final AppointmentRepostiory appointmentRepostiory;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;

    // 발령 리스트 조회
    public List<AppointmentDTO> hrAppointmentService(){
        List<AppointmentEntity> appointmentEntities = appointmentRepostiory.findAll();

        // 1. 마스터 정보 Map 생성 (루프 밖에서 한 번만 실행)
        Map<Integer, String> deptMap = departmentRepository.findAll().stream()
                .collect(Collectors.toMap(Department::getId, Department::getDepartmentName, (e, r) -> e));

        Map<Integer, String> positionMap = positionRepository.findAll().stream()
                .collect(Collectors.toMap(Position::getId, Position::getPositionName, (e, r) -> e));

        // 2. 리스트를 돌면서 각 엔티티의 코드를 이름으로 변환
        return appointmentEntities.stream().map(entity -> {
            // 각 행(entity)마다 가지고 있는 코드값을 여기서 꺼내야 합니다.
            String preDeptName = deptMap.getOrDefault(entity.getPreDept(), "부서 미지정");
            String currDeptName = deptMap.getOrDefault(entity.getCurrentDept(), "부서 미지정");
            String preRankName = positionMap.getOrDefault(entity.getPreRank(), "직급 미지정");
            String currRankName = positionMap.getOrDefault(entity.getCurrentRank(), "직급 미지정");

            return AppointmentDTO.builder()
                    .appointmentId(entity.getAppointmentId())
                    .memberId(entity.getMemberId().getName())
                    .approverId(entity.getApproverId() != null ? entity.getApproverId().getName() : "승인 대기")
                    .preDept(preDeptName)
                    .currentDept(currDeptName)
                    .preRank(preRankName)
                    .currentRank(currRankName)
                    .workStatus(entity.getWorkStatus())
                    .appointmentDate(entity.getAppointmentDate())
                    .build();
        }).toList();
    }
}
