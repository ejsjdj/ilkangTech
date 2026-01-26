package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.dto.AppointmentDTO;
import com.itwillbs.ilkwangtech.Hr.entity.AppointmentEntity;
import com.itwillbs.ilkwangtech.Hr.repository.AppointmentRepostiory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AppointmentService {

    private final AppointmentRepostiory appointmentRepostiory;

    // 발령 리스트 조회
    public List<AppointmentDTO> hrAppointmentService(){
        List<AppointmentEntity> appointmentEntities = appointmentRepostiory.findAll();

        return appointmentEntities.stream().map(
                appointmentEntity -> AppointmentDTO.builder().
                        appointmentId(appointmentEntity.getAppointmentId()).
                        memberId(appointmentEntity.getMemberId().getId()).
                        approverId(appointmentEntity.getApproverId() != null ? appointmentEntity.getApproverId().getId() : null).
                        previousDeptId(appointmentEntity.getPreviousDeptId() != null ? appointmentEntity.getPreviousDeptId().getDepartmentName() : null).
                        previoutPositionId(appointmentEntity.getPreviousPositionId() != null ? appointmentEntity.getPreviousPositionId().getPositionName() : null).
                        workStatus(appointmentEntity.getWorkStatus()).
                        appointmentDate(appointmentEntity.getAppointmentDate()).
                        build()).toList();
    }
}
