package com.itwillbs.ilkwangtech.sales.service;

import com.itwillbs.ilkwangtech.sales.dto.DeliveryDTO;
import java.util.List;

public interface DeliveryService {
    List<DeliveryDTO> getAllDeliveries();
    DeliveryDTO getDeliveryById(Long id);
    DeliveryDTO createDelivery(DeliveryDTO deliveryDTO);
    DeliveryDTO updateDelivery(Long id, DeliveryDTO deliveryDTO);
    void deleteDelivery(Long id);
    DeliveryDTO completeDelivery(Long id);
    DeliveryDTO startDelivery(Long id);
    List<DeliveryDTO> getPendingDeliveries();
    List<DeliveryDTO> getActiveDeliveries();
    List<DeliveryDTO> getDeliveriesByCustomer(Long customerId);
}
