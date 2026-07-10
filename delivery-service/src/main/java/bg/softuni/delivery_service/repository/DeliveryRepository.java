package bg.softuni.delivery_service.repository;

import bg.softuni.delivery_service.model.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeliveryRepository extends JpaRepository<Delivery, UUID> {

    Optional<Delivery> findByOrderId(UUID orderId);

    boolean existsByOrderId(UUID orderId);
}