package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.dto.LeaveDTO;
import com.itwillbs.ilkwangtech.Hr.repository.LeaveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveService {

    private final LeaveRepository leaveRepository;

    @Transactional
    public List<LeaveDTO> getLeaveStatus(Long userId){

        return leaveRepository.findByMemberId(userId).stream()
                .map(leaveEntity -> LeaveDTO.builder()
                        .totalLeave(leaveEntity.getTotalLeave())
                        .usedLeave(leaveEntity.getUsedLeave())
                        .remainLeave(leaveEntity.getRemainLeave())
                        .build())
                .toList();
    }
}
