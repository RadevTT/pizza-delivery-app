package bg.softuni.pizza_delivery_application.service;

import bg.softuni.pizza_delivery_application.model.dto.UserRegisterDTO;
import bg.softuni.pizza_delivery_application.model.entity.User;

public interface UserService {

    void register(UserRegisterDTO dto);

    User findByUsername(String username);

    boolean usernameExists(String username);

    boolean emailExists(String email);

}