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
                        approverId(appointmentEntity.getMemberId().getId()).
                        preDept(appointmentEntity.getPreDept()).
                        preRank(appointmentEntity.getPreRank()).
                        workStatus(appointmentEntity.getWorkStatus()).
                        appointmentDate(appointmentEntity.getAppointmentDate()).
                        build()).toList();
    }
}
