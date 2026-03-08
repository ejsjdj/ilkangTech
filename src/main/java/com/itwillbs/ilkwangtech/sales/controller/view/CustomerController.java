package com.itwillbs.ilkwangtech.sales.controller.view;

import com.itwillbs.ilkwangtech.sales.dto.CompanyDetailDTO;
import com.itwillbs.ilkwangtech.sales.service.company.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sales/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CompanyService companyService;

    @GetMapping("/list")
    public String getCustomerList() {
        return "sales/customer/list";
    }

    @GetMapping("/detail/{id}")
    public String getCustomerDetail(@PathVariable("id") Long id, Model model) {
        CompanyDetailDTO detail = companyService.getCompanyDetail(id);
        model.addAttribute("companyDetail", detail);
        model.addAttribute("company", detail.getCompany());
        return "sales/customer/customer-detail";
    }

}
