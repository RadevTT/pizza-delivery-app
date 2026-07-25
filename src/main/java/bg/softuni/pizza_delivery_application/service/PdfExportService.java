package bg.softuni.pizza_delivery_application.service;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface PdfExportService {

    void exportPizzaMenu(HttpServletResponse response) throws IOException;
}