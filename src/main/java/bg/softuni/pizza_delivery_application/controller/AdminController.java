package bg.softuni.pizza_delivery_application.controller;

import bg.softuni.pizza_delivery_application.service.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final OrderService orderService;

    public AdminController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/orders")
    public String allOrders(Model model) {
        model.addAttribute("orders", orderService.getAllOrders());
        return "admin-orders";
    }

    @PostMapping("/orders/change-status/{id}")
    public String changeStatus(@PathVariable UUID id) {
        orderService.changeStatus(id);
        return "redirect:/admin/orders";
    }
}