package bg.softuni.pizza_delivery_application.init;

import bg.softuni.pizza_delivery_application.model.entity.Role;
import bg.softuni.pizza_delivery_application.model.enums.RoleName;
import bg.softuni.pizza_delivery_application.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {

        if (roleRepository.count() == 0) {

            Role user = new Role();
            user.setName(RoleName.USER);

            Role admin = new Role();
            admin.setName(RoleName.ADMIN);

            roleRepository.save(user);
            roleRepository.save(admin);

            System.out.println("ROLES CREATED");
        }
    }
}
