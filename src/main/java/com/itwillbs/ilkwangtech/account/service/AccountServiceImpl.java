package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.AccountDTO;
import com.itwillbs.ilkwangtech.account.helper.AccountHelper;
import com.itwillbs.ilkwangtech.account.repository.AccountRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

// 컨트롤러에서는 사용자가 요청을 하면 그 요청에 맞는 함수를 AccountService 에서 호출을 한다.
// AccountService 에서는 컨트롤러가 받은 요청을 처리를 할때
// 세션에 관련된 것들은 helper 패키지에 SessionAccountHelper 를 호출해 처리한다.
// 암호화와 관련된 작업은 util 패키지에 encryptionUtils 를 호출해 처리한다.
// 그 외에 기타사항은 자체적으로 처리한다.
// 최종적으로 DB 에 CRUD 기능은 repository 를 이용한다.

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountHelper accountHelper;

    static int idx = 0;

    @Override
    public AccountDTO.AccountJoinResponse create(AccountDTO.AccountJoinRequest req) {

        // 중복검사

        // 암호화

        // 저장
        Member createdMember = Member.builder()
                .name(req.name())
                .email(req.email())
                .accountNumber(req.accountNumber())
                .bank(req.bank())
                .employeeNumber(LocalDateTime.now().getYear() + "-" + (1000 + ++idx))
                .gender(req.gender())
                .department(req.department())
                .password(req.password())
                .phoneNumber(req.phoneNumber())
                .position(req.position())
                .residentNumber(req.residentNumber())
                .build();

        Member member = accountRepository.save(createdMember);

        return null;
    }

    @Override
    public AccountDTO.AccountLoginResponse login(AccountDTO.AccountLoginRequest req) {

        Member member = accountRepository.findByEmployeeNumberAndPassword(req.employeeNumber(), req.passWord());

        AccountDTO.AccountLoginResponse res = new AccountDTO.AccountLoginResponse(
                member.getName(),
                member.getEmployeeNumber(),
                member.getGender(),
                member.getJoinDate(),
                member.getResidentNumber(),
                member.getEmail(),
                member.getPhoneNumber(),
                member.getDepartment(),
                member.getPosition(),
                member.getBank(),
                member.getAccountNumber(),
                member.getLastLogin()
        );

        return res;
    }

    @Override
    public Page<AccountDTO.AccountListResponse> getListPage(Pageable pageable) {
        return accountRepository.findAll(pageable)
                .map(AccountDTO.AccountListResponse::from);
    }


}
