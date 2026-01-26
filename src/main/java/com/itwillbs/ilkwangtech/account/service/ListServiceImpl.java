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

    @Override
    public Page<AccountDetail> searchAccountList(String keyword, Pageable pageable) {
        return memberToAccountDetail(listRepository.findBySearchKeyword(keyword, pageable));
    }

    public Page<AccountDetail> getAccountList(Pageable pageable) {
        return memberToAccountDetail(listRepository.findAll(pageable));
    }

    @Override
    public AccountDetailResponse getAccountDetail(Long id) {
        Member member = accountRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다. ID: " + id));

        String deptName = member.getDepartment() != null ?
                departmentRepository.findById(member.getDepartment()).map(d -> d.getDepartmentName()).orElse(null) : null;
        String posName = member.getPosition() != null ?
                positionRepository.findById(member.getPosition()).map(p -> p.getPositionName()).orElse(null) : null;
        String bankName = member.getBank() != null ?
                bankRepository.findById(member.getBank()).map(b -> b.getBankName()).orElse(null) : null;

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
                .status("재직") // 임시
                .build();
    }

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
                        null, // status (추후 구현)
                        member.getPhoneNumber(),
                        member.getEmail()
                )
        );
    }

    private HashMap<Integer, String> createDepartmentMap() {
        HashMap<Integer, String> map = new HashMap<>();
        departmentRepository.findAll().forEach(dept -> map.put(dept.getId(), dept.getDepartmentName()));
        return map;
    }

    private HashMap<Integer, String> createPositionMap() {
        HashMap<Integer, String> map = new HashMap<>();
        positionRepository.findAll().forEach(pos -> map.put(pos.getId(), pos.getPositionName()));
        return map;
    }

}