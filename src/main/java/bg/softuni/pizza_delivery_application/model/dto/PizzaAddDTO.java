package bg.softuni.pizza_delivery_application.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public class PizzaAddDTO {

    @NotBlank(message = "Pizza name is required")
    @Size(min = 3, max = 50, message = "Pizza name must be between 3 and 50 characters")
    private String name;

    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal price;

    @NotBlank(message = "Description is required")
    @Size(min = 5, max = 255, message = "Description must be between 5 and 255 characters")
    private String description;

    private String imageUrl;

    public PizzaAddDTO() {
    }

    public String getName() {
        return name;
    }

    public PizzaAddDTO setName(String name) {
        this.name = name;
        return this;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public PizzaAddDTO setPrice(BigDecimal price) {
        this.price = price;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PizzaAddDTO setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public PizzaAddDTO setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }
}