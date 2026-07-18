package bg.softuni.pizza_delivery_application.service.impl;

import bg.softuni.pizza_delivery_application.client.DeliveryClient;
import bg.softuni.pizza_delivery_application.client.dto.DeliveryCreateRequest;
import bg.softuni.pizza_delivery_application.client.dto.DeliveryResponse;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemService orderItemService;

    @Mock
    private PizzaRepository pizzaRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DeliveryClient deliveryClient;

    private OrderServiceImpl orderService;

    private UUID orderId;
    private UUID pizzaId;
    private UUID deliveryId;

    private User user;
    private Pizza pizza;
    private Order order;

    @BeforeEach
    void setUp() {

        orderService = new OrderServiceImpl(
                orderRepository,
                orderItemService,
                pizzaRepository,
                userRepository,
                deliveryClient
        );

        orderId = UUID.randomUUID();
        pizzaId = UUID.randomUUID();
        deliveryId = UUID.randomUUID();

        user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("jimmy");
        user.setEmail("jimmy@example.com");

        pizza = new Pizza();
        pizza.setId(pizzaId);
        pizza.setName("Margherita");
        pizza.setPrice(new BigDecimal("12.00"));
        pizza.setDescription("Tomato sauce and mozzarella");
        pizza.setImageUrl("https://example.com/margherita.jpg");

        order = createOrder(orderId, OrderStatus.PENDING);
    }

    @Test
    void createOrder_ShouldCreateOrderSuccessfully() {

        OrderCreateDTO dto = createOrderDTO();

        when(userRepository.findByUsername("jimmy"))
                .thenReturn(Optional.of(user));

        when(pizzaRepository.findById(pizzaId))
                .thenReturn(Optional.of(pizza));

        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order savedOrder = invocation.getArgument(0);
                    setOrderId(savedOrder, orderId);
                    return savedOrder;
                });

        orderService.createOrder(dto, "jimmy");

        ArgumentCaptor<Order> orderCaptor =
                ArgumentCaptor.forClass(Order.class);

        verify(orderRepository).save(orderCaptor.capture());

        Order savedOrder = orderCaptor.getValue();

        assertEquals(user, savedOrder.getUser());
        assertEquals(OrderStatus.PENDING, savedOrder.getStatus());
        assertNotNull(savedOrder.getCreatedOn());

        ArgumentCaptor<OrderItem> itemCaptor =
                ArgumentCaptor.forClass(OrderItem.class);

        verify(orderItemService).save(itemCaptor.capture());

        OrderItem savedItem = itemCaptor.getValue();

        assertEquals(savedOrder, savedItem.getOrder());
        assertEquals(pizza, savedItem.getPizza());
        assertEquals(2, savedItem.getQuantity());

        ArgumentCaptor<DeliveryCreateRequest> deliveryCaptor =
                ArgumentCaptor.forClass(DeliveryCreateRequest.class);

        verify(deliveryClient).createDelivery(deliveryCaptor.capture());

        DeliveryCreateRequest request = deliveryCaptor.getValue();

        assertEquals(orderId, request.getOrderId());
        assertEquals("Sofia Center 10", request.getAddress());
        assertEquals("+359888123456", request.getPhoneNumber());
    }

    @Test
    void createOrder_WhenUserMissing_ShouldThrowException() {

        OrderCreateDTO dto = createOrderDTO();

        when(userRepository.findByUsername("jimmy"))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> orderService.createOrder(dto, "jimmy")
        );

        verify(pizzaRepository, never()).findById(any());
        verify(orderRepository, never()).save(any());
        verify(orderItemService, never()).save(any());
        verify(deliveryClient, never()).createDelivery(any());
    }

    @Test
    void createOrder_WhenPizzaMissing_ShouldThrowException() {

        OrderCreateDTO dto = createOrderDTO();

        when(userRepository.findByUsername("jimmy"))
                .thenReturn(Optional.of(user));

        when(pizzaRepository.findById(pizzaId))
                .thenReturn(Optional.empty());

        assertThrows(
                PizzaNotFoundException.class,
                () -> orderService.createOrder(dto, "jimmy")
        );

        verify(orderRepository, never()).save(any());
        verify(orderItemService, never()).save(any());
        verify(deliveryClient, never()).createDelivery(any());
    }

    @Test
    void getOrdersForUser_ShouldReturnOrders() {

        when(userRepository.findByUsername("jimmy"))
                .thenReturn(Optional.of(user));

        when(orderRepository.findAllByUserOrderByCreatedOnDesc(user))
                .thenReturn(List.of(order));

        List<Order> result =
                orderService.getOrdersForUser("jimmy");

        assertEquals(1, result.size());
        assertEquals(order, result.get(0));

        verify(orderRepository)
                .findAllByUserOrderByCreatedOnDesc(user);
    }

    @Test
    void getOrderDetails_ShouldReturnMappedDTOs() {

        order.setStatus(OrderStatus.PREPARING);
        order.setCreatedOn(LocalDateTime.of(
                2026, 7, 18, 20, 30
        ));

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setPizza(pizza);
        item.setQuantity(3);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(orderItemService.findAllByOrderId(orderId))
                .thenReturn(List.of(item));

        List<OrderDetailsDTO> result =
                orderService.getOrderDetails(orderId);

        assertEquals(1, result.size());

        OrderDetailsDTO dto = result.get(0);

        assertEquals(orderId, dto.getOrderId());
        assertEquals("Margherita", dto.getPizzaName());
        assertEquals(3, dto.getQuantity());
        assertEquals(new BigDecimal("12.00"), dto.getPrice());
        assertEquals(new BigDecimal("36.00"), dto.getTotal());
        assertEquals("PREPARING", dto.getStatus());
        assertEquals(order.getCreatedOn(), dto.getCreatedOn());
    }

    @Test
    void changeStatus_FromPending_ShouldBecomePreparing() {

        order.setStatus(OrderStatus.PENDING);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        orderService.changeStatus(orderId);

        assertEquals(OrderStatus.PREPARING, order.getStatus());

        verify(orderRepository).save(order);
        verifyNoInteractions(deliveryClient);
    }

    @Test
    void changeStatus_FromPreparing_ShouldDispatchDelivery() {

        order.setStatus(OrderStatus.PREPARING);

        DeliveryResponse delivery = createDeliveryResponse();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(deliveryClient.getByOrderId(orderId))
                .thenReturn(delivery);

        orderService.changeStatus(orderId);

        assertEquals(
                OrderStatus.OUT_FOR_DELIVERY,
                order.getStatus()
        );

        verify(deliveryClient).getByOrderId(orderId);
        verify(deliveryClient).dispatchDelivery(deliveryId);
        verify(orderRepository).save(order);
    }

    @Test
    void changeStatus_FromOutForDelivery_ShouldCompleteDelivery() {

        order.setStatus(OrderStatus.OUT_FOR_DELIVERY);

        DeliveryResponse delivery = createDeliveryResponse();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(deliveryClient.getByOrderId(orderId))
                .thenReturn(delivery);

        orderService.changeStatus(orderId);

        assertEquals(OrderStatus.DELIVERED, order.getStatus());

        verify(deliveryClient).getByOrderId(orderId);
        verify(deliveryClient).completeDelivery(deliveryId);
        verify(orderRepository).save(order);
    }

    @Test
    void cancelOrder_ShouldCancelOrderAndDelivery() {

        order.setStatus(OrderStatus.PENDING);

        DeliveryResponse delivery = createDeliveryResponse();

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        when(deliveryClient.getByOrderId(orderId))
                .thenReturn(delivery);

        orderService.cancelOrder(orderId);

        assertEquals(OrderStatus.CANCELLED, order.getStatus());

        verify(deliveryClient).getByOrderId(orderId);
        verify(deliveryClient).cancelDelivery(deliveryId);
        verify(orderRepository).save(order);
    }

    @Test
    void cancelOrder_WhenDelivered_ShouldDoNothing() {

        order.setStatus(OrderStatus.DELIVERED);

        when(orderRepository.findById(orderId))
                .thenReturn(Optional.of(order));

        orderService.cancelOrder(orderId);

        assertEquals(OrderStatus.DELIVERED, order.getStatus());

        verifyNoInteractions(deliveryClient);
        verify(orderRepository, never()).save(any());
    }

    private OrderCreateDTO createOrderDTO() {

        OrderCreateDTO dto = new OrderCreateDTO();

        dto.setPizzaId(pizzaId);
        dto.setQuantity(2);
        dto.setAddress("Sofia Center 10");
        dto.setPhoneNumber("+359888123456");

        return dto;
    }

    private DeliveryResponse createDeliveryResponse() {

        DeliveryResponse response = new DeliveryResponse();

        response.setId(deliveryId);
        response.setOrderId(orderId);
        response.setStatus("CREATED");

        return response;
    }

    private Order createOrder(
            UUID id,
            OrderStatus status) {

        Order result = new Order();

        setOrderId(result, id);

        result.setUser(user);
        result.setStatus(status);
        result.setCreatedOn(LocalDateTime.now());

        return result;
    }

    private void setOrderId(
            Order order,
            UUID id) {

        try {
            Field idField =
                    Order.class.getDeclaredField("id");

            idField.setAccessible(true);
            idField.set(order, id);

        } catch (NoSuchFieldException
                 | IllegalAccessException exception) {

            throw new RuntimeException(
                    "Could not set order ID in test",
                    exception
            );
        }
    }
}