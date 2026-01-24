package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.entity.LoginAttempt;
import com.itwillbs.ilkwangtech.account.repository.AccountRepository;
import com.itwillbs.ilkwangtech.account.repository.LoginAttemptRepository;
import com.itwillbs.ilkwangtech.common.exception.MemberNotFoundException;
import com.itwillbs.ilkwangtech.member.entity.Member;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final LoginAttemptRepository loginAttemptRepository;
    private final AccountRepository accountRepository;

    public LoginAttemptServiceImpl(LoginAttemptRepository loginAttemptRepository, AccountRepository accountRepository) {
        this.loginAttemptRepository = loginAttemptRepository;
        this.accountRepository = accountRepository;
    }

    public LoginAttempt getLoginAttempt(Long memberId) {

        LoginAttempt loginAttempt = loginAttemptRepository.findByMemberId(memberId)
                .orElse(null);

        if (loginAttempt == null) {
            Member member = accountRepository.getMemberById(memberId)
                    .orElseThrow(() -> new MemberNotFoundException());
            loginAttempt = new LoginAttempt(member);
            loginAttemptRepository.save(loginAttempt);
        }

        return loginAttempt;
    }


}
