package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.AccountDetail;
import com.itwillbs.ilkwangtech.account.repository.BankRepository;
import com.itwillbs.ilkwangtech.account.repository.DepartmentRepository;
import com.itwillbs.ilkwangtech.account.repository.ListRepository;
import com.itwillbs.ilkwangtech.account.repository.PositionRepository;
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

    public ListServiceImpl(ListRepository listRepository, BankRepository bankRepository, DepartmentRepository departmentRepository, PositionRepository positionRepository) {
        this.listRepository = listRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
    }

    public Page<AccountDetail> getAccountList(Pageable pageable) {
        Page<Member> memberPage = listRepository.findAll(pageable);

        HashMap<Integer, String> deptMap = createDepartmentMap();
        HashMap<Integer, String> posMap = createPositionMap();

        Page<AccountDetail> accountDetailPage = memberPage.map(member ->
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

        return accountDetailPage;
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