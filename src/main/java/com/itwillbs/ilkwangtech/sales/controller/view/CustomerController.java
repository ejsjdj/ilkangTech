package com.itwillbs.ilkwangtech.sales.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sales/customer")
public class CustomerController {

    @GetMapping("/list")
    public String getCustomerList() {
        return "sales/customer/list";
    }

}
