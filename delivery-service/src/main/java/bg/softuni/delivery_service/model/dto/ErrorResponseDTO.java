package bg.softuni.delivery_service.model.dto;

import java.time.LocalDateTime;
import java.util.Map;

public class ErrorResponseDTO {

    private LocalDateTime timestamp;
    private int status;
    private String message;
    private Map<String, String> validationErrors;

    public ErrorResponseDTO() {
    }

    public ErrorResponseDTO(
            LocalDateTime timestamp,
            int status,
            String message,
            Map<String, String> validationErrors) {

        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
        this.validationErrors = validationErrors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getValidationErrors() {
        return validationErrors;
    }
}