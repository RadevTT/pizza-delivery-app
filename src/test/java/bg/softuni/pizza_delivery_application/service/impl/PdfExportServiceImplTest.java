package bg.softuni.pizza_delivery_application.service.impl;

import bg.softuni.pizza_delivery_application.model.entity.Pizza;
import bg.softuni.pizza_delivery_application.repository.PizzaRepository;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PdfExportServiceImplTest {

    @Mock
    private PizzaRepository pizzaRepository;

    @Mock
    private HttpServletResponse response;

    private PdfExportServiceImpl pdfExportService;

    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() throws IOException {
        pdfExportService = new PdfExportServiceImpl(pizzaRepository);

        outputStream = new ByteArrayOutputStream();

        when(response.getOutputStream())
                .thenReturn(new TestServletOutputStream(outputStream));
    }

    @Test
    void exportPizzaMenu_ShouldGeneratePdfWithPizzaData() throws IOException {
        Pizza pizza = new Pizza();
        pizza.setName("Margherita");
        pizza.setDescription("Tomato sauce and mozzarella");
        pizza.setPrice(new BigDecimal("12.50"));

        when(pizzaRepository.findAll())
                .thenReturn(List.of(pizza));

        pdfExportService.exportPizzaMenu(response);

        verify(response).setContentType("application/pdf");
        verify(response).setHeader(
                "Content-Disposition",
                "attachment; filename=pizza-menu.pdf"
        );
        verify(pizzaRepository).findAll();

        assertTrue(outputStream.size() > 0);
    }

    @Test
    void exportPizzaMenu_WhenDescriptionIsNull_ShouldGeneratePdf()
            throws IOException {

        Pizza pizza = new Pizza();
        pizza.setName("Pepperoni");
        pizza.setDescription(null);
        pizza.setPrice(new BigDecimal("14.90"));

        when(pizzaRepository.findAll())
                .thenReturn(List.of(pizza));

        pdfExportService.exportPizzaMenu(response);

        verify(pizzaRepository).findAll();

        assertTrue(outputStream.size() > 0);
    }

    @Test
    void exportPizzaMenu_WhenDescriptionIsBlank_ShouldGeneratePdf()
            throws IOException {

        Pizza pizza = new Pizza();
        pizza.setName("Quattro Formaggi");
        pizza.setDescription(" ");
        pizza.setPrice(new BigDecimal("16.50"));

        when(pizzaRepository.findAll())
                .thenReturn(List.of(pizza));

        pdfExportService.exportPizzaMenu(response);

        verify(pizzaRepository).findAll();

        assertTrue(outputStream.size() > 0);
    }

    @Test
    void exportPizzaMenu_WhenThereAreNoPizzas_ShouldGenerateEmptyTable()
            throws IOException {

        when(pizzaRepository.findAll())
                .thenReturn(List.of());

        pdfExportService.exportPizzaMenu(response);

        verify(pizzaRepository).findAll();

        assertTrue(outputStream.size() > 0);
    }

    private static class TestServletOutputStream
            extends ServletOutputStream {

        private final ByteArrayOutputStream outputStream;

        private TestServletOutputStream(
                ByteArrayOutputStream outputStream
        ) {
            this.outputStream = outputStream;
        }

        @Override
        public void write(int value) {
            outputStream.write(value);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
        }
    }
}