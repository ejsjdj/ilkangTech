package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.CommonCode;
import com.itwillbs.ilkwangtech.account.dto.MemberRoleView;

import java.util.List;

public interface RoleService {

    List<CommonCode> getRoles();

    List<MemberRoleView> findMemberByRole(long roleId);

}
