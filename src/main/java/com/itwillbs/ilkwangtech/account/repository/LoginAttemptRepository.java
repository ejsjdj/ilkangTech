package com.itwillbs.ilkwangtech.account.repository;

import com.itwillbs.ilkwangtech.account.dto.LoginAttemptDTO;
import com.itwillbs.ilkwangtech.account.entity.LoginAttempt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {

    Optional<LoginAttempt> findByMemberId(Long memberId);

    boolean deleteByMemberId(Long id);

    @Query(value = """
        select new com.itwillbs.ilkwangtech.account.dto.LoginAttemptDTO(
            a.id,
            m.id,
            m.employeeNumber,
            d.departmentName,
            p.positionName,
            m.name,
            m.phoneNumber,
            m.email,
            a.lockTime
        )
        from LoginAttempt a
        join a.member m
        left join Department d on d.id = m.department
        left join Position p on p.id = m.position
        where a.accountNonLocked = false
        """,
        countQuery = """
        select count(a)
        from LoginAttempt a
        where a.accountNonLocked = false
        """)
    Page<LoginAttemptDTO> findAttemptLongingList(Pageable pageable);
}
