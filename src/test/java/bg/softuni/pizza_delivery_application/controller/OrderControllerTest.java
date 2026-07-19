package bg.softuni.pizza_delivery_application.controller;

import bg.softuni.pizza_delivery_application.model.dto.OrderCreateDTO;
import bg.softuni.pizza_delivery_application.model.entity.Pizza;
import bg.softuni.pizza_delivery_application.service.OrderService;
import bg.softuni.pizza_delivery_application.service.PizzaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private PizzaService pizzaService;

    @Test
    @WithMockUser(username = "jimmy")
    void myOrders_ShouldReturnOrdersView() throws Exception {

        when(orderService.getOrdersForUser("jimmy"))
                .thenReturn(List.of());

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(view().name("orders"))
                .andExpect(model().attributeExists("orders"));

        verify(orderService).getOrdersForUser("jimmy");
    }

    @Test
    @WithMockUser(username = "jimmy")
    void createOrderPage_ShouldReturnOrderCreateView() throws Exception {

        UUID pizzaId = UUID.randomUUID();

        Pizza pizza = new Pizza();
        pizza.setId(pizzaId);
        pizza.setName("Margherita");

        when(pizzaService.findById(pizzaId))
                .thenReturn(pizza);

        mockMvc.perform(get("/orders/create/{pizzaId}", pizzaId))
                .andExpect(status().isOk())
                .andExpect(view().name("order-create"))
                .andExpect(model().attributeExists("orderCreateDTO"))
                .andExpect(model().attributeExists("pizza"))
                .andExpect(model().attribute(
                        "orderCreateDTO",
                        org.hamcrest.Matchers.hasProperty("pizzaId",
                                org.hamcrest.Matchers.is(pizzaId))
                ))
                .andExpect(model().attribute(
                        "orderCreateDTO",
                        org.hamcrest.Matchers.hasProperty("quantity",
                                org.hamcrest.Matchers.is(1))
                ));

        verify(pizzaService).findById(pizzaId);
    }

    @Test
    @WithMockUser(username = "jimmy")
    void createOrder_WithValidData_ShouldCreateOrderAndRedirect() throws Exception {

        UUID pizzaId = UUID.randomUUID();

        mockMvc.perform(post("/orders/create")
                        .with(csrf())
                        .param("pizzaId", pizzaId.toString())
                        .param("quantity", "2")
                        .param("address", "Sofia, Vitosha 10")
                        .param("phoneNumber", "+359 888 123 456"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));

        verify(orderService).createOrder(
                any(OrderCreateDTO.class),
                eq("jimmy")
        );
    }

    @Test
    @WithMockUser(username = "jimmy")
    void orderDetails_ShouldReturnOrderDetailsView() throws Exception {

        UUID orderId = UUID.randomUUID();

        when(orderService.getOrderDetails(orderId))
                .thenReturn(List.of());

        mockMvc.perform(get("/orders/{id}", orderId))
                .andExpect(status().isOk())
                .andExpect(view().name("order-details"))
                .andExpect(model().attributeExists("details"))
                .andExpect(model().attribute("details", List.of()));

        verify(orderService).getOrderDetails(orderId);
    }
}