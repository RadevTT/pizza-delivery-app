package bg.softuni.pizza_delivery_application.controller;

import bg.softuni.pizza_delivery_application.model.dto.ProfileEditDTO;
import bg.softuni.pizza_delivery_application.model.entity.User;
import bg.softuni.pizza_delivery_application.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProfileController.class)
class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    @WithMockUser(username = "pesho")
    void profile_ShouldReturnProfileView() throws Exception {

        User user = new User();
        user.setUsername("pesho");
        user.setEmail("pesho@test.com");

        when(userService.findByUsername("pesho"))
                .thenReturn(user);

        mockMvc.perform(get("/profile"))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeExists("profileEditDTO"));

        verify(userService).findByUsername("pesho");
    }

    @Test
    @WithMockUser(username = "pesho")
    void updateProfile_WithValidData_ShouldRedirect() throws Exception {

        User user = new User();
        user.setUsername("pesho");
        user.setEmail("pesho@test.com");

        when(userService.findByUsername("pesho"))
                .thenReturn(user);

        mockMvc.perform(post("/profile")
                        .with(csrf())
                        .param("email", "new@test.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile"));

        verify(userService).findByUsername("pesho");
        verify(userService).updateProfile(eq("pesho"), any(ProfileEditDTO.class));
    }

    @Test
    @WithMockUser(username = "pesho")
    void updateProfile_WithInvalidData_ShouldReturnProfileView() throws Exception {

        User user = new User();
        user.setUsername("pesho");
        user.setEmail("pesho@test.com");

        when(userService.findByUsername("pesho"))
                .thenReturn(user);

        mockMvc.perform(post("/profile")
                        .with(csrf())
                        .param("email", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("profile"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attributeHasFieldErrors("profileEditDTO", "email"));

        verify(userService).findByUsername("pesho");
        verify(userService, never()).updateProfile(eq("pesho"), any(ProfileEditDTO.class));
    }
}