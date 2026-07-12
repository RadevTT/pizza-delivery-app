package bg.softuni.pizza_delivery_application.exception;

public class DeliveryServiceUnavailableException extends RuntimeException {

    public DeliveryServiceUnavailableException() {
        super("Delivery service is currently unavailable. Please try again later.");
    }
}
