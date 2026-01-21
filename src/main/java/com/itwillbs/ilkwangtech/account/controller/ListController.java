package com.itwillbs.ilkwangtech.account.controller;

import com.itwillbs.ilkwangtech.account.dto.AccountDetail;
import com.itwillbs.ilkwangtech.account.service.ListService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping("/account/getList")
    @ResponseBody
    public Page<AccountDetail> getEmployeeList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "") String searchField,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "DESC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, 10, Sort.by(direction, sortBy));

        if (searchField != null && !searchField.isEmpty()) {
            return listService.searchAccountList(searchField, pageable);
        }

        return listService.getAccountList(pageable);
    }

}
