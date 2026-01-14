package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.entity.Departments;
import com.itwillbs.ilkwangtech.account.repository.DepartmentRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.itwillbs.ilkwangtech.account.dto.AccountDTO;
import com.itwillbs.ilkwangtech.account.dto.AccountRegisterResponseDTO;
import com.itwillbs.ilkwangtech.account.repository.AccountRepository;
import com.itwillbs.ilkwangtech.member.entity.Member;

import lombok.RequiredArgsConstructor;

import java.util.List;

// 컨트롤러에서는 사용자가 요청을 하면 그 요청에 맞는 함수를 AccountService 에서 호출을 한다.
// AccountService 에서는 컨트롤러가 받은 요청을 처리를 할때
// 세션에 관련된 것들은 helper 패키지에 SessionAccountHelper 를 호출해 처리한다.
// 암호화와 관련된 작업은 util 패키지에 encryptionUtils 를 호출해 처리한다.
// 그 외에 기타사항은 자체적으로 처리한다.
// 최종적으로 DB 에 CRUD 기능은 repository 를 이용한다.

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;
	private final DepartmentRepository departmentRepository;
	private final ModelMapper modelMapper;

	static int idx = 0;

	public AccountRegisterResponseDTO create(AccountDTO req) {
		// 컨트롤러에서 올바른 값이 넘어왔다면 DB에 해당 정보가 중복되는게 있는지 확인
		AccountRegisterResponseDTO res = validateDuplicates(req);
		
		
		// 입력받은 비밀번호를 암호화 한 후에 다시 해당 dto 에 다시 저장
		
		
		// 비밀번호 암호화까지 마친 DTO를 Entity로 변환
		Member member = modelMapper.map(req, Member.class);

		return null;
	}

	private AccountRegisterResponseDTO validateDuplicates(AccountDTO req) {

		AccountRegisterResponseDTO res = new AccountRegisterResponseDTO();
		
		// 이메일 중복 검사
		if (accountRepository.existsByEmail(req.getEmail())) {
			return res.duplicateError("이메일", req.getEmail());
		}

		// 전화번호 중복 검사
		if (accountRepository.existsByPhoneNumber(req.getPhoneNumber())) {
			return res.duplicateError("전화번호", req.getPhoneNumber());
		}

		// 주민등록번호 중복 검사
		if (accountRepository.existsByResidentNumber(req.getResidentNumber())) {
			return res.duplicateError("주민등록번호", req.getResidentNumber());
		}

		// 계좌번호 중복 검사
		if (accountRepository.existsByAccountNumber(req.getAccountNumber())) {
			return res.duplicateError("계좌번호", req.getAccountNumber());
		}
		
		return null;
		
	}

	// 활성화된 부서 목록 조회
	public List<Departments> getActiveDepartments() {
		return departmentRepository.findByIsActiveTrue();
	}

}
