package com.itwillbs.ilkwangtech.messenger.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.itwillbs.ilkwangtech.member.entity.Member;

@Repository
public interface MessengerRepository extends JpaRepository<Member, Long> {

	// 사원 리스트 출력
	@Query(value = """
		    select m.name as memberName,
		           d.department_name as departmentName
		    from members m
		    join departments d
		      on m.department = d.id
		    order by m.name
		""", nativeQuery = true)
		List<Object[]> findMemberDeptRows();
	
}
