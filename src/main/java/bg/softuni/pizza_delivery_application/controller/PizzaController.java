package bg.softuni.pizza_delivery_application.controller;

import bg.softuni.pizza_delivery_application.model.dto.PizzaAddDTO;
import bg.softuni.pizza_delivery_application.model.dto.PizzaEditDTO;
import bg.softuni.pizza_delivery_application.service.PizzaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/pizzas")
public class PizzaController {

    private final PizzaService pizzaService;

    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping
    public String allPizzas(Model model) {

        model.addAttribute("pizzas",
                pizzaService.getAllPizzas());

        return "pizzas";
    }

    @GetMapping("/add")
    public String addPizza(Model model) {

        if (!model.containsAttribute("pizzaAddDTO")) {
            model.addAttribute("pizzaAddDTO",
                    new PizzaAddDTO());
        }

        return "pizza-add";
    }

    @PostMapping("/add")
    public String addPizza(
            @Valid PizzaAddDTO pizzaAddDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "pizza-add";
        }

        pizzaService.addPizza(pizzaAddDTO);

        return "redirect:/pizzas";
    }

    @PostMapping("/delete/{id}")
    public String deletePizza(@PathVariable UUID id) {

        pizzaService.deletePizza(id);

        return "redirect:/pizzas";
    }

    @GetMapping("/edit/{id}")
    public String editPizza(@PathVariable UUID id, Model model) {

        if (!model.containsAttribute("pizzaEditDTO")) {
            model.addAttribute("pizzaEditDTO", pizzaService.getPizzaForEdit(id));
        }

        return "pizza-edit";
    }

    @PostMapping("/edit")
    public String editPizza(
            @Valid PizzaEditDTO pizzaEditDTO,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "pizza-edit";
        }

        pizzaService.editPizza(pizzaEditDTO);

        return "redirect:/pizzas";
    }
}