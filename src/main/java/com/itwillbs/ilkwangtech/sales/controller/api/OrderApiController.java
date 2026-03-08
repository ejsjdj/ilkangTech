package com.itwillbs.ilkwangtech.sales.controller.api;

import com.itwillbs.ilkwangtech.common.dto.ApiResponseDTO;
import com.itwillbs.ilkwangtech.sales.constant.OrderStatus;
import com.itwillbs.ilkwangtech.sales.dto.OrderDTO;
import com.itwillbs.ilkwangtech.sales.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Log4j2
public class OrderApiController {

    private final OrderService orderService;

    @GetMapping("/list")
    public ResponseEntity<ApiResponseDTO<List<OrderDTO>>> getOrderList(@RequestParam OrderStatus category, Pageable pageable) {
        List<OrderDTO> orderList = orderService.getOrderList(category, pageable);
        return ResponseEntity.ok(ApiResponseDTO.success(orderList));
    }

}