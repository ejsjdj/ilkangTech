package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.LoginAttemptDTO;
import com.itwillbs.ilkwangtech.account.entity.LoginAttempt;
import com.itwillbs.ilkwangtech.account.repository.AccountRepository;
import com.itwillbs.ilkwangtech.account.repository.LoginAttemptRepository;
import com.itwillbs.ilkwangtech.common.exception.MemberNotFoundException;
import com.itwillbs.ilkwangtech.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class LoginAttemptServiceImpl implements LoginAttemptService {

    private final LoginAttemptRepository loginAttemptRepository;
    private final AccountRepository accountRepository;

    public LoginAttemptServiceImpl(LoginAttemptRepository loginAttemptRepository, AccountRepository accountRepository) {
        this.loginAttemptRepository = loginAttemptRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public LoginAttempt getLoginAttempt(Long memberId) {

        LoginAttempt loginAttempt = loginAttemptRepository.findByMemberId(memberId)
                .orElse(null);

        if (loginAttempt == null) {
            Member member = accountRepository.findById(memberId)
                    .orElseThrow(() -> new MemberNotFoundException());
            loginAttempt = new LoginAttempt(member);
            loginAttemptRepository.save(loginAttempt);
        }

        return loginAttempt;
    }

    @Override
    public boolean unlock(Long id) {

        LoginAttempt loginAttempt = loginAttemptRepository.findByMemberId(id).orElse(null);
        if (loginAttempt != null) {
            loginAttemptRepository.delete(loginAttempt);
            return true;
        }
        return false;

    }

    @Override
    public Page<LoginAttemptDTO> getList(Integer page, Integer size, String sortBy, Sort.Direction direction) {

        System.out.println("@@@@@@@@@@@@"+direction);
        System.out.println("@@@@@@@@@@@@"+sortBy);

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction,sortBy));
       // Pageable pageable = PageRequest.of(page - 1, size);
        System.out.println("@@@@@@@@@@@@"+pageable);


        Page<LoginAttemptDTO> loginAttempts = loginAttemptRepository.findAttemptLongingList(pageable);

        return loginAttempts;
    }

}
