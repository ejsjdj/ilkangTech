package com.itwillbs.ilkwangtech.sales.controller;

import com.itwillbs.ilkwangtech.sales.dto.DeliveryDTO;
import com.itwillbs.ilkwangtech.sales.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sales/delivery")
@RequiredArgsConstructor
/**
 * 배송 상태를 관리
 * 제품 출하 계획이 생기면 create 를 하고
 * 제품이 출하가 완료가 되면 update 를 해서 finished 를 true로 바꾼다
 * 출하가 완료될 시 창고에 있는 제품의 재고를 감소시킨다
 * 출하가 완료되면 자동으로 인보이스를 발행한다.
 */
public class DeliveryController {

    private final DeliveryService deliveryService;

    // 전체 배송 목록 조회
    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> getAllDeliveries() {
        List<DeliveryDTO> deliveries = deliveryService.getAllDeliveries();
        return ResponseEntity.ok(deliveries);
    }

    // 특정 배송 조회
    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> getDeliveryById(@PathVariable Long id) {
        DeliveryDTO delivery = deliveryService.getDeliveryById(id);
        return ResponseEntity.ok(delivery);
    }

    // 새로운 배송 생성
    @PostMapping
    public ResponseEntity<DeliveryDTO> createDelivery(@RequestBody DeliveryDTO deliveryDTO) {
        DeliveryDTO createdDelivery = deliveryService.createDelivery(deliveryDTO);
        return ResponseEntity.ok(createdDelivery);
    }

    // 배송 정보 수정
    @PutMapping("/{id}")
    public ResponseEntity<DeliveryDTO> updateDelivery(@PathVariable Long id, @RequestBody DeliveryDTO deliveryDTO) {
        DeliveryDTO updatedDelivery = deliveryService.updateDelivery(id, deliveryDTO);
        return ResponseEntity.ok(updatedDelivery);
    }

    // 배송 삭제 (대기 중인 상태만 가능)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Long id) {
        deliveryService.deleteDelivery(id);
        return ResponseEntity.ok().build();
    }

    // 배송 시작 (재고 감소)
    @PostMapping("/{id}/start")
    public ResponseEntity<DeliveryDTO> startDelivery(@PathVariable Long id) {
        DeliveryDTO delivery = deliveryService.startDelivery(id);
        return ResponseEntity.ok(delivery);
    }

    // 배송 완료 (인보이스 자동 발행)
    @PostMapping("/{id}/complete")
    public ResponseEntity<DeliveryDTO> completeDelivery(@PathVariable Long id) {
        DeliveryDTO delivery = deliveryService.completeDelivery(id);
        return ResponseEntity.ok(delivery);
    }

    // 대기 중인 배송 목록 조회
    @GetMapping("/pending")
    public ResponseEntity<List<DeliveryDTO>> getPendingDeliveries() {
        List<DeliveryDTO> deliveries = deliveryService.getPendingDeliveries();
        return ResponseEntity.ok(deliveries);
    }

    // 진행 중인 배송 목록 조회
    @GetMapping("/active")
    public ResponseEntity<List<DeliveryDTO>> getActiveDeliveries() {
        List<DeliveryDTO> deliveries = deliveryService.getActiveDeliveries();
        return ResponseEntity.ok(deliveries);
    }

    // 특정 고객의 배송 목록 조회
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<DeliveryDTO>> getDeliveriesByCustomer(@PathVariable Long customerId) {
        List<DeliveryDTO> deliveries = deliveryService.getDeliveriesByCustomer(customerId);
        return ResponseEntity.ok(deliveries);
    }

}
