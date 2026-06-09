package bg.softuni.pizza_delivery_application.service.impl;

import bg.softuni.pizza_delivery_application.model.dto.PizzaAddDTO;
import bg.softuni.pizza_delivery_application.model.entity.Pizza;
import bg.softuni.pizza_delivery_application.repository.PizzaRepository;
import bg.softuni.pizza_delivery_application.service.PizzaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PizzaServiceImpl implements PizzaService {

    private final PizzaRepository pizzaRepository;

    public PizzaServiceImpl(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    @Override
    public void addPizza(PizzaAddDTO dto) {

        Pizza pizza = new Pizza();

        pizza.setName(dto.getName());
        pizza.setPrice(dto.getPrice());
        pizza.setDescription(dto.getDescription());
        pizza.setImageUrl(dto.getImageUrl());

        pizzaRepository.save(pizza);
    }

    @Override
    public List<Pizza> getAllPizzas() {
        return pizzaRepository.findAll();
    }

    @Override
    public Pizza findById(UUID id) {
        return pizzaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pizza not found"));
    }

    @Override
    public void deletePizza(UUID id) {
        pizzaRepository.deleteById(id);
    }
}