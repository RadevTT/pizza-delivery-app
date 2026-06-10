package bg.softuni.pizza_delivery_application.model.entity;

import bg.softuni.pizza_delivery_application.model.enums.OrderStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    @Column(nullable = false)
    private LocalDateTime createdOn;

    @ManyToOne
    private User user;

    public Order() {
    }

    public UUID getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public User getUser() {
        return user;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public void setUser(User user) {
        this.user = user;
    }
}