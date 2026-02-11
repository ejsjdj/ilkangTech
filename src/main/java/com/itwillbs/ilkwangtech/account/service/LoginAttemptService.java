package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.LoginAttemptDTO;
import com.itwillbs.ilkwangtech.account.entity.LoginAttempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

public interface LoginAttemptService {

    LoginAttempt getLoginAttempt(Long id);

    boolean unlock(Long id);

    Page<LoginAttemptDTO> getList(Integer page, Integer size, String sortBy, Sort.Direction direction);
}
