package com.itwillbs.ilkwangtech.schedule.controller;

import com.itwillbs.ilkwangtech.account.dto.AccountLogin;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.UriUtils;

import com.itwillbs.ilkwangtech.schedule.dto.ScheduleDTO;
import com.itwillbs.ilkwangtech.schedule.dto.ScheduleSearchDTO;
import com.itwillbs.ilkwangtech.schedule.entity.ScheduleFile;
import com.itwillbs.ilkwangtech.schedule.service.ScheduleService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@RequestMapping("/schedule/*")
@Log4j2
public class ScheduleController {

	private final ScheduleService scheduleService;
	
	// [추가 1] 빈 문자열("")을 null로 변환하여 LocalDate 바인딩 에러 방지
	@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

	@GetMapping("/calendar")
	public String calendarGET() {
		log.info("calendarGET() 실행!");
		log.info("calendarGET() 종료!");
		return "/schedule/calendar";
	}

	@GetMapping("/list")
	public String scheduleListGET(Model model, HttpSession session,
								  @ModelAttribute("searchParams") ScheduleSearchDTO params,
								  @RequestParam(name = "page", defaultValue = "1") int page,
								  @RequestParam(name = "sortField", defaultValue = "startDate") String sortField,
	                              @RequestParam(name = "sortDir", defaultValue = "desc") String sortDir) { // 페이지 번호 받기
		log.info("scheduleListGET() 실행!");
		// 1. 로그인 체크 (기존 코드 유지)
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		AccountLogin loginMember = (AccountLogin) authentication.getPrincipal();
		if (loginMember == null) return "redirect:/account/login";
		
		// 정렬 생성
		Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();
	    Pageable pageable = PageRequest.of(page - 1, 10, sort);

		// 2. 서비스 호출
		Page<ScheduleDTO> result = scheduleService.getScheduleList(loginMember.getId(), params, pageable);

		// 3. 모델에 담기
		model.addAttribute("schedules", result); // Page 객체 자체를 넘김

		// [추가] 페이지네이션 UI 계산 (보여줄 페이지 번호 범위)
		// 현재 페이지를 기준으로 앞뒤 5페이지씩, 총 10페이지 표시
		int blockLimit = 10;
		int startPage = (((int)(Math.ceil((double)page / blockLimit))) - 1) * blockLimit + 1;
		int endPage = Math.min((startPage + blockLimit - 1), result.getTotalPages());

		// 데이터가 아예 없을 때 endPage가 0이 되는 것을 방지
		if (endPage == 0) endPage = 1;

		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		model.addAttribute("currentPage", page);
		
		model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);

