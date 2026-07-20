package bg.softuni.pizza_delivery_application.controller;

import bg.softuni.pizza_delivery_application.model.dto.PizzaAddDTO;
import bg.softuni.pizza_delivery_application.model.dto.PizzaEditDTO;
import bg.softuni.pizza_delivery_application.model.entity.Pizza;
import bg.softuni.pizza_delivery_application.service.PizzaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PizzaController.class)
class PizzaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PizzaService pizzaService;

    @Test
    @WithMockUser
    void allPizzas_ShouldReturnPizzasView() throws Exception {

        when(pizzaService.getAllPizzas())
                .thenReturn(List.of(new Pizza()));

        mockMvc.perform(get("/pizzas"))
                .andExpect(status().isOk())
                .andExpect(view().name("pizzas"))
                .andExpect(model().attributeExists("pizzas"));

        verify(pizzaService).getAllPizzas();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addPizzaPage_ShouldReturnPizzaAddView() throws Exception {

        mockMvc.perform(get("/pizzas/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("pizza-add"))
                .andExpect(model().attributeExists("pizzaAddDTO"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addPizza_WithValidData_ShouldAddPizzaAndRedirect() throws Exception {

        mockMvc.perform(post("/pizzas/add")
                        .with(csrf())
                        .param("name", "Margherita")
                        .param("price", "12.50")
                        .param("description", "Tomato sauce and mozzarella")
                        .param("imageUrl", "https://example.com/pizza.jpg"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pizzas"));

        verify(pizzaService).addPizza(any(PizzaAddDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addPizza_WithInvalidData_ShouldReturnPizzaAddView() throws Exception {

        mockMvc.perform(post("/pizzas/add")
                        .with(csrf())
                        .param("name", "")
                        .param("price", "0")
                        .param("description", "bad"))
                .andExpect(status().isOk())
                .andExpect(view().name("pizza-add"))
                .andExpect(model().attributeHasFieldErrors(
                        "pizzaAddDTO",
                        "name",
                        "price",
                        "description"
                ));

        verify(pizzaService, never()).addPizza(any(PizzaAddDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletePizza_ShouldDeletePizzaAndRedirect() throws Exception {

        UUID pizzaId = UUID.randomUUID();

        mockMvc.perform(post("/pizzas/delete/{id}", pizzaId)
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pizzas"));

        verify(pizzaService).deletePizza(pizzaId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editPizzaPage_ShouldReturnPizzaEditView() throws Exception {

        UUID pizzaId = UUID.randomUUID();

        PizzaEditDTO pizzaEditDTO = new PizzaEditDTO()
                .setId(pizzaId)
                .setName("Pepperoni")
                .setPrice(BigDecimal.valueOf(14.50))
                .setDescription("Pepperoni and mozzarella")
                .setImageUrl("https://example.com/pepperoni.jpg");

        when(pizzaService.getPizzaForEdit(pizzaId))
                .thenReturn(pizzaEditDTO);

        mockMvc.perform(get("/pizzas/edit/{id}", pizzaId))
                .andExpect(status().isOk())
                .andExpect(view().name("pizza-edit"))
                .andExpect(model().attributeExists("pizzaEditDTO"))
                .andExpect(model().attribute(
                        "pizzaEditDTO",
                        pizzaEditDTO
                ));

        verify(pizzaService).getPizzaForEdit(pizzaId);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editPizza_WithValidData_ShouldEditPizzaAndRedirect() throws Exception {

        UUID pizzaId = UUID.randomUUID();

        mockMvc.perform(post("/pizzas/edit")
                        .with(csrf())
                        .param("id", pizzaId.toString())
                        .param("name", "Quattro Formaggi")
                        .param("price", "15.90")
                        .param("description", "Pizza with four kinds of cheese")
                        .param("imageUrl", "https://example.com/cheese.jpg"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/pizzas"));

        verify(pizzaService).editPizza(any(PizzaEditDTO.class));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void editPizza_WithInvalidData_ShouldReturnPizzaEditView() throws Exception {

        UUID pizzaId = UUID.randomUUID();

        mockMvc.perform(post("/pizzas/edit")
                        .with(csrf())
                        .param("id", pizzaId.toString())
                        .param("name", "")
                        .param("price", "0")
                        .param("description", "bad"))
                .andExpect(status().isOk())
                .andExpect(view().name("pizza-edit"))
                .andExpect(model().attributeHasFieldErrors(
                        "pizzaEditDTO",
                        "name",
                        "price",
                        "description"
                ));

        verify(pizzaService, never()).editPizza(any(PizzaEditDTO.class));
    }
}