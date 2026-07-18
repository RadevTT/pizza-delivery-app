package bg.softuni.pizza_delivery_application.service.impl;

import bg.softuni.pizza_delivery_application.exception.RoleNotFoundException;
import bg.softuni.pizza_delivery_application.exception.UserNotFoundException;
import bg.softuni.pizza_delivery_application.exception.UsernameAlreadyExistsException;
import bg.softuni.pizza_delivery_application.model.dto.AdminUserDTO;
import bg.softuni.pizza_delivery_application.model.dto.ProfileEditDTO;
import bg.softuni.pizza_delivery_application.model.dto.UserRegisterDTO;
import bg.softuni.pizza_delivery_application.model.entity.Role;
import bg.softuni.pizza_delivery_application.model.entity.User;
import bg.softuni.pizza_delivery_application.model.enums.RoleName;
import bg.softuni.pizza_delivery_application.repository.RoleRepository;
import bg.softuni.pizza_delivery_application.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserServiceImpl userService;

    private UUID userId;
    private User user;
    private Role userRole;
    private Role adminRole;

    @BeforeEach
    void setUp() {

        userService = new UserServiceImpl(
                userRepository,
                roleRepository,
                passwordEncoder
        );

        userId = UUID.randomUUID();

        userRole = new Role();
        userRole.setName(RoleName.USER);

        adminRole = new Role();
        adminRole.setName(RoleName.ADMIN);

        user = new User();
        user.setId(userId);
        user.setUsername("jimmy");
        user.setEmail("jimmy@example.com");
        user.setPassword("encoded-password");
        user.getRoles().add(userRole);
    }

    @Test
    void register_WhenDataIsValid_ShouldSaveUserWithUserRole() {

        UserRegisterDTO dto = mock(UserRegisterDTO.class);

        when(dto.getUsername()).thenReturn("newuser");
        when(dto.getEmail()).thenReturn("newuser@example.com");
        when(dto.getPassword()).thenReturn("password123");

        when(userRepository.findByUsername("newuser"))
                .thenReturn(Optional.empty());

        when(roleRepository.findByName(RoleName.USER))
                .thenReturn(Optional.of(userRole));

        when(passwordEncoder.encode("password123"))
                .thenReturn("encoded-password123");

        userService.register(dto);

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(User.class);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertEquals("newuser", savedUser.getUsername());
        assertEquals("newuser@example.com", savedUser.getEmail());
        assertEquals("encoded-password123", savedUser.getPassword());
        assertTrue(savedUser.getRoles().contains(userRole));

        verify(passwordEncoder).encode("password123");
    }

    @Test
    void register_WhenUsernameExists_ShouldThrowException() {

        UserRegisterDTO dto = mock(UserRegisterDTO.class);

        when(dto.getUsername()).thenReturn("jimmy");

        when(userRepository.findByUsername("jimmy"))
                .thenReturn(Optional.of(user));

        assertThrows(
                UsernameAlreadyExistsException.class,
                () -> userService.register(dto)
        );

        verify(userRepository, never()).save(any());
        verify(roleRepository, never()).findByName(any());
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void register_WhenUserRoleDoesNotExist_ShouldThrowException() {

        UserRegisterDTO dto = mock(UserRegisterDTO.class);

        when(dto.getUsername()).thenReturn("newuser");

        when(userRepository.findByUsername("newuser"))
                .thenReturn(Optional.empty());

        when(roleRepository.findByName(RoleName.USER))
                .thenReturn(Optional.empty());

        assertThrows(
                RoleNotFoundException.class,
                () -> userService.register(dto)
        );

        verify(userRepository, never()).save(any());
    }

    @Test
    void findByUsername_WhenUserExists_ShouldReturnUser() {

        when(userRepository.findByUsername("jimmy"))
                .thenReturn(Optional.of(user));

        User result = userService.findByUsername("jimmy");

        assertEquals(user, result);
        assertEquals("jimmy", result.getUsername());

        verify(userRepository).findByUsername("jimmy");
    }

    @Test
    void findByUsername_WhenUserDoesNotExist_ShouldThrowException() {

        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.findByUsername("missing")
        );

        verify(userRepository).findByUsername("missing");
    }

    @Test
    void usernameExists_WhenUsernameExists_ShouldReturnTrue() {

        when(userRepository.findByUsername("jimmy"))
                .thenReturn(Optional.of(user));

        boolean result = userService.usernameExists("jimmy");

        assertTrue(result);
    }

    @Test
    void usernameExists_WhenUsernameDoesNotExist_ShouldReturnFalse() {

        when(userRepository.findByUsername("missing"))
                .thenReturn(Optional.empty());

        boolean result = userService.usernameExists("missing");

        assertFalse(result);
    }

    @Test
    void emailExists_WhenEmailExists_ShouldReturnTrue() {

        when(userRepository.findByEmail("jimmy@example.com"))
                .thenReturn(Optional.of(user));

        boolean result =
                userService.emailExists("jimmy@example.com");

        assertTrue(result);
    }

    @Test
    void emailExists_WhenEmailDoesNotExist_ShouldReturnFalse() {

        when(userRepository.findByEmail("missing@example.com"))
                .thenReturn(Optional.empty());

        boolean result =
                userService.emailExists("missing@example.com");

        assertFalse(result);
    }

    @Test
    void updateProfile_WhenOnlyEmailChanges_ShouldUpdateEmail() {

        ProfileEditDTO dto = mock(ProfileEditDTO.class);

        when(dto.getEmail()).thenReturn("new@example.com");
        when(dto.getPassword()).thenReturn("");

        when(userRepository.findByUsername("jimmy"))
                .thenReturn(Optional.of(user));

        when(userRepository.findByEmail("new@example.com"))
                .thenReturn(Optional.empty());

        userService.updateProfile("jimmy", dto);

        assertEquals("new@example.com", user.getEmail());
        assertEquals("encoded-password", user.getPassword());

        verify(userRepository).saveAndFlush(user);
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void updateProfile_WhenPasswordChanges_ShouldEncodeNewPassword() {

        ProfileEditDTO dto = mock(ProfileEditDTO.class);

        when(dto.getEmail()).thenReturn("jimmy@example.com");
        when(dto.getPassword()).thenReturn("newpassword");
        when(dto.getConfirmPassword()).thenReturn("newpassword");

        when(userRepository.findByUsername("jimmy"))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.encode("newpassword"))
                .thenReturn("new-encoded-password");

        userService.updateProfile("jimmy", dto);

        assertEquals("jimmy@example.com", user.getEmail());
        assertEquals("new-encoded-password", user.getPassword());

        verify(passwordEncoder).encode("newpassword");
        verify(userRepository).saveAndFlush(user);
    }

    @Test
    void updateProfile_WhenEmailAlreadyExists_ShouldThrowException() {

        ProfileEditDTO dto = mock(ProfileEditDTO.class);

        when(dto.getEmail()).thenReturn("used@example.com");

        User anotherUser = new User();
        anotherUser.setUsername("another");

        when(userRepository.findByUsername("jimmy"))
                .thenReturn(Optional.of(user));

        when(userRepository.findByEmail("used@example.com"))
                .thenReturn(Optional.of(anotherUser));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateProfile("jimmy", dto)
        );

        assertEquals("Email already exists", exception.getMessage());

        verify(userRepository, never()).saveAndFlush(any());
    }

    @Test
    void updateProfile_WhenPasswordsDoNotMatch_ShouldThrowException() {

        ProfileEditDTO dto = mock(ProfileEditDTO.class);

        when(dto.getEmail()).thenReturn("jimmy@example.com");
        when(dto.getPassword()).thenReturn("password1");
        when(dto.getConfirmPassword()).thenReturn("password2");

        when(userRepository.findByUsername("jimmy"))
                .thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateProfile("jimmy", dto)
        );

        assertEquals("Passwords do not match", exception.getMessage());

        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).saveAndFlush(any());
    }

    @Test
    void getAllUsers_ShouldMapUsersToAdminUserDTOs() {

        User admin = new User();
        admin.setId(UUID.randomUUID());
        admin.setUsername("admin");
        admin.setEmail("admin@example.com");
        admin.getRoles().add(userRole);
        admin.getRoles().add(adminRole);

        when(userRepository.findAll())
                .thenReturn(List.of(user, admin));

        List<AdminUserDTO> result =
                userService.getAllUsers();

        assertEquals(2, result.size());

        AdminUserDTO normalUserDTO = result.get(0);
        AdminUserDTO adminDTO = result.get(1);

        assertEquals(user.getId(), normalUserDTO.getId());
        assertEquals("jimmy", normalUserDTO.getUsername());
        assertEquals("jimmy@example.com", normalUserDTO.getEmail());
        assertFalse(normalUserDTO.isAdmin());

        assertEquals(admin.getId(), adminDTO.getId());
        assertEquals("admin", adminDTO.getUsername());
        assertEquals("admin@example.com", adminDTO.getEmail());
        assertTrue(adminDTO.isAdmin());
    }

    @Test
    void addAdminRole_WhenUserExists_ShouldAddRoleAndSaveUser() {

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(roleRepository.findByName(RoleName.ADMIN))
                .thenReturn(Optional.of(adminRole));

        userService.addAdminRole(userId);

        assertTrue(user.getRoles().contains(adminRole));

        verify(userRepository).save(user);
    }

    @Test
    void addAdminRole_WhenUserDoesNotExist_ShouldThrowException() {

        when(userRepository.findById(userId))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.addAdminRole(userId)
        );

        verify(roleRepository, never()).findByName(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void removeAdminRole_WhenValid_ShouldRemoveRoleAndSaveUser() {

        user.getRoles().add(adminRole);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        when(roleRepository.findByName(RoleName.ADMIN))
                .thenReturn(Optional.of(adminRole));

        userService.removeAdminRole(userId, "anotherAdmin");

        assertFalse(user.getRoles().contains(adminRole));

        verify(userRepository).save(user);
    }

    @Test
    void removeAdminRole_WhenRemovingOwnRole_ShouldThrowException() {

        user.getRoles().add(adminRole);

        when(userRepository.findById(userId))
                .thenReturn(Optional.of(user));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.removeAdminRole(userId, "jimmy")
        );

        assertEquals(
                "You cannot remove your own administrator role.",
                exception.getMessage()
        );

        assertTrue(user.getRoles().contains(adminRole));

        verify(roleRepository, never()).findByName(any());
        verify(userRepository, never()).save(any());
    }
}