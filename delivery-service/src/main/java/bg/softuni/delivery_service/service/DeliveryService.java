package bg.softuni.delivery_service.service;

import bg.softuni.delivery_service.model.dto.DeliveryCreateDTO;
import bg.softuni.delivery_service.model.dto.DeliveryResponseDTO;

import java.util.UUID;

public interface DeliveryService {

    DeliveryResponseDTO createDelivery(DeliveryCreateDTO deliveryCreateDTO);

    DeliveryResponseDTO getByOrderId(UUID orderId);

    DeliveryResponseDTO dispatchDelivery(UUID id);

    DeliveryResponseDTO completeDelivery(UUID id);

    void cancelDelivery(UUID id);
}