package com.itwillbs.ilkwangtech.sales.controller.api;

import com.itwillbs.ilkwangtech.sales.constant.OrderStatus;
import com.itwillbs.ilkwangtech.sales.dto.ApiResponseDTO;
import com.itwillbs.ilkwangtech.sales.dto.OrderDTO;
import com.itwillbs.ilkwangtech.sales.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales/orders")
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderService orderService;

    // 1. 신규 수주 등록 (POST)
    // 수주 헤더(날짜, 고객사)와 상세 내역(품목, 수량)을 한 번에 받습니다.
    @PostMapping
    public void createOrder(@RequestBody OrderDTO orderDTO) {
        orderService.createOrder(orderDTO);
    }

    // 2. 수주 목록 조회 (GET)
    // 기간, 고객사명, 진행 상태별 필터링이 필수입니다.
    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<OrderDTO>>> getOrderList(Pageable pageable) {

        List<OrderDTO> orderList = orderService.getOrderList(pageable);

        return ResponseEntity.ok(ApiResponseDTO.success(orderList));
    }

    // 3. 수주 상세 조회 (GET)
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<OrderDTO>> getOrder(@PathVariable Long id) {


        return ResponseEntity.ok(ApiResponseDTO.success(orderService.getOrder(id)));
    }

    // 4. 수주 정보 수정 (PUT)
    // 수주가 이미 '생산 시작' 또는 '출고' 상태라면 수정을 제한하는 로직이 필요합니다.
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<OrderDTO>> updateOrder(@PathVariable Long id, @RequestBody OrderDTO orderDTO) {
        if (orderService.isEditable(id)) {
            orderService.update(orderDTO);
            return ResponseEntity.ok(ApiResponseDTO.success(orderDTO));
        }
        return ResponseEntity.badRequest().body(ApiResponseDTO.fail("수주 수정 불가"));
    }

    // 5. 수주 삭제 (PUT)
    // 이력을 남기기 위해 '주문 취소'
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<OrderDTO>> deleteOrder(@PathVariable Long id) {

        OrderDTO orderDTO = orderService.getOrder(id);

        if (orderService.isEditable(id)) {
            orderService.invalid(id);
            return ResponseEntity.ok(ApiResponseDTO.success(orderDTO));
        }

        return ResponseEntity.badRequest().body(ApiResponseDTO.fail("수주 삭제 불가"));

    }

    // 6. 수주 상태 일괄 변경 (PATCH)
    // 수주 확정, 생산 요청 등 상태값만 바꿀 때 유용합니다.
    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponseDTO<String>> changeOrderStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus status) { // String 대신 OrderStatus Enum으로 직접 받기

        orderService.changeOrderStatus(id, status);
        return ResponseEntity.ok(new ApiResponseDTO<>(true, "상태가 변경되었습니다.", status.getDescription()));
    }

}