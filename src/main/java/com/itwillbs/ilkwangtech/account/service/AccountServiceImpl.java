package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.AccountRegisterRequest;
import com.itwillbs.ilkwangtech.account.dto.AccountRegisterResponse;
import com.itwillbs.ilkwangtech.account.entity.Department;
import com.itwillbs.ilkwangtech.account.repository.DepartmentRepository;
import com.itwillbs.ilkwangtech.account.util.EncryptionUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.itwillbs.ilkwangtech.account.dto.AccountDetail;
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
	private final EncryptionUtils encryptionUtils;
	private final ModelMapper modelMapper;

	static int idx = 0;

	public AccountRegisterResponse register(AccountRegisterRequest req) {

		// 컨트롤러에서 올바른 값이 넘어왔다면 DB에 해당 정보가 중복되는게 있는지 확인
		AccountRegisterResponse res = validateDuplicates(req);
		if (res != null) {
			return res;
		} else {
			res = new AccountRegisterResponse();
		}

		req.setEmployeeNumber(idx);
		req.setPassword(encryptionUtils.encrypt(req.getPassword()));
		Member member = accountRepository.save(modelMapper.map(req, Member.class));
		res.setSuccess(true);
		res.setEmployeeNumber(member.getEmployeeNumber());
		res.setMessage("회원가입 성공");

		return res;
	}

	private AccountRegisterResponse validateDuplicates(AccountRegisterRequest req) {

		AccountRegisterResponse res = new AccountRegisterResponse();
		res.setSuccess(false);

		// 이메일 중복 검사
		if (accountRepository.existsByEmail(req.getEmail())) {
			res.setMessage("이메일을 다시 확인해 주세요!");
			return res;
		}

		// 전화번호 중복 검사
		if (accountRepository.existsByPhoneNumber(req.getPhoneNumber())) {
			res.setMessage("전화번호를 다시 확인해 주세요!");
			return res;
		}

		// 주민등록번호 중복 검사
		if (accountRepository.existsByResidentNumber(req.getResidentNumber())) {
			res.setMessage("이미 가입했되어 있습니다.");
			return res;
		}

		// 계좌번호 중복 검사
		if (accountRepository.existsByAccountNumber(req.getAccountNumber())) {
			res.setMessage("계좌번호를 변경해주세요");
			return res;
		}
		return null;
	}

}
