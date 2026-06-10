package bg.softuni.pizza_delivery_application.model.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    private Order order;

    @ManyToOne
    private Pizza pizza;

    @Column(nullable = false)
    private Integer quantity;

    public OrderItem() {
    }

    public UUID getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Pizza getPizza() {
        return pizza;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public void setPizza(Pizza pizza) {
        this.pizza = pizza;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
