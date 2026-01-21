package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.AccountDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ListService {

    Page<AccountDetail> getAccountList(Pageable pageable);

    Page<AccountDetail> searchAccountList(String keyword, Pageable pageable);

}
