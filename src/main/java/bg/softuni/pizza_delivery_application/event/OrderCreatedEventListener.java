package bg.softuni.pizza_delivery_application.event;

import bg.softuni.pizza_delivery_application.model.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class OrderCreatedEventListener {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(OrderCreatedEventListener.class);

    @EventListener
    public void handleOrderCreated(OrderCreatedEvent event) {

        Order order = event.getOrder();

        LOGGER.info(
                "Order created event received. Order id: {}, user: {}",
                order.getId(),
                order.getUser().getUsername()
        );
    }
}