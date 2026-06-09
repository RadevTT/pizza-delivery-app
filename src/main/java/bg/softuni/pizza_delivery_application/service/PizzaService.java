package bg.softuni.pizza_delivery_application.service;

import bg.softuni.pizza_delivery_application.model.dto.PizzaAddDTO;
import bg.softuni.pizza_delivery_application.model.entity.Pizza;

import java.util.List;
import java.util.UUID;

public interface PizzaService {

    void addPizza(PizzaAddDTO dto);

    List<Pizza> getAllPizzas();

    Pizza findById(UUID id);

    void deletePizza(UUID id);
}
