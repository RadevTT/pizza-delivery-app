package bg.softuni.pizza_delivery_application.controller;

import bg.softuni.pizza_delivery_application.model.entity.User;
import bg.softuni.pizza_delivery_application.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {

        User user = userService.findByUsername(authentication.getName());

        model.addAttribute("user", user);

        return "profile";
    }
}
