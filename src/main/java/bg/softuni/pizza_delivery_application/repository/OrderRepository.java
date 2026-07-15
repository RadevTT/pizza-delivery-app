package bg.softuni.pizza_delivery_application.repository;

import bg.softuni.pizza_delivery_application.model.entity.Order;
import bg.softuni.pizza_delivery_application.model.entity.User;
import bg.softuni.pizza_delivery_application.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    List<Order> findAllByUserOrderByCreatedOnDesc(User user);

    List<Order> findAllByOrderByCreatedOnDesc();

    List<Order> findAllByStatusAndCreatedOnBefore(
            OrderStatus status,
            LocalDateTime createdBefore
    );
}