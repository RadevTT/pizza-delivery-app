package bg.softuni.pizza_delivery_application.service.impl;

import bg.softuni.pizza_delivery_application.model.entity.OrderItem;
import bg.softuni.pizza_delivery_application.repository.OrderItemRepository;
import bg.softuni.pizza_delivery_application.service.OrderItemService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    @Override
    public void save(OrderItem orderItem) {
        orderItemRepository.save(orderItem);
    }

    @Override
    public List<OrderItem> findAllByOrderId(UUID orderId) {
        return orderItemRepository.findAllByOrderId(orderId);
    }
}