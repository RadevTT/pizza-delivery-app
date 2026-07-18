package bg.softuni.pizza_delivery_application.service.impl;

import bg.softuni.pizza_delivery_application.exception.PizzaNotFoundException;
import bg.softuni.pizza_delivery_application.model.dto.PizzaAddDTO;
import bg.softuni.pizza_delivery_application.model.dto.PizzaEditDTO;
import bg.softuni.pizza_delivery_application.model.entity.Pizza;
import bg.softuni.pizza_delivery_application.repository.PizzaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PizzaServiceImplTest {

    @Mock
    private PizzaRepository pizzaRepository;

    private PizzaServiceImpl pizzaService;

    private UUID pizzaId;
    private Pizza pizza;

    @BeforeEach
    void setUp() {
        pizzaService = new PizzaServiceImpl(pizzaRepository);

        pizzaId = UUID.randomUUID();

        pizza = new Pizza();
        pizza.setId(pizzaId);
        pizza.setName("Margherita");
        pizza.setPrice(new BigDecimal("12.00"));
        pizza.setDescription("Tomato sauce and mozzarella");
        pizza.setImageUrl("https://example.com/margherita.jpg");
    }

    @Test
    void addPizza_ShouldSaveMappedPizza() {

        PizzaAddDTO dto = new PizzaAddDTO()
                .setName("Pepperoni")
                .setPrice(new BigDecimal("15.50"))
                .setDescription("Pepperoni and mozzarella")
                .setImageUrl("https://example.com/pepperoni.jpg");

        pizzaService.addPizza(dto);

        ArgumentCaptor<Pizza> pizzaCaptor =
                ArgumentCaptor.forClass(Pizza.class);

        verify(pizzaRepository).save(pizzaCaptor.capture());

        Pizza savedPizza = pizzaCaptor.getValue();

        assertEquals(dto.getName(), savedPizza.getName());
        assertEquals(dto.getPrice(), savedPizza.getPrice());
        assertEquals(dto.getDescription(), savedPizza.getDescription());
        assertEquals(dto.getImageUrl(), savedPizza.getImageUrl());
    }

    @Test
    void getAllPizzas_ShouldReturnAllPizzas() {

        when(pizzaRepository.findAll())
                .thenReturn(List.of(pizza));

        List<Pizza> result = pizzaService.getAllPizzas();

        assertEquals(1, result.size());
        assertEquals(pizza, result.get(0));

        verify(pizzaRepository).findAll();
    }

    @Test
    void findById_WhenPizzaExists_ShouldReturnPizza() {

        when(pizzaRepository.findById(pizzaId))
                .thenReturn(Optional.of(pizza));

        Pizza result = pizzaService.findById(pizzaId);

        assertEquals(pizzaId, result.getId());
        assertEquals("Margherita", result.getName());

        verify(pizzaRepository).findById(pizzaId);
    }

    @Test
    void findById_WhenPizzaDoesNotExist_ShouldThrowException() {

        when(pizzaRepository.findById(pizzaId))
                .thenReturn(Optional.empty());

        PizzaNotFoundException exception = assertThrows(
                PizzaNotFoundException.class,
                () -> pizzaService.findById(pizzaId)
        );

        assertEquals("Pizza not found", exception.getMessage());

        verify(pizzaRepository).findById(pizzaId);
    }

    @Test
    void deletePizza_WhenPizzaExists_ShouldDeletePizza() {

        when(pizzaRepository.findById(pizzaId))
                .thenReturn(Optional.of(pizza));

        pizzaService.deletePizza(pizzaId);

        verify(pizzaRepository).findById(pizzaId);
        verify(pizzaRepository).delete(pizza);
    }

    @Test
    void deletePizza_WhenPizzaDoesNotExist_ShouldThrowException() {

        when(pizzaRepository.findById(pizzaId))
                .thenReturn(Optional.empty());

        assertThrows(
                PizzaNotFoundException.class,
                () -> pizzaService.deletePizza(pizzaId)
        );

        verify(pizzaRepository).findById(pizzaId);
        verify(pizzaRepository, never()).delete(any());
    }

    @Test
    void getPizzaForEdit_WhenPizzaExists_ShouldMapToDTO() {

        when(pizzaRepository.findById(pizzaId))
                .thenReturn(Optional.of(pizza));

        PizzaEditDTO result =
                pizzaService.getPizzaForEdit(pizzaId);

        assertEquals(pizza.getId(), result.getId());
        assertEquals(pizza.getName(), result.getName());
        assertEquals(pizza.getPrice(), result.getPrice());
        assertEquals(pizza.getDescription(), result.getDescription());
        assertEquals(pizza.getImageUrl(), result.getImageUrl());

        verify(pizzaRepository).findById(pizzaId);
    }

    @Test
    void getPizzaForEdit_WhenPizzaDoesNotExist_ShouldThrowException() {

        when(pizzaRepository.findById(pizzaId))
                .thenReturn(Optional.empty());

        assertThrows(
                PizzaNotFoundException.class,
                () -> pizzaService.getPizzaForEdit(pizzaId)
        );

        verify(pizzaRepository).findById(pizzaId);
    }

    @Test
    void editPizza_WhenPizzaExists_ShouldUpdateAndSavePizza() {

        PizzaEditDTO dto = new PizzaEditDTO()
                .setId(pizzaId)
                .setName("Updated Margherita")
                .setPrice(new BigDecimal("13.50"))
                .setDescription("Updated description")
                .setImageUrl("https://example.com/updated.jpg");

        when(pizzaRepository.findById(pizzaId))
                .thenReturn(Optional.of(pizza));

        pizzaService.editPizza(dto);

        assertEquals(dto.getName(), pizza.getName());
        assertEquals(dto.getPrice(), pizza.getPrice());
        assertEquals(dto.getDescription(), pizza.getDescription());
        assertEquals(dto.getImageUrl(), pizza.getImageUrl());

        verify(pizzaRepository).findById(pizzaId);
        verify(pizzaRepository).save(pizza);
    }

    @Test
    void editPizza_WhenPizzaDoesNotExist_ShouldThrowException() {

        PizzaEditDTO dto = new PizzaEditDTO()
                .setId(pizzaId)
                .setName("Updated Pizza")
                .setPrice(new BigDecimal("13.50"))
                .setDescription("Updated description")
                .setImageUrl("https://example.com/updated.jpg");

        when(pizzaRepository.findById(pizzaId))
                .thenReturn(Optional.empty());

        assertThrows(
                PizzaNotFoundException.class,
                () -> pizzaService.editPizza(dto)
        );

        verify(pizzaRepository).findById(pizzaId);
        verify(pizzaRepository, never()).save(any());
    }
}