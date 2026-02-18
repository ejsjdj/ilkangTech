package com.itwillbs.ilkwangtech.notice.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.member.entity.Member;
import com.itwillbs.ilkwangtech.member.repository.MemberRepository;
import com.itwillbs.ilkwangtech.notice.dto.NoticeDetailDTO;
import com.itwillbs.ilkwangtech.notice.dto.NoticeSearchDTO;
import com.itwillbs.ilkwangtech.notice.dto.NoticeWriteDTO;
import com.itwillbs.ilkwangtech.notice.repository.NoticeFileRepository;
import com.itwillbs.ilkwangtech.notice.service.NoticeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequestMapping("/notice/*")
@Log4j2
@RequiredArgsConstructor
public class NoticeController {
	
	private final NoticeService noticeService;
	private final NoticeFileRepository noticeFileRepository;
	
	private final MemberRepository memberRepository;
	
	// 권한 체크 로직
    private boolean checkAdminPermission(Long memberId) {
        if (memberId == null) return false;

        // 1. 최신 회원 정보 조회 (DB)
        Member freshMember = memberRepository.findById(memberId).orElse(null);

        // 2. 권한 확인
        if (freshMember != null && freshMember.getRoles() != null) {
            return freshMember.getRoles().stream()
                    .anyMatch(memberRole -> {
                        Long roleId = memberRole.getRole().getId();
                        // 0: 시스템관리자, 2: 관리자, 6: 인사관리자 (예시)
                        return roleId == 6L || roleId == 0L || roleId == 2L;
                    });
        }
        return false;
    }
	
	@GetMapping("/list")
	public String list(@RequestParam(value = "page", defaultValue = "0") int page, Model model, 
	                   @AuthenticationPrincipal AccountLogin loginMember,
	                   @ModelAttribute NoticeSearchDTO searchDTO) {
	    
	    // 고정 게시글 및 일반 리스트 조회
	    model.addAttribute("pinnedList", noticeService.getPinnedNotices());
        model.addAttribute("noticeList", noticeService.getNoticeList(page, searchDTO));
        model.addAttribute("searchDTO", searchDTO);

	    // 권한 체크: 전산관리부 혹은 관리부 계정 확인
        boolean canWrite = false;
        
        if (loginMember != null) {
            // 1. 현재 접속한 사용자의 최신 정보를 DB에서 다시 가져옴 (새로고침 효과)
            Member freshMember = memberRepository.findById(loginMember.getId()).orElse(null);
        
            if (freshMember != null && freshMember.getRoles() != null) {
                // 2. 방금 가져온 정보(freshMember)로 권한 체크
                canWrite = freshMember.getRoles().stream()
                        .anyMatch(memberRole -> {
                            Long roleId = memberRole.getRole().getId();
                            return roleId == 6L || roleId == 0L || roleId == 2L;
                        });
                
                log.info("실시간 권한 체크 - 사원: {}, 결과: {}", freshMember.getName(), canWrite);
            }
        }
	    model.addAttribute("canWrite", canWrite);

	    return "notice/list";
	}
	
	// 공지사항 작성 페이지 이동
	@GetMapping("/write")
    public String writeForm(@AuthenticationPrincipal AccountLogin loginMember, Model model) {
        // 비로그인 체크
        if (loginMember == null) {
            return "redirect:/login";
        }
        
        // Role ID 기반 권한 체크
        if (!checkAdminPermission(loginMember.getId())) {
            log.warn("작성 권한 없음: 사용자 ID {}", loginMember.getId());
            return "redirect:/notice/list"; // 권한 없으면 리스트로
        }
        
        model.addAttribute("writerName", loginMember.getName());
        model.addAttribute("writerDept", loginMember.getDepartment()); 
        return "notice/write";
    }

    // 고정 게시글 개수 체크 API
    @GetMapping("/api/check-pinned")
    @ResponseBody
    public boolean checkPinnedCount() {
        // 3개 이상이면 true(제한), 미만이면 false(가능) 반환
        return noticeService.checkPinnedLimit();
    }

    // 공지사항 등록 처리
    @PostMapping("/register")
    public String register(NoticeWriteDTO dto, @AuthenticationPrincipal AccountLogin loginMember) throws IOException {
        noticeService.registerNotice(dto, loginMember);
        return "redirect:/notice/list";
    }
    
