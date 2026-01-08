package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.AccountDTO;
import com.itwillbs.ilkwangtech.member.entity.Member;

public interface AccountService {

    // 사원 데이터 저장
    AccountDTO.AccountJoinResponse join(AccountDTO.AccountJoinRequest accountJoinRequest);

}
