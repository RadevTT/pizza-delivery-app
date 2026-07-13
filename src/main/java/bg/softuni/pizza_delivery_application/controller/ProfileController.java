package bg.softuni.pizza_delivery_application.controller;

import bg.softuni.pizza_delivery_application.model.dto.ProfileEditDTO;
import bg.softuni.pizza_delivery_application.model.entity.User;
import bg.softuni.pizza_delivery_application.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication,
                          Model model) {

        User user = userService.findByUsername(authentication.getName());

        ProfileEditDTO profileEditDTO = new ProfileEditDTO();
        profileEditDTO.setEmail(user.getEmail());

        model.addAttribute("user", user);
        model.addAttribute("profileEditDTO", profileEditDTO);

        return "profile";
    }

    @PostMapping("/profile")
    public String updateProfile(
            @Valid
            @ModelAttribute("profileEditDTO")
            ProfileEditDTO profileEditDTO,

            BindingResult bindingResult,

            Authentication authentication,

            Model model) {

        User user =
                userService.findByUsername(authentication.getName());

        if (bindingResult.hasErrors()) {

            model.addAttribute("user", user);

            return "profile";
        }

        try {

            userService.updateProfile(
                    authentication.getName(),
                    profileEditDTO);

        } catch (IllegalArgumentException ex) {

            model.addAttribute("user", user);

            model.addAttribute(
                    "profileError",
                    ex.getMessage());

            return "profile";
        }

        return "redirect:/profile";
    }
}