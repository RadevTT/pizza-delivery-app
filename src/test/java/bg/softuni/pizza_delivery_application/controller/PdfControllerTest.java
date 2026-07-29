package bg.softuni.pizza_delivery_application.controller;

import bg.softuni.pizza_delivery_application.config.SecurityConfig;
import bg.softuni.pizza_delivery_application.service.PdfExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PdfController.class)
@Import(SecurityConfig.class)
class PdfControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PdfExportService pdfExportService;

    @Test
    @WithMockUser(roles = "ADMIN")
    void exportPizzaMenu_WhenAdmin_ShouldCallService() throws Exception {

        mockMvc.perform(get("/pizzas/export/pdf"))
                .andExpect(status().isOk());

        verify(pdfExportService)
                .exportPizzaMenu(any(HttpServletResponse.class));
    }

    @Test
    void exportPizzaMenu_WhenNotAuthenticated_ShouldRedirectToLogin() throws Exception {

        mockMvc.perform(get("/pizzas/export/pdf"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));

        verify(pdfExportService, never())
                .exportPizzaMenu(any(HttpServletResponse.class));
    }

    @Test
    @WithMockUser(roles = "USER")
    void exportPizzaMenu_WhenUser_ShouldReturnForbidden() throws Exception {

        mockMvc.perform(get("/pizzas/export/pdf"))
                .andExpect(status().isForbidden());

        verify(pdfExportService, never())
                .exportPizzaMenu(any(HttpServletResponse.class));
    }
}