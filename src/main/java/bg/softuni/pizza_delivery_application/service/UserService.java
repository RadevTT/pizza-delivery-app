package bg.softuni.pizza_delivery_application.service;

import bg.softuni.pizza_delivery_application.model.dto.AdminUserDTO;
import bg.softuni.pizza_delivery_application.model.dto.ProfileEditDTO;
import bg.softuni.pizza_delivery_application.model.dto.UserRegisterDTO;
import bg.softuni.pizza_delivery_application.model.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    void register(UserRegisterDTO dto);

    User findByUsername(String username);

    boolean usernameExists(String username);

    boolean emailExists(String email);

    List<AdminUserDTO> getAllUsers();

    void addAdminRole(UUID userId);

    void removeAdminRole(UUID userId, String currentUsername);

    void updateProfile(String username, ProfileEditDTO dto);

}