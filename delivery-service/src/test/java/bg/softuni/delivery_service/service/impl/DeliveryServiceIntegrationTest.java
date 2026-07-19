package bg.softuni.delivery_service.service.impl;

import bg.softuni.delivery_service.model.dto.DeliveryCreateDTO;
import bg.softuni.delivery_service.model.dto.DeliveryResponseDTO;
import bg.softuni.delivery_service.model.entity.Delivery;
import bg.softuni.delivery_service.model.enums.DeliveryStatus;
import bg.softuni.delivery_service.repository.DeliveryRepository;
import bg.softuni.delivery_service.service.DeliveryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class DeliveryServiceIntegrationTest {

    @Autowired
    private DeliveryService deliveryService;

    @Autowired
    private DeliveryRepository deliveryRepository;

    private UUID orderId;

    @BeforeEach
    void setUp() {

        deliveryRepository.deleteAll();

        orderId = UUID.randomUUID();
    }

    @Test
    void createDispatchAndCompleteDelivery_ShouldPersistAllChanges() {

        DeliveryCreateDTO createDTO = createDeliveryDTO();

        DeliveryResponseDTO createdDelivery =
                deliveryService.createDelivery(createDTO);

        assertNotNull(createdDelivery.getId());
        assertEquals(orderId, createdDelivery.getOrderId());
        assertEquals(
                DeliveryStatus.CREATED,
                createdDelivery.getStatus()
        );
        assertNotNull(createdDelivery.getCreatedOn());

        Optional<Delivery> createdEntity =
                deliveryRepository.findById(createdDelivery.getId());

        assertTrue(createdEntity.isPresent());
        assertEquals(
                DeliveryStatus.CREATED,
                createdEntity.get().getStatus()
        );

        DeliveryResponseDTO dispatchedDelivery =
                deliveryService.dispatchDelivery(
                        createdDelivery.getId()
                );

        assertEquals(
                DeliveryStatus.ON_THE_WAY,
                dispatchedDelivery.getStatus()
        );
        assertNotNull(
                dispatchedDelivery.getDispatchedOn()
        );

        Delivery persistedDispatchedDelivery =
                deliveryRepository.findById(
                        createdDelivery.getId()
                ).orElseThrow();

        assertEquals(
                DeliveryStatus.ON_THE_WAY,
                persistedDispatchedDelivery.getStatus()
        );
        assertNotNull(
                persistedDispatchedDelivery.getDispatchedOn()
        );

        DeliveryResponseDTO completedDelivery =
                deliveryService.completeDelivery(
                        createdDelivery.getId()
                );

        assertEquals(
                DeliveryStatus.DELIVERED,
                completedDelivery.getStatus()
        );
        assertNotNull(
                completedDelivery.getDeliveredOn()
        );

        Delivery persistedCompletedDelivery =
                deliveryRepository.findById(
                        createdDelivery.getId()
                ).orElseThrow();

        assertEquals(
                DeliveryStatus.DELIVERED,
                persistedCompletedDelivery.getStatus()
        );
        assertNotNull(
                persistedCompletedDelivery.getDeliveredOn()
        );
    }

    @Test
    void createAndCancelDelivery_ShouldPersistCancelledStatus() {

        DeliveryResponseDTO createdDelivery =
                deliveryService.createDelivery(
                        createDeliveryDTO()
                );

        deliveryService.cancelDelivery(
                createdDelivery.getId()
        );

        Delivery cancelledDelivery =
                deliveryRepository.findById(
                        createdDelivery.getId()
                ).orElseThrow();

        assertEquals(
                DeliveryStatus.CANCELLED,
                cancelledDelivery.getStatus()
        );
    }

    @Test
    void getByOrderId_ShouldReturnPersistedDelivery() {

        DeliveryResponseDTO createdDelivery =
                deliveryService.createDelivery(
                        createDeliveryDTO()
                );

        DeliveryResponseDTO foundDelivery =
                deliveryService.getByOrderId(orderId);

        assertEquals(
                createdDelivery.getId(),
                foundDelivery.getId()
        );
        assertEquals(
                orderId,
                foundDelivery.getOrderId()
        );
        assertEquals(
                DeliveryStatus.CREATED,
                foundDelivery.getStatus()
        );
        assertEquals(
                "Sofia Center 10",
                foundDelivery.getAddress()
        );
        assertEquals(
                "+359888123456",
                foundDelivery.getPhoneNumber()
        );
    }

    private DeliveryCreateDTO createDeliveryDTO() {

        DeliveryCreateDTO dto =
                new DeliveryCreateDTO();

        dto.setOrderId(orderId);
        dto.setAddress("Sofia Center 10");
        dto.setPhoneNumber("+359888123456");

        return dto;
    }
}