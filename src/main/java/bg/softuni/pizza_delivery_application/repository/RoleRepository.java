package bg.softuni.pizza_delivery_application.repository;

import bg.softuni.pizza_delivery_application.model.entity.Role;
import bg.softuni.pizza_delivery_application.model.enums.RoleName;
import bg.softuni.pizza_delivery_application.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(RoleName name);
}