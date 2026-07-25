package bg.softuni.pizza_delivery_application.service.impl;

import bg.softuni.pizza_delivery_application.model.entity.Pizza;
import bg.softuni.pizza_delivery_application.repository.PizzaRepository;
import bg.softuni.pizza_delivery_application.service.PdfExportService;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class PdfExportServiceImpl implements PdfExportService {

    private final PizzaRepository pizzaRepository;

    public PdfExportServiceImpl(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    @Override
    public void exportPizzaMenu(HttpServletResponse response) throws IOException {
        response.setContentType("application/pdf");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=pizza-menu.pdf"
        );

        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, response.getOutputStream());

            document.open();

            Font titleFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    20
            );

            Paragraph title = new Paragraph("Pizza Menu", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20);

            document.add(title);

            PdfPTable table = new PdfPTable(3);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{2.0f, 5.0f, 1.5f});
            table.setSpacingBefore(10);

            addHeaderCell(table, "Name");
            addHeaderCell(table, "Description");
            addHeaderCell(table, "Price");

            List<Pizza> pizzas = pizzaRepository.findAll();

            for (Pizza pizza : pizzas) {
                String description = pizza.getDescription();

                if (description == null || description.isBlank()) {
                    description = "No description";
                }

                table.addCell(createBodyCell(pizza.getName()));
                table.addCell(createBodyCell(description));
                table.addCell(createBodyCell(pizza.getPrice() + " BGN"));
            }

            document.add(table);
        } catch (DocumentException e) {
            throw new IOException("Could not generate pizza menu PDF", e);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
    }

    private void addHeaderCell(PdfPTable table, String text) {
        Font headerFont = FontFactory.getFont(
                FontFactory.HELVETICA_BOLD,
                12
        );

        PdfPCell cell = new PdfPCell(new Phrase(text, headerFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(8);

        table.addCell(cell);
    }

    private PdfPCell createBodyCell(String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(7);

        return cell;
    }
}