package bg.softuni.pizza_delivery_application.service;

import bg.softuni.pizza_delivery_application.model.dto.OrderCreateDTO;
import bg.softuni.pizza_delivery_application.model.dto.OrderDetailsDTO;
import bg.softuni.pizza_delivery_application.model.entity.Order;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    void createOrder(OrderCreateDTO dto, String username);

    List<Order> getOrdersForUser(String username);

    List<OrderDetailsDTO> getOrderDetails(UUID orderId);
}
