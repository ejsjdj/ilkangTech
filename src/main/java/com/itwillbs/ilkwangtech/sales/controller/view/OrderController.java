package com.itwillbs.ilkwangtech.sales.controller.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sales/order")
public class OrderController {

    @GetMapping("/list")
    public String getOrderList() {
        return "sales/order/order_list";
    }

}
