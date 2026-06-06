package bg.softuni.pizza_delivery_application.service.impl;

import bg.softuni.pizza_delivery_application.model.dto.UserRegisterDTO;
import bg.softuni.pizza_delivery_application.model.entity.User;
import bg.softuni.pizza_delivery_application.repository.UserRepository;
import bg.softuni.pizza_delivery_application.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(UserRegisterDTO dto) {

        User user = new User();

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());

        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);
    }
}
