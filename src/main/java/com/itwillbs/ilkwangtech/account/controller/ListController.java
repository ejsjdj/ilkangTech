package com.itwillbs.ilkwangtech.account.controller;

import com.itwillbs.ilkwangtech.account.dto.AccountDetailResponse;
import com.itwillbs.ilkwangtech.account.dto.AccountForList;
import com.itwillbs.ilkwangtech.account.service.ListService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 사원 목록 및 상세 조회를 담당하는 컨트롤러
 */
@Controller
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    /**
     * 사원 목록 페이지를 요청합니다.
     *
     * @param model 뷰에 전달할 데이터를 담는 객체
     * @return 사원 목록 페이지 뷰 경로
     */
    @GetMapping("/account/list")
    public String accountList(Model model) {
        return "/account/list";
    }

    /**
     * 특정 사원의 상세 정보를 조회합니다.
     *
     * @param id 사원의 고유 ID
     * @param model 뷰에 전달할 데이터를 담는 객체
     * @return 사원 상세 정보 페이지 뷰 경로
     */
    @GetMapping("/account/detail/{id}")
    public String accountDetail(@PathVariable("id") Long id, Model model) {

        AccountDetailResponse detail = listService.getAccountDetail(id);

        model.addAttribute("user", detail);

        return "/account/detail";
    }

    /**
     * 사원 목록 데이터를 조회합니다. (페이징, 정렬, 검색 지원)
     * 주로 프론트엔드의 데이터 테이블이나 리스트 UI에서 비동기 호출 시 사용됩니다.
     *
     * @param page 페이지 번호 (기본값 0)
     * @param searchField 검색 키워드 (이름, 사번, 연락처 등)
     * @param sortBy 정렬 기준 필드 (기본값 id)
     * @param direction 정렬 방향 (DESC/ASC, 기본값 DESC)
     * @return 사원 정보 목록이 담긴 Page 객체
     */
    @GetMapping("/account/getList")
    @ResponseBody
    public Page<AccountForList> getEmployeeList(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "searchField", defaultValue = "") String searchField,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by(direction, sortBy));

        // 검색어가 있는 경우 검색 결과 반환
        if (searchField != null && !searchField.isEmpty()) {
            return listService.searchAccountList(searchField, pageable);
        }

        // 검색어가 없는 경우 전체 목록 반환
        return listService.getAccountList(pageable);
    }

}