    // 상세 페이지 이동
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal AccountLogin loginMember) {
        NoticeDetailDTO notice = noticeService.getNoticeDetail(id);
        model.addAttribute("notice", notice);
        
        // Role ID 기반 권한 체크
        boolean canEdit = false;
        if (loginMember != null) {
            canEdit = checkAdminPermission(loginMember.getId());
        }
        
        model.addAttribute("canEdit", canEdit);
        
        return "notice/detail";
    }
    
    // 4. 수정 페이지 이동
    @GetMapping("/modify/{id}")
    public String modifyForm(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal AccountLogin loginMember) {
        // 비로그인 체크
        if (loginMember == null) {
            return "redirect:/login";
        }

        // Role ID 기반 권한 체크
        if (!checkAdminPermission(loginMember.getId())) {
            log.warn("수정 페이지 접근 권한 없음: 사용자 ID {}", loginMember.getId());
            return "redirect:/notice/list";
        }

        // 기존 데이터 조회
        NoticeDetailDTO notice = noticeService.getNoticeDetail(id);
        model.addAttribute("notice", notice);
        
        return "notice/modify";
    }

    // 5. 수정 처리
    @PostMapping("/modify")
    public String modify(NoticeWriteDTO dto, @AuthenticationPrincipal AccountLogin loginMember) throws IOException {
        // 비로그인 체크
        if (loginMember == null) {
            return "redirect:/login";
        }

        // Role ID 기반 권한 체크
        if (!checkAdminPermission(loginMember.getId())) {
            log.warn("수정 처리 권한 없음: 사용자 ID {}", loginMember.getId());
            return "redirect:/notice/list";
        }

        noticeService.updateNotice(dto, loginMember);
        return "redirect:/notice/detail/" + dto.getId();
    }

    // 삭제 처리
    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id, @AuthenticationPrincipal AccountLogin loginMember) {
        // 비로그인 체크
        if (loginMember == null) {
            return "redirect:/login";
        }

        // Role ID 기반 권한 체크
        if (!checkAdminPermission(loginMember.getId())) {
            log.warn("삭제 권한 없음: 사용자 ID {}", loginMember.getId());
            return "redirect:/notice/list";
        }
        
        noticeService.deleteNotice(id);
        return "redirect:/notice/list";
    }

    // 권한 체크 헬퍼 메서드
    private boolean isAuthorized(String dept) {
        return "정보시스템팀".equals(dept) || "임원팀".equals(dept) || "인사팀".equals(dept);
    }

    // 첨부파일 다운로드 처리
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> download(@PathVariable("fileId") Long fileId) throws IOException {
        // 1. 서비스에서 '실제 파일'을 가져옴 (경로 문제 해결됨)
        File file = noticeService.getDownloadFile(fileId);
        
        if (!file.exists()) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다: " + file.getAbsolutePath());
        }

        // 2. 스트림 리소스 생성
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        
        // 3. 다운로드 파일명 인코딩 (한글 깨짐 방지)
        // UUID(36자) + _(1자) = 37자 제거 후 원본 이름 추출
        String originalName = file.getName().substring(37); 
        String encodedName = URLEncoder.encode(originalName, StandardCharsets.UTF_8.toString())
                                       .replaceAll("\\+", "%20"); // 공백 처리

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length())
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedName + "\"")
                .body(resource);
    }
    
 // [추가 1] 썸머노트 이미지 업로드 (AJAX 요청)
    @PostMapping("/api/upload-image")
    @ResponseBody
    public ResponseEntity<String> uploadSummernoteImage(@RequestParam("file") MultipartFile file) {
        try {
            String savedFileName = noticeService.uploadSummernoteImage(file);
            // 업로드된 이미지를 불러올 수 있는 URL 반환
            // 예: /notice/api/image/uuid_filename.jpg
            return ResponseEntity.ok("/notice/api/image/" + savedFileName);
        } catch (IOException e) {
            return ResponseEntity.badRequest().body("이미지 업로드 실패");
        }
    }

    // [추가 2] 썸머노트 본문 이미지 출력 (src 태그용)
    @GetMapping("/api/image/{filename}")
    public ResponseEntity<Resource> showImage(@PathVariable("filename") String filename) throws IOException {
        File file = noticeService.getSummernoteImageFile(filename);

        if (!file.exists()) {
            return ResponseEntity.notFound().build();
        }

        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        
        // 이미지 MIME 타입 자동 감지 (또는 MediaType.IMAGE_JPEG 등으로 고정 가능)
        // 여기서는 간단히 옥텟 스트림 혹은 확장자 체크 로직 필요하나, 기본적으로 브라우저가 해석하게 둠
        return ResponseEntity.ok()
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM) // 혹은 이미지 타입에 맞춰 설정
                .body(resource);
    }
	

}
