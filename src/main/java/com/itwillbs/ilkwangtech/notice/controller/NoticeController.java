package com.itwillbs.ilkwangtech.notice.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.util.UriUtils;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;
import com.itwillbs.ilkwangtech.notice.dto.NoticeDetailDTO;
import com.itwillbs.ilkwangtech.notice.dto.NoticeSearchDTO;
import com.itwillbs.ilkwangtech.notice.dto.NoticeWriteDTO;
import com.itwillbs.ilkwangtech.notice.entity.NoticeFile;
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
	
	@GetMapping("/list")
	public String list(@RequestParam(value = "page", defaultValue = "0") int page, Model model, 
	                   @AuthenticationPrincipal AccountLogin loginMember,
	                   @ModelAttribute NoticeSearchDTO searchDTO) {
	    
	    // 고정 게시글 및 일반 리스트 조회
	    model.addAttribute("pinnedList", noticeService.getPinnedNotices());
        model.addAttribute("noticeList", noticeService.getNoticeList(page, searchDTO));
        model.addAttribute("searchDTO", searchDTO);

	    // 1-4. 권한 체크: 전산관리부 혹은 관리부 계정 확인
	    boolean canWrite = false;
	    
	    if (loginMember != null) {
	        // AccountLogin 객체의 department 필드를 직접 참조
	        String dept = loginMember.getDepartment(); 
	        
	        if ("정보시스템팀".equals(dept) || "임원팀".equals(dept) || "인사팀".equals(dept)) {
	            canWrite = true;
	        }
	        
	        log.info("접속 사원: {}, 부서: {}, 쓰기권한: {}", loginMember.getName(), dept, canWrite);
	    }
	    model.addAttribute("canWrite", canWrite);

	    return "notice/list";
	}
	
	// 1-1. 공지사항 작성 페이지 이동
    @GetMapping("/write")
    public String writeForm(@AuthenticationPrincipal AccountLogin loginMember, Model model) {
        // 1-7. 권한 체크 (URL 직접 접근 방지)
        if (loginMember == null) return "redirect:/login";
        String dept = loginMember.getDepartment();
        if (!("정보시스템팀".equals(dept) || "임원팀".equals(dept) || "인사팀".equals(dept))) {
            return "redirect:/notice/list"; // 권한 없으면 리스트로 튕겨내기
        }
        
        model.addAttribute("writerName", loginMember.getName());
        model.addAttribute("writerDept", dept);
        return "notice/write";
    }

    // 1-6. 고정 게시글 개수 체크 API (AJAX용)
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
    
    // 1-2. 상세 페이지 이동
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal AccountLogin loginMember) {
        NoticeDetailDTO notice = noticeService.getNoticeDetail(id);
        model.addAttribute("notice", notice);
        
        // 3. 권한 체크 (수정/삭제 버튼 노출용)
        boolean canEdit = false;
        if (loginMember != null) {
            String dept = loginMember.getDepartment();
            if ("정보시스템팀".equals(dept) || "임원팀".equals(dept) || "인사팀".equals(dept)) {
                canEdit = true;
            }
        }
        model.addAttribute("canEdit", canEdit);
        
        return "notice/detail";
    }
    
    // [추가] 1. 수정 페이지 이동 (기존 내용 불러오기)
    @GetMapping("/modify/{id}")
    public String modifyForm(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal AccountLogin loginMember) {
        // 권한 체크
        if (loginMember == null || !isAuthorized(loginMember.getDepartment())) {
            return "redirect:/notice/list";
        }

        // 기존 데이터 조회
        NoticeDetailDTO notice = noticeService.getNoticeDetail(id);
        model.addAttribute("notice", notice);
        
        return "notice/modify";
    }

    // [추가] 2. 수정 처리
    @PostMapping("/modify")
    public String modify(NoticeWriteDTO dto, @AuthenticationPrincipal AccountLogin loginMember) throws IOException {
        if (loginMember == null || !isAuthorized(loginMember.getDepartment())) {
            return "redirect:/notice/list";
        }
        noticeService.updateNotice(dto, loginMember);
        return "redirect:/notice/detail/" + dto.getId();
    }

    // [추가] 3. 삭제 처리
    @PostMapping("/delete")
    public String delete(@RequestParam("id") Long id, @AuthenticationPrincipal AccountLogin loginMember) {
        if (loginMember == null || !isAuthorized(loginMember.getDepartment())) {
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
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileId") Long fileId) throws MalformedURLException {
        
        // 1. DB에서 파일 정보 조회
        NoticeFile fileEntity = noticeFileRepository.findById(fileId).orElse(null);
        if (fileEntity == null) {
            return ResponseEntity.notFound().build();
        }

        // 2. 실제 파일 경로를 통해 리소스 생성 (file:///C:/upload/notice/uuid_filename)
        // savedFileName만 가지고 있다면 전체 경로를 조합해야 함
        // NoticeService에서 정의한 경로와 일치해야 합니다. (여기선 예시 경로)
        String uploadPath = "C:/upload/"; 
        UrlResource resource = new UrlResource("file:" + uploadPath + fileEntity.getSavedFileName());

        // 3. 한글 파일명 깨짐 방지 인코딩
        String encodedUploadFileName = UriUtils.encode(fileEntity.getOriginalFileName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedUploadFileName + "\"";

        // 4. 다운로드 응답 반환
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
	

}
