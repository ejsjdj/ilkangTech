package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.account.entity.Department;
import com.itwillbs.ilkwangtech.account.entity.Position;
import com.itwillbs.ilkwangtech.account.repository.DepartmentRepository;
import com.itwillbs.ilkwangtech.account.repository.PositionRepository;
import com.itwillbs.ilkwangtech.hr.dto.AppointmentDTO;
import com.itwillbs.ilkwangtech.hr.entity.AppointmentEntity;
import com.itwillbs.ilkwangtech.hr.repository.AppointmentRepostiory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<AppointmentDTO> hrAppointmentService(Pageable pageable, String keyword){

        // 1. 마스터 정보 Map 생성 (루프 밖에서 한 번만 실행)
        Map<Integer, String> deptMap = departmentRepository.findAll().stream()
                .collect(Collectors.toMap(Department::getId, Department::getDepartmentName, (e, r) -> e));

        Map<Integer, String> positionMap = positionRepository.findAll().stream()
                .collect(Collectors.toMap(Position::getId, Position::getPositionName, (e, r) -> e));


        Page<AppointmentEntity> appointmentEntities = appointmentRepostiory.findAllBySearch(pageable, keyword);


        // 2. 리스트를 돌면서 각 엔티티의 코드를 이름으로 변환
        return appointmentEntities.map(appointmentEntity -> {
            String preDeptName = deptMap.getOrDefault(appointmentEntity.getPreDept(), "부서 미지정");
            String currDeptName = deptMap.getOrDefault(appointmentEntity.getCurrentDept(), "부서 미지정");
            String preRankName = positionMap.getOrDefault(appointmentEntity.getPreRank(), "직급 미지정");
            String currRankName = positionMap.getOrDefault(appointmentEntity.getCurrentRank(), "직급 미지정");

            return AppointmentDTO.builder()
                    .appointmentId(appointmentEntity.getAppointmentId())
                    .memberId(appointmentEntity.getMemberId().getName())
                    .preDept(preDeptName)
                    .currentDept(currDeptName)
                    .preRank(preRankName)
                    .currentRank(currRankName)
                    .workStatus(appointmentEntity.getWorkStatus())
                    .appointmentDate(appointmentEntity.getAppointmentDate())
                    .build();
        });
    }
}
