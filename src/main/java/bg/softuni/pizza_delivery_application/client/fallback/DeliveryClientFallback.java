package bg.softuni.pizza_delivery_application.client.fallback;

import bg.softuni.pizza_delivery_application.client.DeliveryClient;
import bg.softuni.pizza_delivery_application.client.dto.DeliveryCreateRequest;
import bg.softuni.pizza_delivery_application.client.dto.DeliveryResponse;
import bg.softuni.pizza_delivery_application.exception.DeliveryServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class DeliveryClientFallback implements DeliveryClient {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DeliveryClientFallback.class);

    @Override
    public DeliveryResponse createDelivery(DeliveryCreateRequest request) {

        LOGGER.error(
                "Delivery service unavailable while creating delivery: orderId={}",
                request.getOrderId()
        );

        throw new DeliveryServiceUnavailableException();
    }

    @Override
    public DeliveryResponse getByOrderId(UUID orderId) {

        LOGGER.error(
                "Delivery service unavailable while finding delivery: orderId={}",
                orderId
        );

        throw new DeliveryServiceUnavailableException();
    }

    @Override
    public DeliveryResponse dispatchDelivery(UUID id) {

        LOGGER.error(
                "Delivery service unavailable while dispatching delivery: deliveryId={}",
                id
        );

        throw new DeliveryServiceUnavailableException();
    }

    @Override
    public DeliveryResponse completeDelivery(UUID id) {

        LOGGER.error(
                "Delivery service unavailable while completing delivery: deliveryId={}",
                id
        );

        throw new DeliveryServiceUnavailableException();
    }

    @Override
    public void cancelDelivery(UUID id) {

        LOGGER.error(
                "Delivery service unavailable while cancelling delivery: deliveryId={}",
                id
        );

        throw new DeliveryServiceUnavailableException();
    }
}