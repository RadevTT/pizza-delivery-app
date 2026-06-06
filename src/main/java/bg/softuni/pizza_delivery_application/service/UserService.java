package bg.softuni.pizza_delivery_application.service;

import bg.softuni.pizza_delivery_application.model.dto.UserRegisterDTO;

public interface UserService {
    void register(UserRegisterDTO dto);
}
