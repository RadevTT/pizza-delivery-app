package bg.softuni.pizza_delivery_application.service.impl;

import bg.softuni.pizza_delivery_application.model.dto.PizzaAddDTO;
import bg.softuni.pizza_delivery_application.model.dto.PizzaEditDTO;
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

    @Override
    public PizzaEditDTO getPizzaForEdit(UUID id) {
        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pizza not found"));

        return new PizzaEditDTO()
                .setId(pizza.getId())
                .setName(pizza.getName())
                .setPrice(pizza.getPrice())
                .setDescription(pizza.getDescription())
                .setImageUrl(pizza.getImageUrl());
    }

    @Override
    public void editPizza(PizzaEditDTO dto) {
        Pizza pizza = pizzaRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("Pizza not found"));

        pizza.setName(dto.getName());
        pizza.setPrice(dto.getPrice());
        pizza.setDescription(dto.getDescription());
        pizza.setImageUrl(dto.getImageUrl());

        pizzaRepository.save(pizza);
    }
}