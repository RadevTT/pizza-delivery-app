package bg.softuni.pizza_delivery_application.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public String handleUsernameAlreadyExists(
            UsernameAlreadyExistsException ex,
            Model model) {

        model.addAttribute("errorMessage", ex.getMessage());

        return "error";
    }

    @ExceptionHandler(DeliveryServiceUnavailableException.class)
    public String handleDeliveryServiceUnavailable(
            DeliveryServiceUnavailableException exception,
            Model model) {

        model.addAttribute("errorMessage", exception.getMessage());

        return "error";
    }

    @ExceptionHandler(AccessDeniedException.class)
    public String handleAccessDenied(
            AccessDeniedException ex,
            Model model) {

        model.addAttribute(
                "errorMessage",
                "Access denied. You do not have permission to access this page."
        );

        return "error";
    }

    @ExceptionHandler(RuntimeException.class)
    public String handleRuntimeException(
            RuntimeException ex,
            Model model) {

        model.addAttribute("errorMessage", ex.getMessage());

        return "error";
    }
}