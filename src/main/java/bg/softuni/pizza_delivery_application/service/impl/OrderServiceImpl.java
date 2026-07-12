package bg.softuni.pizza_delivery_application.service.impl;

import bg.softuni.pizza_delivery_application.client.DeliveryClient;
import bg.softuni.pizza_delivery_application.client.dto.DeliveryCreateRequest;
import bg.softuni.pizza_delivery_application.client.dto.DeliveryResponse;
import bg.softuni.pizza_delivery_application.exception.OrderNotFoundException;
import bg.softuni.pizza_delivery_application.exception.PizzaNotFoundException;
import bg.softuni.pizza_delivery_application.exception.UserNotFoundException;
import bg.softuni.pizza_delivery_application.model.dto.OrderCreateDTO;
import bg.softuni.pizza_delivery_application.model.dto.OrderDetailsDTO;
import bg.softuni.pizza_delivery_application.model.entity.Order;
import bg.softuni.pizza_delivery_application.model.entity.OrderItem;
import bg.softuni.pizza_delivery_application.model.entity.Pizza;
import bg.softuni.pizza_delivery_application.model.entity.User;
import bg.softuni.pizza_delivery_application.model.enums.OrderStatus;
import bg.softuni.pizza_delivery_application.repository.OrderRepository;
import bg.softuni.pizza_delivery_application.repository.PizzaRepository;
import bg.softuni.pizza_delivery_application.repository.UserRepository;
import bg.softuni.pizza_delivery_application.service.OrderItemService;
import bg.softuni.pizza_delivery_application.service.OrderService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemService orderItemService;
    private final PizzaRepository pizzaRepository;
    private final UserRepository userRepository;
    private final DeliveryClient deliveryClient;

    public OrderServiceImpl(OrderRepository orderRepository,
                            OrderItemService orderItemService,
                            PizzaRepository pizzaRepository,
                            UserRepository userRepository,
                            DeliveryClient deliveryClient) {

        this.orderRepository = orderRepository;
        this.orderItemService = orderItemService;
        this.pizzaRepository = pizzaRepository;
        this.userRepository = userRepository;
        this.deliveryClient = deliveryClient;
    }

    @Override
    @Transactional
    public void createOrder(OrderCreateDTO dto, String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Pizza pizza = pizzaRepository.findById(dto.getPizzaId())
                .orElseThrow(() -> new PizzaNotFoundException("Pizza not found"));

        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedOn(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        OrderItem item = new OrderItem();
        item.setOrder(savedOrder);
        item.setPizza(pizza);
        item.setQuantity(dto.getQuantity());

        orderItemService.save(item);

        DeliveryCreateRequest deliveryRequest = new DeliveryCreateRequest(
                savedOrder.getId(),
                dto.getAddress(),
                dto.getPhoneNumber()
        );

        deliveryClient.createDelivery(deliveryRequest);
    }

    @Override
    public List<Order> getOrdersForUser(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return orderRepository.findAllByUserOrderByCreatedOnDesc(user);
    }

    @Override
    public List<OrderDetailsDTO> getOrderDetails(UUID orderId) {

        orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        return orderItemService.findAllByOrderId(orderId)
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
                                    .multiply(BigDecimal.valueOf(item.getQuantity()))
                    );

                    dto.setStatus(item.getOrder().getStatus().name());
                    dto.setCreatedOn(item.getOrder().getCreatedOn());

                    return dto;
                })
                .toList();
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAllByOrderByCreatedOnDesc();
    }

    @Override
    public void changeStatus(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        switch (order.getStatus()) {
            case PENDING -> order.setStatus(OrderStatus.PREPARING);

            case PREPARING -> {
                order.setStatus(OrderStatus.OUT_FOR_DELIVERY);

                DeliveryResponse delivery =
                        deliveryClient.getByOrderId(orderId);

                deliveryClient.dispatchDelivery(delivery.getId());
            }

            case OUT_FOR_DELIVERY -> {
                order.setStatus(OrderStatus.DELIVERED);

                DeliveryResponse delivery =
                        deliveryClient.getByOrderId(orderId);

                deliveryClient.completeDelivery(delivery.getId());
            }

            case DELIVERED, CANCELLED -> {
                return;
            }
        }

        orderRepository.save(order);
    }

    @Override
    public void cancelOrder(UUID orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        if (order.getStatus() != OrderStatus.PENDING
                && order.getStatus() != OrderStatus.PREPARING) {
            return;
        }

        DeliveryResponse delivery = deliveryClient.getByOrderId(orderId);

        deliveryClient.cancelDelivery(delivery.getId());

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }
}