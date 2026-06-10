package bg.softuni.pizza_delivery_application.controller;

import bg.softuni.pizza_delivery_application.model.dto.OrderCreateDTO;
import bg.softuni.pizza_delivery_application.service.OrderService;
import bg.softuni.pizza_delivery_application.service.PizzaService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;
    private final PizzaService pizzaService;

    public OrderController(OrderService orderService,
                           PizzaService pizzaService) {
        this.orderService = orderService;
        this.pizzaService = pizzaService;
    }

    @GetMapping
    public String myOrders(Model model, Authentication authentication) {

        model.addAttribute("orders",
                orderService.getOrdersForUser(authentication.getName()));

        return "orders";
    }

    @GetMapping("/create/{pizzaId}")
    public String createOrder(@PathVariable UUID pizzaId, Model model) {

        OrderCreateDTO dto = new OrderCreateDTO();
        dto.setPizzaId(pizzaId);
        dto.setQuantity(1);

        model.addAttribute("orderCreateDTO", dto);
        model.addAttribute("pizza", pizzaService.findById(pizzaId));

        return "order-create";
    }

    @PostMapping("/create")
    public String createOrder(
            @Valid OrderCreateDTO orderCreateDTO,
            BindingResult bindingResult,
            Authentication authentication,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("pizza", pizzaService.findById(orderCreateDTO.getPizzaId()));
            return "order-create";
        }

        orderService.createOrder(orderCreateDTO, authentication.getName());

        return "redirect:/orders";
    }
}