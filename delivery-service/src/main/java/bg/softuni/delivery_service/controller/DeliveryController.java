package bg.softuni.delivery_service.controller;

import bg.softuni.delivery_service.model.dto.DeliveryCreateDTO;
import bg.softuni.delivery_service.model.dto.DeliveryResponseDTO;
import bg.softuni.delivery_service.service.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping
    public ResponseEntity<DeliveryResponseDTO> createDelivery(
            @Valid @RequestBody DeliveryCreateDTO deliveryCreateDTO) {

        DeliveryResponseDTO createdDelivery =
                deliveryService.createDelivery(deliveryCreateDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdDelivery);
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<DeliveryResponseDTO> getByOrderId(
            @PathVariable UUID orderId) {

        return ResponseEntity.ok(
                deliveryService.getByOrderId(orderId)
        );
    }

    @PutMapping("/{id}/dispatch")
    public ResponseEntity<DeliveryResponseDTO> dispatchDelivery(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                deliveryService.dispatchDelivery(id)
        );
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<DeliveryResponseDTO> completeDelivery(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                deliveryService.completeDelivery(id)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelDelivery(
            @PathVariable UUID id) {

        deliveryService.cancelDelivery(id);

        return ResponseEntity.noContent().build();
    }
}