        log.info("scheduleListGET() 종료!");
		return "/schedule/list";
	}

	@PostMapping("/regist")
    public ResponseEntity<String> registerSchedule(@ModelAttribute ScheduleDTO scheduleDto,
                                                   @AuthenticationPrincipal AccountLogin accountLogin) { // 로그인 정보 가정
		log.info("registerSchedule() 실행!");
        
        // 유효성 검사 (Backend 측)
        if(scheduleDto.getTitle() == null || scheduleDto.getContent() == null) {
            return ResponseEntity.badRequest().body("필수 값이 누락되었습니다.");
        }

        scheduleService.registSchedule(scheduleDto, accountLogin.getId());
        log.info("registerSchedule() 종료!");
        return ResponseEntity.ok("Success");
    }
	
	// 일정 상세 조회
    @GetMapping("/get/{id}")
    @ResponseBody
    public ResponseEntity<ScheduleDTO> getScheduleDetail(@PathVariable("id") Long id) {
        log.info("getScheduleDetail() 실행: " + id);
        ScheduleDTO dto = scheduleService.getScheduleDetails(id);
        log.info("getScheduleDetail() 종료: " + id);
        return ResponseEntity.ok(dto);
    }

    // 일정 수정 처리
    @PostMapping("/update")
    public ResponseEntity<String> updateSchedule(@ModelAttribute ScheduleDTO scheduleDto,
                                                 @AuthenticationPrincipal AccountLogin accountLogin) {
        log.info("updateSchedule() 실행: " + scheduleDto.getId());
        
        try {
            scheduleService.updateSchedule(scheduleDto, accountLogin.getId());
            return ResponseEntity.ok("수정되었습니다.");
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
            log.error("수정 실패", e);
            return ResponseEntity.badRequest().body("수정 중 오류가 발생했습니다.");
        }
    }

    // 일정 삭제 처리
    @PostMapping("/delete") 
    public ResponseEntity<String> deleteSchedule(@RequestParam("id") Long id,
                                                 @AuthenticationPrincipal AccountLogin accountLogin) {
        log.info("deleteSchedule() 실행: " + id);
        try {
            scheduleService.deleteSchedule(id, accountLogin.getId());
            return ResponseEntity.ok("삭제되었습니다.");
        } catch (SecurityException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (Exception e) {
        	log.error("일정 삭제 중 오류 발생", e);
            return ResponseEntity.badRequest().body("삭제 실패");
        }
    }
    
 // [추가] 캘린더용 일정 데이터 조회 (JSON 반환)
    @GetMapping("/api/events")
    @ResponseBody
    public ResponseEntity<List<ScheduleDTO>> getCalendarEvents(
            @RequestParam(name = "start") String startStr,
            @RequestParam(name = "end") String endStr,
            @AuthenticationPrincipal AccountLogin accountLogin) {
        
        log.info("getCalendarEvents() 실행: {} ~ {}", startStr, endStr);

        // 검색 DTO 생성 및 날짜 설정
        ScheduleSearchDTO searchDTO = new ScheduleSearchDTO();
        // Toast UI는 ISO String 등을 보내므로 LocalDate로 파싱 필요
        // 편의상 String -> LocalDate 파싱 로직은 Service나 여기서 처리
        searchDTO.setStartDate(LocalDate.parse(startStr.substring(0, 10))); 
        searchDTO.setEndDate(LocalDate.parse(endStr.substring(0, 10)));
        
        // 페이지네이션 없이 전체 조회 (캘린더 범위 내)
        // 기존 Service 메서드 활용 (Pageable.unpaged() 사용 가능 시 사용, 아니면 size를 크게 잡음)
        Pageable pageable = PageRequest.of(0, 1000); 
        
        Page<ScheduleDTO> result = scheduleService.getScheduleList(accountLogin.getId(), searchDTO, pageable);
        
        return ResponseEntity.ok(result.getContent());
    }
    
    @Value("${file.upload.path:C:/upload/}") // application.properties의 경로와 일치해야 함
    private String uploadDir;
    
    // 썸네일 다운로드 기능 추가
    @GetMapping("/download/thumbnail/{scheduleId}")
    public ResponseEntity<Resource> downloadThumbnail(@PathVariable(name = "scheduleId") Long scheduleId) throws MalformedURLException {
        
        // 1. 일정 정보 조회 (Service 재사용 또는 Repository 사용)
        ScheduleDTO schedule = scheduleService.getScheduleDetails(scheduleId);
        String thumbnailPath = schedule.getThumbnailPath();
        
        if (thumbnailPath == null) {
            throw new RuntimeException("썸네일이 존재하지 않습니다.");
        }

        // 2. 파일 경로 설정
        Path filePath = Paths.get(uploadDir + thumbnailPath);
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("파일을 찾을 수 없습니다.");
        }

        // 3. 다운로드 헤더 설정 (파일명: thumbnail_일정ID.jpg 형식으로 지정)
        // 원본 파일명을 따로 저장하지 않았다면 UUID 부분을 제외하거나 임의의 이름을 부여
        String originalName = "thumbnail_" + scheduleId + ".jpg"; 
        
        // 만약 저장된 파일명에서 UUID를 떼고 싶다면 아래 로직 사용
        if (thumbnailPath.contains("_")) {
             originalName = thumbnailPath.substring(thumbnailPath.indexOf("_") + 1);
        }

        String encodedFileName = UriUtils.encode(originalName, StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    // [메서드 추가] 첨부파일 다운로드
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable(name = "fileId") Long fileId) throws MalformedURLException {
        
        // 1. DB에서 파일 정보 조회
        ScheduleFile fileEntity = scheduleService.getScheduleFile(fileId);
        
        // 2. 실제 파일 경로 찾기
        // 저장된 파일명(UUID_원본명)을 사용
        Path filePath = Paths.get(uploadDir + fileEntity.getSavedFileName());
        Resource resource = new UrlResource(filePath.toUri());
        
        if (!resource.exists() || !resource.isReadable()) {
            throw new RuntimeException("파일을 찾을 수 없거나 읽을 수 없습니다.");
        }

        // 3. 다운로드 시 원본 파일명으로 다운로드되도록 헤더 설정 (한글 깨짐 방지)
        String encodedFileName = UriUtils.encode(fileEntity.getOriginalFileName(), StandardCharsets.UTF_8);
        String contentDisposition = "attachment; filename=\"" + encodedFileName + "\"";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }
    
    // [추가] 개별 파일 삭제 요청 처리
    @PostMapping("/file/delete")
    @ResponseBody
    public ResponseEntity<String> deleteFile(@RequestParam("fileId") Long fileId,
                                             @AuthenticationPrincipal AccountLogin accountLogin) {
        try {
            // 로그인한 사용자의 ID를 같이 전달
            scheduleService.deleteAttachedFile(fileId, accountLogin.getId());
            return ResponseEntity.ok("삭제되었습니다.");
            
        } catch (SecurityException e) {
            // 권한 없을 때 403 Forbidden 반환
            return ResponseEntity.status(403).body(e.getMessage());
            
        } catch (Exception e) {
            log.error("파일 삭제 중 오류 발생", e);
            return ResponseEntity.badRequest().body("삭제 실패");
        }
    }

}
