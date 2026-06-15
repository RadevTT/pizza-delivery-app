package bg.softuni.pizza_delivery_application.service;

import bg.softuni.pizza_delivery_application.model.entity.OrderItem;

import java.util.List;
import java.util.UUID;

public interface OrderItemService {

    void save(OrderItem orderItem);

    List<OrderItem> findAllByOrderId(UUID orderId);
}