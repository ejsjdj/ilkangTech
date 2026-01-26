package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.AccountRegisterRequest;
import com.itwillbs.ilkwangtech.account.dto.AccountRegisterResponse;
import com.itwillbs.ilkwangtech.account.entity.LoginAttempt;

// 컨트롤러에서는 사용자가 요청을 하면 그 요청에 맞는 함수를 AccountService 에서 호출을 한다.
// AccountService 에서는 컨트롤러가 받은 요청을 처리를 할때
// 세션에 관련된 것들은 helper 패키지에 SessionAccountHelper 를 호출해 처리한다.
// 암호화와 관련된 작업은 util 패키지에 EncrptionUtils 를 호출해 처리한다.
// 그 외에 기타사항은 자체적으로 처리한다.
// 최종적으로 DB 에 CRUD 기능은 repository 를 이용한다.
public interface AccountService {

    AccountRegisterResponse register(AccountRegisterRequest request);

    boolean updateMyInfo(Long id, String email, String phoneNumber);

}
