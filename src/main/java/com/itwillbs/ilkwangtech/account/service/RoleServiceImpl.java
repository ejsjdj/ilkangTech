package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.CommonCodeDTO;
import com.itwillbs.ilkwangtech.account.dto.MemberRoleViewDTO;
import com.itwillbs.ilkwangtech.account.dto.MemberSummaryDTO;
import com.itwillbs.ilkwangtech.account.repository.AccountRepository;
import com.itwillbs.ilkwangtech.account.repository.CommonCodeRepository;
import com.itwillbs.ilkwangtech.account.repository.MemberRoleRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.entity.MemberRole;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * RoleService 인터페이스의 구현체
 * 공통코드 테이블에서 권한 목록을 가져오고, 회원별 권한을 매핑/해제하는 기능을 수행합니다.
 */
@Service
public class RoleServiceImpl implements RoleService {

    private final MemberRoleRepository memberRoleRepository;
    private final CommonCodeRepository commonCodeRepository;
    private final AccountRepository accountRepository;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(MemberRoleRepository memberRoleRepository, CommonCodeRepository commonCodeRepository, AccountRepository accountRepository, ModelMapper modelMapper) {
        this.memberRoleRepository = memberRoleRepository;
        this.commonCodeRepository = commonCodeRepository;
        this.accountRepository = accountRepository;
        this.modelMapper = modelMapper;
    }

    /**
     * 공통코드 테이블에서 'MEMBER_ROLE' 그룹에 속하는 권한 목록을 조회합니다.
     *
     * @return 권한(CommonCode) DTO 리스트
     */
    public List<CommonCodeDTO> getRoles() {

        return commonCodeRepository.findAll()
                .stream()
                .map(role -> new CommonCodeDTO(role.getId(), role.getCommonCodeName()))
                .collect(Collectors.toList());
    }



    /**
     * 특정 권한 ID를 가진 모든 사원 정보를 조회합니다.
     *
     * @param roleId 조회할 권한 고유 ID
     * @param pageable 페이징 정보
     * @return 사원 권한 뷰(MemberRoleView) Page
     */
    public Page<MemberRoleViewDTO> findMemberByRole(long roleId, Pageable pageable) {
        Page<java.util.Map<String, Object>> result = memberRoleRepository.findMembersByRoleId(roleId, pageable);
        
        return result.map(map -> {
            // Oracle 등 DB에 따라 컬럼명이 대문자로 올 수 있으므로 유연하게 처리
            Long memberId = ((Number) (map.get("MEMBERID") != null ? map.get("MEMBERID") : map.get("memberId"))).longValue();
            Long rId = ((Number) (map.get("ROLEID") != null ? map.get("ROLEID") : map.get("roleId"))).longValue();
            String name = (String) (map.get("NAME") != null ? map.get("NAME") : map.get("name"));
            String empNo = (String) (map.get("EMPLOYEENUMBER") != null ? map.get("EMPLOYEENUMBER") : map.get("employeeNumber"));
            String dept = (String) (map.get("DEPARTMENT") != null ? map.get("DEPARTMENT") : map.get("department"));
            String pos = (String) (map.get("POSITION") != null ? map.get("POSITION") : map.get("position"));
            String desc = (String) (map.get("DESCRIPTION") != null ? map.get("DESCRIPTION") : map.get("description"));
            
            return new MemberRoleViewDTO(memberId, rId, name, empNo, dept, pos, desc);
        });
    }

    /**
     * 사원과 권한 간의 매핑 데이터를 삭제하여 권한을 회수합니다.
     *
     * @param memberId 사원 고유 ID
     * @param roleId 회수할 권한 ID
     */
    @Override
    @Transactional
    public void revokeRole(Long memberId, Long roleId) {
        memberRoleRepository.deleteByMemberIdAndRoleId(memberId, roleId);
    }

    @Override
    @Transactional
    public void regrantRolebyAppointment(Long memberId, Long departmentId) {
        memberRoleRepository.deleteAllByMemberId(memberId);
        grantRole(memberId, departmentId);
        grantRole(memberId, 1000L);
    }

    /**
     * 특정 사원에게 새로운 권한을 부여합니다.
     * 이미 동일한 권한을 가지고 있는 경우 중복 부여하지 않습니다.
     *
     * @param memberId 사원 고유 ID
     * @param roleId 부여할 권한 ID
     */
    @Override
    @Transactional
    public void grantRole(Long memberId, Long roleId) {

        // 1. 회원 정보 조회
        Member member = accountRepository.findById(memberId)
                .orElseThrow(() -> new RuntimeException("회원을 찾을 수 없습니다."));
     
        // 2. 부여할 권한(공통코드) 정보 조회
        com.itwillbs.ilkwangtech.common.entity.CommonCode role = commonCodeRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("권한을 찾을 수 없습니다."));

        // 3. 이미 해당 권한이 있는지 확인
        boolean exists = member.getRoles().stream()
                .anyMatch(mr -> mr.getRole().getId().equals(roleId));

        // 4. 권한이 없는 경우에만 새로 저장
        if (!exists) {
            MemberRole memberRole = new MemberRole(member, role);
            memberRoleRepository.save(memberRole);
        }
    }

    /**
     * 권한 부여 대상이 될 수 있는 전체 사원 목록을 조회합니다.
     *
     * @return 전체 사원 DTO 리스트
     */
    @Override
    public List<MemberSummaryDTO> getAssignableMembers() {
        return accountRepository.findAll().stream()
                .map(m -> modelMapper.map(m, MemberSummaryDTO.class))
                .collect(Collectors.toList());
    }

}
