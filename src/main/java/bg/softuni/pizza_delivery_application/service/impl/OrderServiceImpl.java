package bg.softuni.pizza_delivery_application.service.impl;

import bg.softuni.pizza_delivery_application.model.dto.OrderCreateDTO;
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
}
