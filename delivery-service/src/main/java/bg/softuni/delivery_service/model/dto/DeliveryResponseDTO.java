package bg.softuni.delivery_service.model.dto;

import bg.softuni.delivery_service.model.enums.DeliveryStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class DeliveryResponseDTO {

    private UUID id;
    private UUID orderId;
    private String address;
    private String phoneNumber;
    private DeliveryStatus status;
    private LocalDateTime createdOn;
    private LocalDateTime dispatchedOn;
    private LocalDateTime deliveredOn;

    public DeliveryResponseDTO() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public LocalDateTime getDispatchedOn() {
        return dispatchedOn;
    }

    public void setDispatchedOn(LocalDateTime dispatchedOn) {
        this.dispatchedOn = dispatchedOn;
    }

    public LocalDateTime getDeliveredOn() {
        return deliveredOn;
    }

    public void setDeliveredOn(LocalDateTime deliveredOn) {
        this.deliveredOn = deliveredOn;
    }
}