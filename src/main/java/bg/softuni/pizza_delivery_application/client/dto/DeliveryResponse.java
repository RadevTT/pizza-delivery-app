package bg.softuni.pizza_delivery_application.client.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class DeliveryResponse {

    private UUID id;
    private UUID orderId;
    private String address;
    private String phoneNumber;
    private String status;
    private LocalDateTime createdOn;
    private LocalDateTime dispatchedOn;
    private LocalDateTime deliveredOn;

    public DeliveryResponse() {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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