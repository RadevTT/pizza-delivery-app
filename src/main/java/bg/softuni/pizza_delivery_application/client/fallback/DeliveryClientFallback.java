package bg.softuni.pizza_delivery_application.client.fallback;

import bg.softuni.pizza_delivery_application.client.DeliveryClient;
import bg.softuni.pizza_delivery_application.client.dto.DeliveryCreateRequest;
import bg.softuni.pizza_delivery_application.client.dto.DeliveryResponse;
import bg.softuni.pizza_delivery_application.exception.DeliveryServiceUnavailableException;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeliveryClientFallback implements DeliveryClient {

    @Override
    public DeliveryResponse createDelivery(DeliveryCreateRequest request) {
        throw new DeliveryServiceUnavailableException();
    }

    @Override
    public DeliveryResponse getByOrderId(UUID orderId) {
        throw new DeliveryServiceUnavailableException();
    }

    @Override
    public DeliveryResponse dispatchDelivery(UUID id) {
        throw new DeliveryServiceUnavailableException();
    }

    @Override
    public DeliveryResponse completeDelivery(UUID id) {
        throw new DeliveryServiceUnavailableException();
    }

    @Override
    public void cancelDelivery(UUID id) {
        throw new DeliveryServiceUnavailableException();
    }
}