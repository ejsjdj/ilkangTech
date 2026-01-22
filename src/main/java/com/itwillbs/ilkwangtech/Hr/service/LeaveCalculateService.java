package com.itwillbs.ilkwangtech.Hr.service;

import com.itwillbs.ilkwangtech.Hr.entity.LeaveEntity;
import com.itwillbs.ilkwangtech.Hr.repository.LeaveRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// TODO ::: 휴가 결재 승인 로직에 추가할 것 !!!!!!

@Service
@RequiredArgsConstructor
public class LeaveCalculateService {

    private final LeaveRepository leaveRepository;

    public void getCalculate(long userId){

        // 전체 연차와 사용 연차 조회
        LeaveEntity leaveEntities = leaveRepository.findByMemberId(userId).
                orElseThrow(() -> new EntityNotFoundException("연차 정보를 확인할 수 없습니다."));

        // 전체 연차 - 사용 연차 계산
        leaveEntities.calculateRemainLeave();

    }
}
