package bg.softuni.delivery_service.exception;

import bg.softuni.delivery_service.model.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DeliveryNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleDeliveryNotFound(
            DeliveryNotFoundException exception) {

        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                exception.getMessage(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(response);
    }

    @ExceptionHandler(DeliveryAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleDeliveryAlreadyExists(
            DeliveryAlreadyExistsException exception) {

        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                exception.getMessage(),
                null
        );

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationErrors(
            MethodArgumentNotValidException exception) {

        Map<String, String> validationErrors = new LinkedHashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        validationErrors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        ErrorResponseDTO response = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed.",
                validationErrors
        );

        return ResponseEntity
                .badRequest()
                .body(response);
    }
}