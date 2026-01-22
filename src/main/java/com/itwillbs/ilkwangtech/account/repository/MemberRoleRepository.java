package com.itwillbs.ilkwangtech.account.repository;

import com.itwillbs.ilkwangtech.account.dto.MemberRoleView;
import com.itwillbs.ilkwangtech.common.entity.CommonCode;
import com.itwillbs.ilkwangtech.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRoleRepository extends JpaRepository<CommonCode, Long> {

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
        join departments d on d.id = m.department
        join positions p on p.id = m.position
        where c.id = :roleId
        """,
        nativeQuery = true)
    List<MemberRoleView> findMembersByRoleId(@Param("roleId") Long roleId);

}
