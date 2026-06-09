package bg.softuni.pizza_delivery_application.service;


import bg.softuni.pizza_delivery_application.model.dto.UserRegisterDTO;
import bg.softuni.pizza_delivery_application.model.entity.Role;
import bg.softuni.pizza_delivery_application.model.enums.RoleName;

public interface UserService {

    void register(UserRegisterDTO dto);

}