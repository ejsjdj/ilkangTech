package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.hr.dto.AppointmentDTO;
import com.itwillbs.ilkwangtech.hr.entity.AppointmentEntity;
import com.itwillbs.ilkwangtech.hr.repository.AppointmentRepostiory;
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
                        currentDept(appointmentEntity.getCurrentDept()).
                        preRank(appointmentEntity.getPreRank()).
                        currentRank(appointmentEntity.getCurrentRank()).
                        workStatus(appointmentEntity.getWorkStatus()).
                        appointmentDate(appointmentEntity.getAppointmentDate()).
                        build()).toList();
    }
}
