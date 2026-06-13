package bg.softuni.pizza_delivery_application.repository;

import bg.softuni.pizza_delivery_application.model.entity.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, UUID> {

}
