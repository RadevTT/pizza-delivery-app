package bg.softuni.pizza_delivery_application.service.impl;

import bg.softuni.pizza_delivery_application.exception.PizzaNotFoundException;
import bg.softuni.pizza_delivery_application.model.dto.PizzaAddDTO;
import bg.softuni.pizza_delivery_application.model.dto.PizzaEditDTO;
import bg.softuni.pizza_delivery_application.model.entity.Pizza;
import bg.softuni.pizza_delivery_application.repository.PizzaRepository;
import bg.softuni.pizza_delivery_application.service.PizzaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PizzaServiceImpl implements PizzaService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(PizzaServiceImpl.class);

    private final PizzaRepository pizzaRepository;

    public PizzaServiceImpl(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    @Override
    @CacheEvict(value = "pizzas", allEntries = true)
    public void addPizza(PizzaAddDTO dto) {

        Pizza pizza = new Pizza();

        pizza.setName(dto.getName());
        pizza.setPrice(dto.getPrice());
        pizza.setDescription(dto.getDescription());
        pizza.setImageUrl(dto.getImageUrl());

        pizzaRepository.save(pizza);

        LOGGER.info(
                "Pizza added successfully: pizzaId={}, name={}, price={}",
                pizza.getId(),
                pizza.getName(),
                pizza.getPrice()
        );
    }

    @Override
    @Cacheable("pizzas")
    public List<Pizza> getAllPizzas() {
        return pizzaRepository.findAll();
    }

    @Override
    @Cacheable(value = "pizzaById", key = "#id")
    public Pizza findById(UUID id) {
        return pizzaRepository.findById(id)
                .orElseThrow(() -> new PizzaNotFoundException("Pizza not found"));
    }

    @Override
    @CacheEvict(value = {"pizzas", "pizzaById"}, allEntries = true)
    public void deletePizza(UUID id) {

        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new PizzaNotFoundException("Pizza not found"));

        pizzaRepository.delete(pizza);

        LOGGER.info(
                "Pizza deleted successfully: pizzaId={}, name={}",
                pizza.getId(),
                pizza.getName()
        );
    }

    @Override
    public PizzaEditDTO getPizzaForEdit(UUID id) {

        Pizza pizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new PizzaNotFoundException("Pizza not found"));

        return new PizzaEditDTO()
                .setId(pizza.getId())
                .setName(pizza.getName())
                .setPrice(pizza.getPrice())
                .setDescription(pizza.getDescription())
                .setImageUrl(pizza.getImageUrl());
    }

    @Override
    @CacheEvict(value = {"pizzas", "pizzaById"}, allEntries = true)
    public void editPizza(PizzaEditDTO dto) {

        Pizza pizza = pizzaRepository.findById(dto.getId())
                .orElseThrow(() -> new PizzaNotFoundException("Pizza not found"));

        pizza.setName(dto.getName());
        pizza.setPrice(dto.getPrice());
        pizza.setDescription(dto.getDescription());
        pizza.setImageUrl(dto.getImageUrl());

        pizzaRepository.save(pizza);

        LOGGER.info(
                "Pizza updated successfully: pizzaId={}, name={}, price={}",
                pizza.getId(),
                pizza.getName(),
                pizza.getPrice()
        );
    }
}