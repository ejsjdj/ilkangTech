package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.AccountDetailResponse;
import com.itwillbs.ilkwangtech.account.dto.AccountForList;
import com.itwillbs.ilkwangtech.account.entity.ProfileImg;
import com.itwillbs.ilkwangtech.account.repository.AccountRepository;
import com.itwillbs.ilkwangtech.account.repository.ListRepository;
import com.itwillbs.ilkwangtech.common.exception.MemberNotFoundException;
import com.itwillbs.ilkwangtech.common.service.CommonCodeService;
import com.itwillbs.ilkwangtech.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * ListService 인터페이스의 구현체
 * 사원 목록 조회, 검색, 상세 정보 조회를 처리합니다.
 */
@Service
@RequiredArgsConstructor
public class ListServiceImpl implements ListService {

    private final ListRepository listRepository;
    private final AccountRepository accountRepository;
    private final CommonCodeService commonCodeService;

    @Value("${file.uploadBaseLocation}")
    private String uploadBaseLocation;

    @Value("${file.profileImgLocation}")
    private String profileImageLocation;

    /**
     * 키워드를 기반으로 사원 목록을 검색합니다. (사번, 이름, 연락처, 이메일)
     *
     * @param keyword 검색어
     * @param pageable 페이징 정보
     * @return 검색된 사원 목록 Page (AccountDetail DTO)
     */
    @Override
    public Page<AccountForList> searchAccountList(String keyword, Pageable pageable) {
        return memberToAccountForList(listRepository.findBySearchKeyword(keyword, pageable));
    }

    /**
     * 전체 사원 목록을 조회합니다.
     *
     * @param pageable 페이징 정보
     * @return 전체 사원 목록 Page (AccountDetail DTO)
     */
    public Page<AccountForList> getAccountList(Pageable pageable) {
        return memberToAccountForList(listRepository.findAll(pageable));
    }

    /**
     * 사원의 상세 정보를 조회합니다.
     * 부서, 직급, 은행 ID를 해당 명칭으로 변환하여 반환합니다.
     *
     * @param id 사원 고유 ID
     * @return 사원 상세 정보 응답 DTO
     */
    @Override
    @Transactional
    public AccountDetailResponse getAccountDetail(Long id) {
        Member member = accountRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다. ID: " + id));

        String deptName = commonCodeService.getDepartmentName(member.getDepartment());
        String posName = commonCodeService.getPositionName(member.getPosition());
        String bankName = commonCodeService.getBankName(member.getBank());
        List<ProfileImg> profileImgs = member.getProfileImgs();

        return AccountDetailResponse.of(member, deptName, posName, bankName, profileImgs);
//        return AccountDetailResponse.of(member, deptName, posName, bankName);
    }

    /**
     * Member 엔티티 Page를 AccountDetail DTO Page로 변환하는 헬퍼 메서드
     * 반복적인 부서/직급 조회를 피하기 위해 맵(Map)을 생성하여 활용합니다.
     *
     * @param members 변환할 Member 엔티티 Page
     * @return 변환된 AccountDetail DTO Page
     */
    private Page<AccountForList> memberToAccountForList(Page<Member> members) {
        return members.map(member -> AccountForList.of(
                member,
                commonCodeService.getDepartmentName(member.getDepartment()),
                commonCodeService.getPositionName(member.getPosition())
        ));
    }

//    // 프로필에 띄울 사진 주소 리턴
//    private String getFirstProfileImgUrl(Member member) {
//
//        if (member.getProfileImgs() == null || member.getProfileImgs().isEmpty()) return null;
//
//        ProfileImg profileImg = member.getProfileImgs().getFirst();
//        String fileName = profileImg.getImgName();
//        String imgLocation = profileImg.getImgLocation();
//
//        return imgLocation + "/" + fileName;
//    }

}