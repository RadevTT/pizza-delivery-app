package bg.softuni.pizza_delivery_application.client.dto;

import java.util.UUID;

public class DeliveryCreateRequest {

    private UUID orderId;
    private String address;
    private String phoneNumber;

    public DeliveryCreateRequest() {
    }

    public DeliveryCreateRequest(
            UUID orderId,
            String address,
            String phoneNumber) {

        this.orderId = orderId;
        this.address = address;
        this.phoneNumber = phoneNumber;
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
}