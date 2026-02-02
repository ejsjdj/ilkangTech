package com.itwillbs.ilkwangtech.sales.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sales/delivery")
/**
 * 배송 상태를 관리
 * 제품 출하 계획이 생기면 create 를 하고
 * 제품이 출하가 완료가 되면 update 를 해서 finished 를 true로 바꾼다
 * 출하가 완료될 시 창고에 있는 제품의 재고를 감소시킨다
 * 출하가 완료되면 자동으로 인보이스를 발행한다.
 */
public class DeliveryController {



}
