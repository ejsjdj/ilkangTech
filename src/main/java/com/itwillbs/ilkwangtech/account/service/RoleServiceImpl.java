package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.CommonCode;
import com.itwillbs.ilkwangtech.account.dto.MemberRoleView;
import com.itwillbs.ilkwangtech.account.repository.CommonCodeRepository;
import com.itwillbs.ilkwangtech.account.repository.MemberRoleRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.entity.MemberRole;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final MemberRoleRepository memberRoleRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(MemberRoleRepository memberRoleRepository, CommonCodeRepository commonCodeRepository, ModelMapper modelMapper) {
        this.memberRoleRepository = memberRoleRepository;
        this.commonCodeRepository = commonCodeRepository;
        this.modelMapper = modelMapper;
    }

    public List<CommonCode> getRoles() {

        return memberRoleRepository
                .findAll()
                .stream()
                .map(role -> modelMapper.map(role, CommonCode.class))
                .collect(Collectors.toList());
    }

    public List<MemberRoleView> findMemberByRole(long roleId) {
        List<MemberRoleView> memberRoles = memberRoleRepository.findMembersByRoleId(roleId);
        return memberRoles;
    }

}
