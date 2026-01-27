package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.CommonCode;
import com.itwillbs.ilkwangtech.account.dto.MemberRoleView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {

    List<CommonCode> getRoles();

    Page<MemberRoleView> findMemberByRole(long roleId, Pageable pageable);

}
