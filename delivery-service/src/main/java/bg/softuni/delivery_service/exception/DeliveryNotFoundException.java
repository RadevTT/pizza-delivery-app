package bg.softuni.delivery_service.exception;

public class DeliveryNotFoundException extends RuntimeException {

    public DeliveryNotFoundException() {
        super("Delivery not found.");
    }
}