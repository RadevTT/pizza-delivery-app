package bg.softuni.pizza_delivery_application.event;

import bg.softuni.pizza_delivery_application.model.entity.Order;
import bg.softuni.pizza_delivery_application.model.entity.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertSame;

class OrderCreatedEventListenerTest {

    private final OrderCreatedEventListener eventListener =
            new OrderCreatedEventListener();

    @Test
    void orderCreatedEvent_ShouldReturnOrder() {
        Order order = createOrder();

        OrderCreatedEvent event = new OrderCreatedEvent(order);

        assertSame(order, event.getOrder());
    }

    @Test
    void handleOrderCreated_ShouldProcessEventWithoutException() {
        Order order = createOrder();
        OrderCreatedEvent event = new OrderCreatedEvent(order);

        assertDoesNotThrow(
                () -> eventListener.handleOrderCreated(event)
        );
    }

    private Order createOrder() {
        User user = new User();
        user.setUsername("jimmy");

        Order order = new Order();
        order.setUser(user);

        return order;
    }
}