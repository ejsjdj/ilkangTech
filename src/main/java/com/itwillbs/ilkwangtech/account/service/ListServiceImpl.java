package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.AccountDetail;
import com.itwillbs.ilkwangtech.account.dto.AccountDetailResponse;
import com.itwillbs.ilkwangtech.account.repository.*;
import com.itwillbs.ilkwangtech.common.exception.MemberNotFoundException;
import com.itwillbs.ilkwangtech.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * ListService 인터페이스의 구현체
 * 사원 목록 조회, 검색, 상세 정보 조회를 처리합니다.
 */
@Service
public class ListServiceImpl implements ListService {

    private final ListRepository listRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final AccountRepository accountRepository;
    private final BankRepository bankRepository;


    public ListServiceImpl(ListRepository listRepository, BankRepository bankRepository, DepartmentRepository departmentRepository, PositionRepository positionRepository, AccountRepository accountRepository) {
        this.listRepository = listRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
    }

    /**
     * 키워드를 기반으로 사원 목록을 검색합니다. (사번, 이름, 연락처, 이메일)
     *
     * @param keyword 검색어
     * @param pageable 페이징 정보
     * @return 검색된 사원 목록 Page (AccountDetail DTO)
     */
    @Override
    public Page<AccountDetail> searchAccountList(String keyword, Pageable pageable) {
        return memberToAccountDetail(listRepository.findBySearchKeyword(keyword, pageable));
    }

    /**
     * 전체 사원 목록을 조회합니다.
     *
     * @param pageable 페이징 정보
     * @return 전체 사원 목록 Page (AccountDetail DTO)
     */
    public Page<AccountDetail> getAccountList(Pageable pageable) {
        return memberToAccountDetail(listRepository.findAll(pageable));
    }

    /**
     * 사원의 상세 정보를 조회합니다.
     * 부서, 직급, 은행 ID를 해당 명칭으로 변환하여 반환합니다.
     *
     * @param id 사원 고유 ID
     * @return 사원 상세 정보 응답 DTO
     */
    @Override
    public AccountDetailResponse getAccountDetail(Long id) {
        // 1. 회원 기본 정보 조회
        Member member = accountRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다. ID: " + id));

        // 2. 부서, 직급, 은행 ID를 명칭으로 변환
        String deptName = member.getDepartment() != null ?
                departmentRepository.findById(member.getDepartment()).map(d -> d.getDepartmentName()).orElse(null) : null;
        String posName = member.getPosition() != null ?
                positionRepository.findById(member.getPosition()).map(p -> p.getPositionName()).orElse(null) : null;
        String bankName = member.getBank() != null ?
                bankRepository.findById(member.getBank()).map(b -> b.getBankName()).orElse(null) : null;

        // 3. Response DTO 구성 및 반환
        return AccountDetailResponse.builder()
                .id(member.getId())
                .employeeNumber(member.getEmployeeNumber())
                .name(member.getName())
                .gender(member.getGender())
                .hireDate(member.getHireDate())
                .residentNumber(member.getResidentNumber())
                .email(member.getEmail())
                .phoneNumber(member.getPhoneNumber())
                .department(deptName)
                .position(posName)
                .bank(bankName)
                .accountNumber(member.getAccountNumber())
                .profilePhotoLink(member.getProfilePhotoLink())
                .roles(member.getRoles().stream()
                        .map(role -> role.getRole().getCommonCodeName())
                        .collect(Collectors.toList()))
                .status("재직") // 상태 관리 로직 미구현으로 임시 "재직" 처리
                .build();
    }

    /**
     * Member 엔티티 Page를 AccountDetail DTO Page로 변환하는 헬퍼 메서드
     * 반복적인 부서/직급 조회를 피하기 위해 맵(Map)을 생성하여 활용합니다.
     *
     * @param members 변환할 Member 엔티티 Page
     * @return 변환된 AccountDetail DTO Page
     */
    private Page<AccountDetail> memberToAccountDetail(Page<Member> members) {

        HashMap<Integer, String> deptMap = createDepartmentMap();
        HashMap<Integer, String> posMap = createPositionMap();

        return members.map(member ->
                new AccountDetail(
                        member.getId(),
                        member.getEmployeeNumber(),
                        member.getName(),
                        deptMap.get(member.getDepartment()),
                        posMap.get(member.getPosition()),
                        null, // status (추후 구현 예정)
                        member.getPhoneNumber(),
                        member.getEmail()
                )
        );
    }

    /**
     * 모든 부서 정보를 ID와 명칭의 Map 형태로 반환합니다.
     */
    private HashMap<Integer, String> createDepartmentMap() {
        HashMap<Integer, String> map = new HashMap<>();
        departmentRepository.findAll().forEach(dept -> map.put(dept.getId(), dept.getDepartmentName()));
        return map;
    }

    /**
     * 모든 직급 정보를 ID와 명칭의 Map 형태로 반환합니다.
     */
    private HashMap<Integer, String> createPositionMap() {
        HashMap<Integer, String> map = new HashMap<>();
        positionRepository.findAll().forEach(pos -> map.put(pos.getId(), pos.getPositionName()));
        return map;
    }

}