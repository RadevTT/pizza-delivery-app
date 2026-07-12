package bg.softuni.pizza_delivery_application.client;

import bg.softuni.pizza_delivery_application.client.dto.DeliveryCreateRequest;
import bg.softuni.pizza_delivery_application.client.dto.DeliveryResponse;
import bg.softuni.pizza_delivery_application.client.fallback.DeliveryClientFallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(
        name = "delivery-service",
        url = "${delivery.service.url}",
        fallback = DeliveryClientFallback.class
)
public interface DeliveryClient {

    @PostMapping("/api/deliveries")
    DeliveryResponse createDelivery(
            @RequestBody DeliveryCreateRequest request);

    @GetMapping("/api/deliveries/order/{orderId}")
    DeliveryResponse getByOrderId(
            @PathVariable("orderId") UUID orderId);

    @PutMapping("/api/deliveries/{id}/dispatch")
    DeliveryResponse dispatchDelivery(
            @PathVariable("id") UUID id);

    @PutMapping("/api/deliveries/{id}/complete")
    DeliveryResponse completeDelivery(
            @PathVariable("id") UUID id);

    @DeleteMapping("/api/deliveries/{id}")
    void cancelDelivery(
            @PathVariable("id") UUID id);
}