package com.itwillbs.ilkwangtech.account.controller;

import com.itwillbs.ilkwangtech.account.dto.AccountDetail;
import com.itwillbs.ilkwangtech.account.dto.AccountDetailResponse;
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

import java.util.List;

@Controller
public class ListController {

    private final ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    @GetMapping("/account/list")
    public String accountList(Model model) {
        return "/account/list";
    }

    @GetMapping("/account/detail/{id}")
    public String accountDetail(@PathVariable("id") Long id, Model model) {
        AccountDetailResponse detail = listService.getAccountDetail(id);
        model.addAttribute("employee", detail);
        return "/account/detail";
    }

    @GetMapping("/account/getList")
    @ResponseBody
    public Page<AccountDetail> getEmployeeList(
    		@RequestParam(name = "page", defaultValue = "0") int page, 
            @RequestParam(name = "searchField", defaultValue = "") String searchField,
            @RequestParam(name = "sortBy", defaultValue = "id") String sortBy,
            @RequestParam(name = "direction", defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by(direction, sortBy));

        if (searchField != null && !searchField.isEmpty()) {
            return listService.searchAccountList(searchField, pageable);
        }

        return listService.getAccountList(pageable);
    }

}
