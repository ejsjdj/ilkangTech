package com.itwillbs.ilkwangtech.account.repository;

import com.itwillbs.ilkwangtech.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 사원(Member) 엔티티에 대한 데이터 액세스를 담당하는 리포지토리
 */
@Repository
public interface AccountRepository extends JpaRepository<Member, Long> {

	/**
	 * 사원번호를 기준으로 사원 정보와 해당 사원의 권한 목록을 함께 조회합니다.
	 * JOIN FETCH를 사용하여 지연 로딩(LAZY) 문제를 해결하고 한 번의 쿼리로 연관 데이터를 가져옵니다.
	 *
	 * @param employeeNumber 조회할 사원번호
	 * @return 사원 정보 (Optional)
	 */
	@Query("SELECT m FROM Member m"
			+ " LEFT JOIN FETCH m.roles r"
			+ " LEFT JOIN FETCH r.role"
			+ " WHERE m.employeeNumber = :employeeNumber")
	Optional<Member> findByEmployeeNumberWithMemberRoles(@Param("employeeNumber") String employeeNumber);

	/**
	 * 이메일 중복 여부를 확인합니다.
	 */
	boolean existsByEmail(String email);

	/**
	 * 전화번호 중복 여부를 확인합니다.
	 */
	boolean existsByPhoneNumber(String phoneNumber);

	/**
	 * 주민등록번호 중복 여부를 확인합니다.
	 */
	boolean existsByResidentNumber(String residentNumber);

	/**
	 * 계좌번호 중복 여부를 확인합니다.
	 */
	boolean existsByAccountNumber(String accountNumber);

	/**
	 * ID로 사원 정보를 조회합니다.
	 */
	Optional<Member> getMemberById(Long memberId);

	/**
	 * 사원번호로 사원 정보를 조회합니다.
	 */
	Optional<Member> getMemberByEmployeeNumber(String employeeNumber);

	/**
	 * 현재 등록된 사원번호 중 가장 높은 순번을 조회합니다.
	 * 새로운 사원번호 생성 시 기반 데이터로 사용됩니다.
	 * 사원번호 형식(예: 26-10000)에서 하이픈 뒤의 숫자를 추출하여 비교합니다.
	 *
	 * @return 최대 사원 순번 (데이터가 없을 경우 기본값 10000)
	 */
	@Query(value = "SELECT COALESCE(MAX(TO_NUMBER(SUBSTR(employee_number, INSTR(employee_number, '-') + 1))), 10000) FROM members", nativeQuery = true)
	int findMaxEmployeeIdx();

}
