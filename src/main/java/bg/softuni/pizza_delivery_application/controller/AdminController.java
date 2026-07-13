package bg.softuni.pizza_delivery_application.controller;

import bg.softuni.pizza_delivery_application.service.OrderService;
import bg.softuni.pizza_delivery_application.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final OrderService orderService;
    private final UserService userService;

    public AdminController(OrderService orderService,
                           UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
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

    @PostMapping("/orders/cancel/{id}")
    public String cancelOrder(
            @PathVariable UUID id,
            RedirectAttributes redirectAttributes) {

        orderService.cancelOrder(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Order cancelled successfully."
        );

        return "redirect:/admin/orders";
    }

    @GetMapping("/users")
    public String allUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());

        return "admin-users";
    }

    @PostMapping("/users/{id}/add-admin")
    public String addAdminRole(
            @PathVariable UUID id,
            RedirectAttributes redirectAttributes) {

        userService.addAdminRole(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Administrator role added successfully."
        );

        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/remove-admin")
    public String removeAdminRole(
            @PathVariable UUID id,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        userService.removeAdminRole(id, authentication.getName());

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Administrator role removed successfully."
        );

        return "redirect:/admin/users";
    }
}