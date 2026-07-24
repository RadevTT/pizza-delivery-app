package bg.softuni.pizza_delivery_application.event;

import bg.softuni.pizza_delivery_application.model.entity.Order;

public class OrderCreatedEvent {

    private final Order order;

    public OrderCreatedEvent(Order order) {
        this.order = order;
    }

    public Order getOrder() {
        return order;
    }
}