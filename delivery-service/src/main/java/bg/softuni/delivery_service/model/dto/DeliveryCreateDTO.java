package bg.softuni.delivery_service.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public class DeliveryCreateDTO {

    @NotNull
    private UUID orderId;

    @NotBlank
    @Size(min = 5, max = 200)
    private String address;

    @NotBlank
    @Pattern(
            regexp = "^\\+?[0-9 ]{7,20}$",
            message = "Phone number must contain between 7 and 20 digits"
    )
    private String phoneNumber;

    public DeliveryCreateDTO() {
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