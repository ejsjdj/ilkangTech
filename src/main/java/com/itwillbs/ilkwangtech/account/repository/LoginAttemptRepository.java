package com.itwillbs.ilkwangtech.account.repository;

import com.itwillbs.ilkwangtech.account.dto.LoginAttemptDTO;
import com.itwillbs.ilkwangtech.account.entity.LoginAttempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    Optional<LoginAttempt> findByMemberId(Long memberId);

    boolean deleteByMemberId(Long id);

    @Query(value = """
        select
            a.id as loginAttemptId,
            m.id as memberId,
            m.employee_number as employeeNumber,
            d.department_name as department,
            p.position_name as position,
            m.name as name,
            m.phone_number as phoneNumber,
            m.email as email,
            a.lock_Time as lockDateTime
        from login_attempts a
        join members m
        on a.MEMBER_ID = M.ID
        join departments d
        on d.id = m.department
        join positions p
        on p.id = m.position
        """,nativeQuery = true)
    Page<LoginAttemptDTO> findAttemptLongingList(Pageable pageable);
}
