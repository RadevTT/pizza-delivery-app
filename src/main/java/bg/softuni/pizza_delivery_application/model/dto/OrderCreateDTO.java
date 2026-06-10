package bg.softuni.pizza_delivery_application.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public class OrderCreateDTO {

    @NotNull
    private UUID pizzaId;

    @NotNull
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    public UUID getPizzaId() {
        return pizzaId;
    }

    public void setPizzaId(UUID pizzaId) {
        this.pizzaId = pizzaId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
