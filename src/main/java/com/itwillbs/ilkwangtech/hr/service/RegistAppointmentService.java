package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.hr.entity.AppointmentEntity;
import com.itwillbs.ilkwangtech.hr.repository.AppointmentRepostiory;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


// 발령 등록
@Service
@AllArgsConstructor
public class RegistAppointmentService {

    private final AppointmentRepostiory appointmentRepostiory;
    private final MemberRepository memberRepository;

    @Transactional
    public void registAppointment(Long approverId, Long userId, Integer newDept, Integer newRank, String workStatus){

        Member member = memberRepository.findById(userId).orElseThrow(); // Member 테이블에서 발령 대상인 사원 찾기

        Member approver = memberRepository.findById(approverId).orElseThrow();

        AppointmentEntity appointmentEntity = new AppointmentEntity(member);

        // 1. 부서 등록
        if (newDept != null){

            appointmentEntity.changeDept(member.getDepartment());

            appointmentEntity.newDept(newDept);

            member.setDepartment(newDept);

        } else {

            appointmentEntity.newDept(member.getDepartment());

            appointmentEntity.changeDept(member.getDepartment());
        }

        // 2. 직급 등록
        if (newRank != null) {

            appointmentEntity.changeRank(member.getPosition());

            appointmentEntity.newRank(newRank);

            member.setPosition(newRank);

        } else {

            appointmentEntity.newRank(member.getPosition());

            appointmentEntity.changeRank(member.getPosition());

        }

        // 3. 근무상태 등록
        if (workStatus != null && !workStatus.isEmpty()) {

            appointmentEntity.changeStatus(workStatus);

        }

        // 4. 승인날짜 등록
        appointmentEntity.newDate(LocalDate.now());

        // 5. 승인자 등록
        appointmentEntity.newApprover(approver);

        appointmentRepostiory.save(appointmentEntity);

        System.out.println("발령 등록 완료!");
    }
}