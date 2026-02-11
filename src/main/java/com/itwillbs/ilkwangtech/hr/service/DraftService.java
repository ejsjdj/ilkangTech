package com.itwillbs.ilkwangtech.hr.service;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.hr.dto.DraftDTO;
import com.itwillbs.ilkwangtech.hr.repository.DraftApproveStatusRepository;
import com.itwillbs.ilkwangtech.hr.repository.DraftRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Log4j2
public class DraftService {

    private final DraftRepository draftRepository;
    private final DraftApproveStatusRepository draftApproveStatusRepository;
    private final MemberRepository memberRepository;

    public DraftService(DraftRepository draftRepository, DraftApproveStatusRepository draftApproveStatusRepository, MemberRepository memberRepository) {
        this.draftRepository = draftRepository;
        this.draftApproveStatusRepository = draftApproveStatusRepository;
        this.memberRepository = memberRepository;
    }

    // 내가 작성한 문서 OR 내가 결재자인 문서 조회
    @Transactional
    public Page<DraftDTO> getDraftById(AccountLogin loginUser, Long userId, Pageable pageable, String docType, String draftType, LocalDate startDate, LocalDate endDate) {

        // 2. employeeNumber → Member 조회
        Member member = memberRepository.findByEmployeeNumber(
                loginUser.getEmployeeNumber()
        ).orElseThrow(() -> new IllegalStateException(
                "로그인 사용자에 해당하는 Member가 존재하지 않습니다."
        ));

        Long memberId = member.getId();

        log.info("resolved member.id = {}", memberId);

        if ("inbox".equals(docType)) { // 3. 결재할 문서
            return draftRepository.findReceivedDrafts(
                    memberId, draftType, startDate, endDate, pageable
            );
        } else { // 4. 내가 올린 문서
            return draftRepository.findSentDrafts(
                    memberId, draftType, startDate, endDate, pageable
            );
        }
    }
}
