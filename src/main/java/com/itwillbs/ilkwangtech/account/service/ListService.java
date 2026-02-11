package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.AccountDetailResponse;
import com.itwillbs.ilkwangtech.account.dto.AccountForList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ListService {

    Page<AccountForList> getAccountList(Pageable pageable);

    Page<AccountForList> searchAccountList(String keyword, Pageable pageable);

    AccountDetailResponse getAccountDetail(Long id);

}
