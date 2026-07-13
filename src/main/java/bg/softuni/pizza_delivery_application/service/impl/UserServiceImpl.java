package bg.softuni.pizza_delivery_application.service.impl;

import bg.softuni.pizza_delivery_application.exception.UserNotFoundException;
import bg.softuni.pizza_delivery_application.exception.RoleNotFoundException;
import bg.softuni.pizza_delivery_application.exception.UsernameAlreadyExistsException;
import bg.softuni.pizza_delivery_application.model.dto.AdminUserDTO;
import bg.softuni.pizza_delivery_application.model.dto.ProfileEditDTO;
import bg.softuni.pizza_delivery_application.model.dto.UserRegisterDTO;
import bg.softuni.pizza_delivery_application.model.entity.Role;
import bg.softuni.pizza_delivery_application.model.entity.User;
import bg.softuni.pizza_delivery_application.model.enums.RoleName;
import bg.softuni.pizza_delivery_application.repository.RoleRepository;
import bg.softuni.pizza_delivery_application.repository.UserRepository;
import bg.softuni.pizza_delivery_application.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void register(UserRegisterDTO dto) {

        if (userRepository.findByUsername(dto.getUsername()).isPresent()) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        Role userRole = roleRepository.findByName(RoleName.USER)
                .orElseThrow(() -> new RoleNotFoundException("ROLE_USER not found"));

        User user = new User();

        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        user.getRoles().add(userRole);

        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
    }

    @Override
    public boolean usernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    @Override
    public boolean emailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Override
    @Transactional
    public void updateProfile(String username, ProfileEditDTO dto) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (!user.getEmail().equals(dto.getEmail())
                && userRepository.findByEmail(dto.getEmail()).isPresent()) {

            throw new IllegalArgumentException("Email already exists");
        }

        user.setEmail(dto.getEmail());

        if (dto.getPassword() != null
                && !dto.getPassword().isBlank()) {

            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                throw new IllegalArgumentException("Passwords do not match");
            }

            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        userRepository.saveAndFlush(user);
    }

    @Override
    public List<AdminUserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToAdminUserDTO)
                .toList();
    }

    @Override
    public void addAdminRole(UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                .orElseThrow(() -> new RoleNotFoundException("ROLE_ADMIN not found"));

        user.getRoles().add(adminRole);

        userRepository.save(user);
    }

    @Override
    public void removeAdminRole(UUID userId, String currentUsername) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        if (user.getUsername().equals(currentUsername)) {
            throw new IllegalArgumentException(
                    "You cannot remove your own administrator role."
            );
        }

        Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                .orElseThrow(() -> new RoleNotFoundException("ROLE_ADMIN not found"));

        user.getRoles().remove(adminRole);

        userRepository.save(user);
    }

    private AdminUserDTO mapToAdminUserDTO(User user) {

        AdminUserDTO dto = new AdminUserDTO();

        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setAdmin(
                user.getRoles()
                        .stream()
                        .anyMatch(role -> role.getName() == RoleName.ADMIN)
        );

        return dto;
    }
}