package com.itwillbs.ilkwangtech.sales.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sales/procurement")
public class ProcurementController {

    @GetMapping("/list")
    public String getCustomerList() {
        return "sales/customer/purchaseOrder";
    }

    @GetMapping("/register")
    public String registCustomerList() {
        return "sales/customer/purchaseOrderRegister";
    }

    @GetMapping("/return")
    public String returnCustomerList(){
        return "sales/customer/purchaseReturn";
    }
}
