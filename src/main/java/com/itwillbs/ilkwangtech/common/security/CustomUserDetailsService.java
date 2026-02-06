package com.itwillbs.ilkwangtech.common.security;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.account.entity.Bank;
import com.itwillbs.ilkwangtech.account.entity.Department;
import com.itwillbs.ilkwangtech.account.entity.Position;
import com.itwillbs.ilkwangtech.account.repository.AccountRepository;
import com.itwillbs.ilkwangtech.account.repository.BankRepository;
import com.itwillbs.ilkwangtech.account.repository.DepartmentRepository;
import com.itwillbs.ilkwangtech.account.repository.PositionRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    // DB 를 참조하기 위한 accountRepository 선언
    private final AccountRepository accountRepository;

    // 부서, 직급, 은행 관련 주입
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final BankRepository bankRepository;

    @Override
    public UserDetails loadUserByUsername(String employeeNumber) throws UsernameNotFoundException {

        // 회원 정보 조회
        Member member = accountRepository
                .findByEmployeeNumberWithMemberRoles(employeeNumber)
                .orElseThrow(() -> new UsernameNotFoundException(employeeNumber + " : + 사용자 조회 실패!"));

        // 부서, 직급, 은행 이름 조회
        String departmentName = getDepartmentName(member.getDepartment());
        String positionName = getPositionName(member.getPosition());
        String bankName = getBankName(member.getBank());

        // 빌더를 이용해서 login 객체를 생성
        return AccountLogin.of(member, departmentName, positionName, bankName);
    }

    private String getDepartmentName(Integer id) {
        if (id == null) return null;
        Department dept = departmentRepository.findById(id).orElse(null);

        if (dept == null) return null;
        return dept.getDepartmentName();
    }

    private String getPositionName(Integer id) {
        if (id == null) return null;
        Position pos = positionRepository.findById(id).orElse(null);
        if (pos == null) return null;
        return pos.getPositionName();
    }

    private String getBankName(Integer id) {
        if (id == null) return null;
        Bank bank = bankRepository.findById(id).orElse(null);
        if (bank == null) return null;
        return bank.getBankName();
    }

}
