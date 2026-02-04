package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.CommonCode;
import com.itwillbs.ilkwangtech.account.dto.MemberRoleView;
import com.itwillbs.ilkwangtech.account.dto.MemberSummary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 사원 권한 관리 비즈니스 로직을 정의한 서비스 인터페이스
 */
public interface RoleService {

    /**
     * 시스템에 정의된 모든 권한(MEMBER_ROLE 그룹코드) 목록을 조회합니다.
     *
     * @return 권한 정보 목록
     */
    List<CommonCode> getRoles();

    /**
     * 특정 권한을 가진 사원 목록을 페이징하여 조회합니다.
     *
     * @param roleId 조회할 권한 ID
     * @param pageable 페이징 및 정렬 정보
     * @return 권한을 가진 사원 목록 Page
     */
    Page<MemberRoleView> findMemberByRole(long roleId, Pageable pageable);
    
    /**
     * 사원에게서 특정 권한을 회수(삭제)합니다.
     *
     * @param memberId 사원 고유 ID
     * @param roleId 회수할 권한 ID
     */
    void revokeRole(Long memberId, Long roleId);

    /**
     * 사원에게 새로운 권한을 부여합니다.
     *
     * @param memberId 사원 고유 ID
     * @param roleId 부여할 권한 ID
     */
    void grantRole(Long memberId, Long roleId);

    /**
     * 권한을 부여할 수 있는 전체 사원 목록을 조회합니다.
     *
     * @return 전체 사원 DTO 목록
     */
    List<MemberSummary> getAssignableMembers();

    /**
     * 사원의 부서 변경에 따른 권한 재부여
     * @param memberId 사원 고유 ID
     * @param departmentId 부여할 권한 ID
     */
    public void regrantRolebyAppointment(Long memberId, Long departmentId);

}
