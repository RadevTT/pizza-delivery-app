package bg.softuni.pizza_delivery_application.service.impl;

import bg.softuni.pizza_delivery_application.model.dto.OrderCreateDTO;
import bg.softuni.pizza_delivery_application.model.dto.OrderDetailsDTO;
import bg.softuni.pizza_delivery_application.model.entity.Order;
import bg.softuni.pizza_delivery_application.model.entity.OrderItem;
import bg.softuni.pizza_delivery_application.model.entity.Pizza;
import bg.softuni.pizza_delivery_application.model.entity.User;
import bg.softuni.pizza_delivery_application.model.enums.OrderStatus;
import bg.softuni.pizza_delivery_application.repository.OrderItemRepository;
import bg.softuni.pizza_delivery_application.repository.OrderRepository;
import bg.softuni.pizza_delivery_application.repository.PizzaRepository;
import bg.softuni.pizza_delivery_application.repository.UserRepository;
import bg.softuni.pizza_delivery_application.service.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PizzaRepository pizzaRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemRepository orderItemRepository,
                            PizzaRepository pizzaRepository,
                            UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.pizzaRepository = pizzaRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createOrder(OrderCreateDTO dto, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Pizza pizza = pizzaRepository.findById(dto.getPizzaId())
                .orElseThrow(() -> new RuntimeException("Pizza not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedOn(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        OrderItem item = new OrderItem();
        item.setOrder(savedOrder);
        item.setPizza(pizza);
        item.setQuantity(dto.getQuantity());

        orderItemRepository.save(item);
    }

    @Override
    public List<Order> getOrdersForUser(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return orderRepository.findAllByUserOrderByCreatedOnDesc(user);
    }

    @Override
    public List<OrderDetailsDTO> getOrderDetails(UUID orderId) {

        return orderItemRepository.findAllByOrderId(orderId)
                .stream()
                .map(item -> {

                    OrderDetailsDTO dto = new OrderDetailsDTO();

                    dto.setOrderId(item.getOrder().getId());

                    dto.setPizzaName(item.getPizza().getName());

                    dto.setQuantity(item.getQuantity());

                    dto.setPrice(item.getPizza().getPrice());

                    dto.setTotal(
                            item.getPizza()
                                    .getPrice()
                                    .multiply(
                                            java.math.BigDecimal.valueOf(
                                                    item.getQuantity()
                                            )
                                    )
                    );

                    dto.setStatus(
                            item.getOrder()
                                    .getStatus()
                                    .name()
                    );

                    dto.setCreatedOn(
                            item.getOrder()
                                    .getCreatedOn()
                    );

                    return dto;

                })
                .toList();
    }
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void changeStatus(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        switch (order.getStatus()) {
            case PENDING -> order.setStatus(OrderStatus.PREPARING);
            case PREPARING -> order.setStatus(OrderStatus.DELIVERED);
            case DELIVERED -> order.setStatus(OrderStatus.DELIVERED);
            case CANCELLED -> order.setStatus(OrderStatus.CANCELLED);
        }

        orderRepository.save(order);
    }
}
