package bg.softuni.delivery_service.exception;

public class DeliveryAlreadyExistsException extends RuntimeException {

    public DeliveryAlreadyExistsException() {
        super("Delivery already exists for this order.");
    }
}