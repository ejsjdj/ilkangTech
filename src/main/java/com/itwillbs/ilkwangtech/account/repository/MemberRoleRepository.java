package com.itwillbs.ilkwangtech.account.repository;

import com.itwillbs.ilkwangtech.member.entity.MemberRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * 사원 권한(MemberRole) 엔티티에 대한 데이터 액세스를 담당하는 리포지토리
 */
@Repository
public interface MemberRoleRepository extends JpaRepository<MemberRole, Long> {

    /**
     * 특정 사원과 권한 ID를 기준으로 사원 권한 매핑 정보를 삭제합니다.
     * (권한 회수 기능)
     *
     * @param memberId 사원 고유 ID
     * @param roleId 권한 고유 ID
     */
    @Modifying
    @Query("DELETE FROM MemberRole mr WHERE mr.member.id = :memberId AND mr.role.id = :roleId")
    void deleteByMemberIdAndRoleId(@Param("memberId") Long memberId, @Param("roleId") Long roleId);

    /**
     * 특정 사원의 모든 권한 정보를 삭제합니다.
     *
     * @param memberId 사원 고유 ID
     */
    void deleteAllByMemberId(Long memberId);

    /**
     * 특정 권한 ID를 가진 사원들의 목록을 조회합니다.
     * 사원 정보, 부서명, 직급명, 권한명을 포함하여 Native Query로 작성되었습니다.
     *
     * @param roleId 조회할 권한 ID
     * @param pageable 페이징 및 정렬 정보
     * @return MemberRoleView DTO Page
     */
    @Query(value = """
        select 
                m.id as memberId, 
                r.id as roleId,
                m.name as name, 
                m.employee_number as employeeNumber,
                d.department_name as department, 
                p.position_name as position, 
                c.common_code_name as description
        from members m
        join member_role r on m.id = r.member_id
        join common_code c on c.id = r.member_role_id
        left join departments d on d.id = m.department
        left join positions p on p.id = m.position
        where r.member_role_id = :roleId
        """,
        countQuery = """
        select count(*)
        from member_role r
        where r.member_role_id = :roleId
        """,
        nativeQuery = true)
    Page<java.util.Map<String, Object>> findMembersByRoleId(@Param("roleId") Long roleId, Pageable pageable);

}
