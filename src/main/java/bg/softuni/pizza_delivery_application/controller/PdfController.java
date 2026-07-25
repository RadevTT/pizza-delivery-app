package bg.softuni.pizza_delivery_application.controller;

import bg.softuni.pizza_delivery_application.service.PdfExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.IOException;

@Controller
public class PdfController {

    private final PdfExportService pdfExportService;

    public PdfController(PdfExportService pdfExportService) {
        this.pdfExportService = pdfExportService;
    }

    @GetMapping("/pizzas/export/pdf")
    public void exportPizzaMenu(HttpServletResponse response) throws IOException {
        pdfExportService.exportPizzaMenu(response);
    }
}