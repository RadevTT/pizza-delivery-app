package bg.softuni.pizza_delivery_application.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public class PizzaEditDTO {

    private UUID id;

    @NotBlank(message = "Pizza name is required")
    @Size(min = 3, max = 50, message = "Pizza name must be between 3 and 50 characters")
    private String name;

    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @NotBlank(message = "Description is required")
    @Size(min = 5, max = 255, message = "Description must be between 5 and 255 characters")
    private String description;

    private String imageUrl;

    public PizzaEditDTO() {
    }

    public UUID getId() {
        return id;
    }

    public PizzaEditDTO setId(UUID id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PizzaEditDTO setName(String name) {
        this.name = name;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public PizzaEditDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PizzaEditDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public PizzaEditDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}