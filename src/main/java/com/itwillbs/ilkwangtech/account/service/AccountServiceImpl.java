package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.account.dto.AccountRegisterRequest;
import com.itwillbs.ilkwangtech.account.dto.AccountRegisterResponse;
import com.itwillbs.ilkwangtech.account.entity.ProfileImg;
import com.itwillbs.ilkwangtech.account.repository.AccountRepository;
import com.itwillbs.ilkwangtech.account.repository.ProfileImgRepository;
import com.itwillbs.ilkwangtech.common.exception.MemberNotFoundException;
import com.itwillbs.ilkwangtech.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * AccountService 인터페이스의 구현체
 * 회원 등록 시 중복 검증, 사원번호 생성, 비밀번호 암호화 등을 수행합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

	private final AccountRepository accountRepository;
	private final ProfileImgRepository profileImgRepository;
	private final BCryptPasswordEncoder passwordEncoder;
	private final ModelMapper modelMapper;
	private final RoleService roleService;

	@Value("${file.uploadBaseLocation}")
	private String uploadBaseLocation;

	@Value("${file.profileImgLocation}")
	private String profileImageLocation;

	/**
	 * 회원가입(사원 등록) 로직을 수행합니다.
	 * 1. 이메일, 전화번호 등의 중복 여부를 검증합니다.
	 * 2. 현재 등록된 최대 사원번호를 조회하여 새로운 사원번호를 생성합니다.
	 * 3. 비밀번호를 BCrypt로 암호화합니다.
	 * 4. DB에 사원 정보를 저장합니다.
	 *
	 * @param req 회원가입 요청 데이터
	 * @return 등록 결과 응답 데이터
	 */
	public AccountRegisterResponse register(AccountRegisterRequest req) {

		// 1. 중복 데이터 검증 (이메일, 전화번호, 주민번호, 계좌번호)
		AccountRegisterResponse res = validateDuplicates(req);
		if (res != null) {
			return res; // 중복이 발견되면 에러 메시지가 담긴 응답 반환
		} else {
			res = new AccountRegisterResponse();
		}

		// 2. 새로운 사원번호 생성을 위한 시퀀스 조회 및 설정
		int maxIdx = accountRepository.findMaxEmployeeIdx();
		req.setEmployeeNumber(maxIdx + 1);
		
		// 3. DTO를 Entity로 변환
		Member member = modelMapper.map(req, Member.class);

		// 4. 비밀번호 암호화 처리
		String rawPassword = req.getPassword();
		String encodedPassword = passwordEncoder.encode(rawPassword);
		member.setPassword(encodedPassword);

		// 5. DB 저장
		Member savedMember = accountRepository.save(member);

		res.setSuccess(true);
		res.setEmployeeNumber(savedMember.getEmployeeNumber());
		res.setMessage("회원가입 성공");

		// 6. 해당 사원의 권한 저장
		Long memberId = savedMember.getId();
		Long departmentIdx = Long.valueOf(savedMember.getDepartment());
		roleService.grantRole(memberId, 1000L);	// 기본 권한 자동 부여
		roleService.grantRole(memberId, departmentIdx);		// 해당 권한에 맞는 권한 부여

		return res;
	}

	/**
	 * 사원의 내 정보(이메일, 연락처)를 수정합니다.
	 *
	 * @param id 사원 고유 ID
	 * @param email 수정할 이메일
	 * @param phoneNumber 수정할 전화번호
	 * @return 성공 여부
	 */
	@Override
	public boolean updateMyInfo(Long id, String email, String phoneNumber) {
		Member member = accountRepository.findById(id)
				.orElseThrow(() -> new MemberNotFoundException("사용자를 찾을 수 없습니다. ID: " + id));

		member.setEmail(email);
		member.setPhoneNumber(phoneNumber);

		accountRepository.save(member);
		return true;
	}

	/**
	 * 특정 회원이 로그인한 본인인지 확인합니다.
	 *
	 * @param id 확인할 회원 ID
	 * @param employeeNumber 로그인한 사용자의 사원번호
	 * @return 본인 여부
	 */
	@Override
	public boolean isSelf(Long id, String employeeNumber) {
		return accountRepository.findById(id)
				.map(member -> member.getEmployeeNumber().equals(employeeNumber))
				.orElse(false);
	}

	/**
	 * 등록 시 데이터 중복 여부를 확인하는 헬퍼 메서드
	 *
	 * @param req 등록 요청 데이터
	 * @return 중복 발견 시 에러 메시지가 담긴 응답 DTO, 없으면 null
	 */
	private AccountRegisterResponse validateDuplicates(AccountRegisterRequest req) {

		AccountRegisterResponse res = new AccountRegisterResponse();
		res.setSuccess(false);

		if (accountRepository.existsByEmail(req.getEmail())) {
			res.setMessage("이메일을 다시 확인해 주세요!");
			return res;
		}

		if (accountRepository.existsByPhoneNumber(req.getPhoneNumber())) {
			res.setMessage("전화번호를 다시 확인해 주세요!");
			return res;
		}

		if (accountRepository.existsByResidentNumber(req.getResidentNumber())) {
			res.setMessage("이미 가입했되어 있습니다.");
			return res;
		}

		if (accountRepository.existsByAccountNumber(req.getAccountNumber())) {
			res.setMessage("계좌번호를 변경해주세요");
			return res;
		}
		return null;
	}

	public int updateProfileImage(MultipartFile upload, @AuthenticationPrincipal AccountLogin login) throws IOException {

		if (login == null || upload == null || upload.isEmpty()) return 0;

		// 기존 대표 이미지 해제
		profileImgRepository.findByMemberIdAndRepImgYn(login.getId(), "Y")
				.ifPresent(existingImg -> {
					existingImg.setRepImgYn("N");
					profileImgRepository.save(existingImg);
				});

		ProfileImg profileImg = new ProfileImg();

		LocalDate today = LocalDate.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
		String subDir = today.format(dtf);

		Path uploadDir = Paths.get(uploadBaseLocation, subDir).toAbsolutePath().normalize();

		if(!Files.exists(uploadDir)) {
			Files.createDirectories(uploadDir);
		}

		String originalFileName = upload.getOriginalFilename();
		String fileName = UUID.randomUUID().toString() + "_" + originalFileName;
		Path uploadPath = uploadDir.resolve(fileName);
		upload.transferTo(uploadPath);

		Member member = accountRepository.findById(login.getId()).orElseThrow();

		profileImg.setMember(member);
		profileImg.setImgName(fileName);
		profileImg.setOriginalImgName(originalFileName);
		profileImg.setImgLocation(profileImageLocation + "/" + subDir);
		profileImg.setRepImgYn("Y");

		profileImgRepository.save(profileImg);

		// 세션 정보 갱신을 위해 URL 설정
		String url = profileImageLocation + "/" + subDir + "/" + fileName;
		login.setProfileImgUrl(url);

		return 1;
	}


}
