package bg.softuni.pizza_delivery_application.scheduler;

import bg.softuni.pizza_delivery_application.service.OrderService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderScheduler {

    private final OrderService orderService;

    public OrderScheduler(OrderService orderService) {
        this.orderService = orderService;
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void cancelExpiredPendingOrders() {
        orderService.cancelExpiredPendingOrders();
    }

    @Scheduled(
            fixedDelay = 300000,
            initialDelay = 60000
    )
    public void dispatchDelayedPreparingOrders() {
        orderService.dispatchDelayedPreparingOrders();
    }
}