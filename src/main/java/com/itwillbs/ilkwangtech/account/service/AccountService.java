package com.itwillbs.ilkwangtech.account.service;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.account.dto.AccountRegisterRequestDTO;
import com.itwillbs.ilkwangtech.account.dto.AccountRegisterResponseDTO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 계정 관리 비즈니스 로직을 정의한 서비스 인터페이스
 * 회원가입, 내 정보 수정, 본인 확인 등의 기능을 정의합니다.
 */
public interface AccountService {

    /**
     * 새로운 사원을 등록(회원가입)합니다.
     *
     * @param request 회원가입 정보가 담긴 DTO
     * @return 등록 성공 여부 및 사원번호 등이 포함된 응답 DTO
     */
    AccountRegisterResponseDTO register(AccountRegisterRequestDTO request);

    /**
     * 사원의 개인 정보를 수정합니다. (이메일, 전화번호)
     *
     * @param id 사원의 고유 ID
     * @param email 수정할 이메일 주소
     * @param phoneNumber 수정할 전화번호
     * @return 수정 성공 여부
     */
    boolean updateMyInfo(Long id, String email, String phoneNumber);

    /**
     * 요청받은 ID가 특정 사원번호의 본인인지 확인합니다.
     * 보안 검증(IDOR 방지) 시 사용됩니다.
     *
     * @param id 확인할 회원의 고유 ID
     * @param employeeNumber 비교 대상이 될 사원번호
     * @return 본인 여부
     */
    boolean isSelf(Long id, String employeeNumber);

    int updateProfileImage(@AuthenticationPrincipal AccountLogin login, MultipartFile upload) throws IOException;

    boolean resetPassword(Long id);

    boolean changePassword(Long id, String oldPassword, String newPassword);

    boolean verifyMember(String employeeNumber, String name, String email);

    boolean resetPassword(String employeeNumber, String newPassword);
}
