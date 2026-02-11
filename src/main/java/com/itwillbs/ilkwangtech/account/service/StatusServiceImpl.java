package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.constant.MemberStatus;
import com.itwillbs.ilkwangtech.account.repository.AccountRepository;
import com.itwillbs.ilkwangtech.common.exception.MemberNotFoundException;
import com.itwillbs.ilkwangtech.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final AccountRepository accountRepository;


    @Override
    @Transactional // 중요: 데이터 변경을 위해 트랜잭션 선언
    public void updateStatus(Long id, String statusDescription) {

        // 1. 사용자 존재 여부 확인 및 조회
        Member member = accountRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다. ID: " + id));

        // 2. 입력받은 문자열("재직", "퇴사" 등)을 Enum으로 변환
        MemberStatus newStatus = MemberStatus.fromDescription(statusDescription);

        // 3. 엔티티의 상태 값 변경 (Dirty Checking 활용)
        member.setStatus(newStatus);

        // 4. 저장 (Transactional이 있으면 사실 save 호출 없이도 메서드 종료 시 자동 반영됩니다.)
        accountRepository.save(member);
    }
}
