package bg.softuni.pizza_delivery_application.service;

import bg.softuni.pizza_delivery_application.model.dto.OrderCreateDTO;
import bg.softuni.pizza_delivery_application.model.entity.Order;

import java.util.List;

public interface OrderService {

    void createOrder(OrderCreateDTO dto, String username);

    List<Order> getOrdersForUser(String username);
}
