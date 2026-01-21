package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.AccountDetail;
import com.itwillbs.ilkwangtech.account.repository.*;
import com.itwillbs.ilkwangtech.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ListServiceImpl implements ListService {

    private final ListRepository listRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final AccountRepository accountRepository;


    public ListServiceImpl(ListRepository listRepository, BankRepository bankRepository, DepartmentRepository departmentRepository, PositionRepository positionRepository, AccountRepository accountRepository) {
        this.listRepository = listRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public Page<AccountDetail> searchAccountList(String keyword, Pageable pageable) {
        return memberToAccountDetail(listRepository.findBySearchKeyword(keyword, pageable));
    }

    public Page<AccountDetail> getAccountList(Pageable pageable) {
        return memberToAccountDetail(listRepository.findAll(pageable));
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