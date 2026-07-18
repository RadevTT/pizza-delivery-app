package bg.softuni.delivery_service.exception;

public class InvalidDeliveryStatusTransitionException
        extends RuntimeException {

    public InvalidDeliveryStatusTransitionException() {
        super("Invalid delivery status transition.");
    }
}