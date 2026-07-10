package bg.softuni.delivery_service.model.entity;

import bg.softuni.delivery_service.model.enums.DeliveryStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "deliveries")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(nullable = false, unique = true)
    private UUID orderId;

    @NotBlank
    @Size(min = 5, max = 200)
    @Column(nullable = false, length = 200)
    private String address;

    @NotBlank
    @Pattern(
            regexp = "^\\+?[0-9 ]{7,20}$",
            message = "Phone number must contain between 7 and 20 digits"
    )
    @Column(nullable = false, length = 20)
    private String phoneNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime createdOn;

    private LocalDateTime dispatchedOn;

    private LocalDateTime deliveredOn;

    public Delivery() {
    }

    public UUID getId() {
        return id;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public LocalDateTime getDispatchedOn() {
        return dispatchedOn;
    }

    public LocalDateTime getDeliveredOn() {
        return deliveredOn;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setStatus(DeliveryStatus status) {
        this.status = status;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public void setDispatchedOn(LocalDateTime dispatchedOn) {
        this.dispatchedOn = dispatchedOn;
    }

    public void setDeliveredOn(LocalDateTime deliveredOn) {
        this.deliveredOn = deliveredOn;
    }
}