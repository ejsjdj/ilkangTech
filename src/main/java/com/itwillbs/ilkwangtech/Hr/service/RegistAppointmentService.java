package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.repository.AppointmentRepostiory;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


// 발령 등록
@Service
@AllArgsConstructor
public class RegistAppointmentService {

    private final AppointmentRepostiory appointmentRepostiory;

    @Transactional
    public void registAppointment(Long approverId, Long userId, Long newDept, Long newRank, String workStatus){
        appointmentRepostiory.findAll();
    }


}
