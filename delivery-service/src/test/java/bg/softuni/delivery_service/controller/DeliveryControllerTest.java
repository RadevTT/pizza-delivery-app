package bg.softuni.delivery_service.controller;

import bg.softuni.delivery_service.exception.DeliveryAlreadyExistsException;
import bg.softuni.delivery_service.exception.DeliveryNotFoundException;
import bg.softuni.delivery_service.exception.InvalidDeliveryStatusTransitionException;
import bg.softuni.delivery_service.model.dto.DeliveryCreateDTO;
import bg.softuni.delivery_service.model.dto.DeliveryResponseDTO;
import bg.softuni.delivery_service.model.enums.DeliveryStatus;
import bg.softuni.delivery_service.service.DeliveryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeliveryController.class)
class DeliveryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DeliveryService deliveryService;

    private UUID deliveryId;
    private UUID orderId;
    private DeliveryResponseDTO deliveryResponse;

    @BeforeEach
    void setUp() {

        deliveryId = UUID.randomUUID();
        orderId = UUID.randomUUID();

        deliveryResponse = new DeliveryResponseDTO();
        deliveryResponse.setId(deliveryId);
        deliveryResponse.setOrderId(orderId);
        deliveryResponse.setAddress("Sofia Center 10");
        deliveryResponse.setPhoneNumber("+359888123456");
        deliveryResponse.setStatus(DeliveryStatus.CREATED);
        deliveryResponse.setCreatedOn(
                LocalDateTime.of(2026, 7, 19, 12, 30)
        );
    }

    @Test
    void createDelivery_WhenRequestIsValid_ShouldReturnCreated() throws Exception {

        DeliveryCreateDTO request = createValidRequest();

        when(deliveryService.createDelivery(any(DeliveryCreateDTO.class)))
                .thenReturn(deliveryResponse);

        mockMvc.perform(
                        post("/api/deliveries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id")
                        .value(deliveryId.toString()))
                .andExpect(jsonPath("$.orderId")
                        .value(orderId.toString()))
                .andExpect(jsonPath("$.address")
                        .value("Sofia Center 10"))
                .andExpect(jsonPath("$.phoneNumber")
                        .value("+359888123456"))
                .andExpect(jsonPath("$.status")
                        .value("CREATED"));

        verify(deliveryService)
                .createDelivery(any(DeliveryCreateDTO.class));
    }

    @Test
    void createDelivery_WhenRequestIsInvalid_ShouldReturnBadRequest()
            throws Exception {

        DeliveryCreateDTO request = new DeliveryCreateDTO();

        request.setAddress("abc");
        request.setPhoneNumber("12");

        mockMvc.perform(
                        post("/api/deliveries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message")
                        .value("Validation failed."))
                .andExpect(jsonPath("$.validationErrors.orderId")
                        .exists())
                .andExpect(jsonPath("$.validationErrors.address")
                        .exists())
                .andExpect(jsonPath("$.validationErrors.phoneNumber")
                        .exists());
    }

    @Test
    void createDelivery_WhenDeliveryAlreadyExists_ShouldReturnConflict()
            throws Exception {

        DeliveryCreateDTO request = createValidRequest();

        when(deliveryService.createDelivery(any(DeliveryCreateDTO.class)))
                .thenThrow(new DeliveryAlreadyExistsException());

        mockMvc.perform(
                        post("/api/deliveries")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message")
                        .value("Delivery already exists for this order."));
    }

    @Test
    void getByOrderId_WhenDeliveryExists_ShouldReturnOk()
            throws Exception {

        when(deliveryService.getByOrderId(orderId))
                .thenReturn(deliveryResponse);

        mockMvc.perform(
                        get("/api/deliveries/order/{orderId}", orderId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(deliveryId.toString()))
                .andExpect(jsonPath("$.orderId")
                        .value(orderId.toString()))
                .andExpect(jsonPath("$.status")
                        .value("CREATED"));

        verify(deliveryService).getByOrderId(orderId);
    }

    @Test
    void getByOrderId_WhenDeliveryDoesNotExist_ShouldReturnNotFound()
            throws Exception {

        when(deliveryService.getByOrderId(orderId))
                .thenThrow(new DeliveryNotFoundException());

        mockMvc.perform(
                        get("/api/deliveries/order/{orderId}", orderId)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message")
                        .value("Delivery not found."));
    }

    @Test
    void dispatchDelivery_ShouldReturnUpdatedDelivery() throws Exception {

        deliveryResponse.setStatus(DeliveryStatus.ON_THE_WAY);
        deliveryResponse.setDispatchedOn(
                LocalDateTime.of(2026, 7, 19, 13, 0)
        );

        when(deliveryService.dispatchDelivery(deliveryId))
                .thenReturn(deliveryResponse);

        mockMvc.perform(
                        put("/api/deliveries/{id}/dispatch", deliveryId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(deliveryId.toString()))
                .andExpect(jsonPath("$.status")
                        .value("ON_THE_WAY"))
                .andExpect(jsonPath("$.dispatchedOn")
                        .exists());

        verify(deliveryService).dispatchDelivery(deliveryId);
    }

    @Test
    void dispatchDelivery_WhenStatusTransitionIsInvalid_ShouldReturnConflict()
            throws Exception {

        when(deliveryService.dispatchDelivery(deliveryId))
                .thenThrow(new InvalidDeliveryStatusTransitionException());

        mockMvc.perform(
                        put("/api/deliveries/{id}/dispatch", deliveryId)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").exists());

        verify(deliveryService).dispatchDelivery(deliveryId);
    }

    @Test
    void completeDelivery_ShouldReturnUpdatedDelivery() throws Exception {

        deliveryResponse.setStatus(DeliveryStatus.DELIVERED);
        deliveryResponse.setDeliveredOn(
                LocalDateTime.of(2026, 7, 19, 14, 0)
        );

        when(deliveryService.completeDelivery(deliveryId))
                .thenReturn(deliveryResponse);

        mockMvc.perform(
                        put("/api/deliveries/{id}/complete", deliveryId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(deliveryId.toString()))
                .andExpect(jsonPath("$.status")
                        .value("DELIVERED"))
                .andExpect(jsonPath("$.deliveredOn")
                        .exists());

        verify(deliveryService).completeDelivery(deliveryId);
    }

    @Test
    void cancelDelivery_ShouldReturnNoContent() throws Exception {

        doNothing()
                .when(deliveryService)
                .cancelDelivery(deliveryId);

        mockMvc.perform(
                        delete("/api/deliveries/{id}", deliveryId)
                )
                .andExpect(status().isNoContent());

        verify(deliveryService).cancelDelivery(deliveryId);
    }

    private DeliveryCreateDTO createValidRequest() {

        DeliveryCreateDTO request = new DeliveryCreateDTO();

        request.setOrderId(orderId);
        request.setAddress("Sofia Center 10");
        request.setPhoneNumber("+359888123456");

        return request;
    }
}