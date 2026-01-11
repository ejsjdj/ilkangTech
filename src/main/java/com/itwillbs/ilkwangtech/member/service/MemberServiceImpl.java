package com.itwillbs.ilkwangtech.member.service;

import com.itwillbs.ilkwangtech.account.repository.AccountRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final AccountRepository accountRepository;

}
