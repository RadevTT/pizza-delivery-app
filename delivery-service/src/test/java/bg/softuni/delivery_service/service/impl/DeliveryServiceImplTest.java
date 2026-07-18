package bg.softuni.delivery_service.service.impl;

import bg.softuni.delivery_service.exception.DeliveryAlreadyExistsException;
import bg.softuni.delivery_service.exception.DeliveryNotFoundException;
import bg.softuni.delivery_service.exception.InvalidDeliveryStatusTransitionException;
import bg.softuni.delivery_service.model.dto.DeliveryCreateDTO;
import bg.softuni.delivery_service.model.dto.DeliveryResponseDTO;
import bg.softuni.delivery_service.model.entity.Delivery;
import bg.softuni.delivery_service.model.enums.DeliveryStatus;
import bg.softuni.delivery_service.repository.DeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryServiceImplTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    private DeliveryServiceImpl deliveryService;

    private UUID deliveryId;
    private UUID orderId;
    private Delivery delivery;

    @BeforeEach
    void setUp() {

        deliveryService =
                new DeliveryServiceImpl(deliveryRepository);

        deliveryId = UUID.randomUUID();
        orderId = UUID.randomUUID();

        delivery = new Delivery();

        setDeliveryId(delivery, deliveryId);

        delivery.setOrderId(orderId);
        delivery.setAddress("Sofia Center 10");
        delivery.setPhoneNumber("+359888123456");
        delivery.setStatus(DeliveryStatus.CREATED);
        delivery.setCreatedOn(LocalDateTime.now());
    }

    @Test
    void createDelivery_WhenDataIsValid_ShouldSaveAndReturnDelivery() {

        DeliveryCreateDTO dto = createDeliveryDTO();

        when(deliveryRepository.existsByOrderId(orderId))
                .thenReturn(false);

        when(deliveryRepository.save(any(Delivery.class)))
                .thenAnswer(invocation -> {

                    Delivery savedDelivery =
                            invocation.getArgument(0);

                    setDeliveryId(savedDelivery, deliveryId);

                    return savedDelivery;
                });

        DeliveryResponseDTO result =
                deliveryService.createDelivery(dto);

        ArgumentCaptor<Delivery> deliveryCaptor =
                ArgumentCaptor.forClass(Delivery.class);

        verify(deliveryRepository)
                .save(deliveryCaptor.capture());

        Delivery savedDelivery =
                deliveryCaptor.getValue();

        assertEquals(orderId, savedDelivery.getOrderId());
        assertEquals(dto.getAddress(), savedDelivery.getAddress());
        assertEquals(
                dto.getPhoneNumber(),
                savedDelivery.getPhoneNumber()
        );
        assertEquals(
                DeliveryStatus.CREATED,
                savedDelivery.getStatus()
        );
        assertNotNull(savedDelivery.getCreatedOn());

        assertEquals(deliveryId, result.getId());
        assertEquals(orderId, result.getOrderId());
        assertEquals(dto.getAddress(), result.getAddress());
        assertEquals(
                dto.getPhoneNumber(),
                result.getPhoneNumber()
        );
        assertEquals(
                DeliveryStatus.CREATED,
                result.getStatus()
        );
        assertNotNull(result.getCreatedOn());
    }

    @Test
    void createDelivery_WhenOrderAlreadyHasDelivery_ShouldThrowException() {

        DeliveryCreateDTO dto = createDeliveryDTO();

        when(deliveryRepository.existsByOrderId(orderId))
                .thenReturn(true);

        assertThrows(
                DeliveryAlreadyExistsException.class,
                () -> deliveryService.createDelivery(dto)
        );

        verify(deliveryRepository, never())
                .save(any());
    }

    @Test
    void getByOrderId_WhenDeliveryExists_ShouldReturnMappedDTO() {

        when(deliveryRepository.findByOrderId(orderId))
                .thenReturn(Optional.of(delivery));

        DeliveryResponseDTO result =
                deliveryService.getByOrderId(orderId);

        assertEquals(deliveryId, result.getId());
        assertEquals(orderId, result.getOrderId());
        assertEquals(delivery.getAddress(), result.getAddress());
        assertEquals(
                delivery.getPhoneNumber(),
                result.getPhoneNumber()
        );
        assertEquals(
                DeliveryStatus.CREATED,
                result.getStatus()
        );

        verify(deliveryRepository)
                .findByOrderId(orderId);
    }

    @Test
    void getByOrderId_WhenDeliveryDoesNotExist_ShouldThrowException() {

        when(deliveryRepository.findByOrderId(orderId))
                .thenReturn(Optional.empty());

        assertThrows(
                DeliveryNotFoundException.class,
                () -> deliveryService.getByOrderId(orderId)
        );
    }

    @Test
    void dispatchDelivery_FromCreated_ShouldChangeStatusAndSave() {

        delivery.setStatus(DeliveryStatus.CREATED);

        when(deliveryRepository.findById(deliveryId))
                .thenReturn(Optional.of(delivery));

        DeliveryResponseDTO result =
                deliveryService.dispatchDelivery(deliveryId);

        assertEquals(
                DeliveryStatus.ON_THE_WAY,
                delivery.getStatus()
        );
        assertNotNull(delivery.getDispatchedOn());

        assertEquals(
                DeliveryStatus.ON_THE_WAY,
                result.getStatus()
        );
        assertNotNull(result.getDispatchedOn());

        verify(deliveryRepository).save(delivery);
    }

    @Test
    void dispatchDelivery_FromDelivered_ShouldThrowException() {

        delivery.setStatus(DeliveryStatus.DELIVERED);

        when(deliveryRepository.findById(deliveryId))
                .thenReturn(Optional.of(delivery));

        assertThrows(
                InvalidDeliveryStatusTransitionException.class,
                () -> deliveryService.dispatchDelivery(deliveryId)
        );

        verify(deliveryRepository, never())
                .save(any());
    }

    @Test
    void dispatchDelivery_FromCancelled_ShouldThrowException() {

        delivery.setStatus(DeliveryStatus.CANCELLED);

        when(deliveryRepository.findById(deliveryId))
                .thenReturn(Optional.of(delivery));

        assertThrows(
                InvalidDeliveryStatusTransitionException.class,
                () -> deliveryService.dispatchDelivery(deliveryId)
        );

        verify(deliveryRepository, never())
                .save(any());
    }

    @Test
    void completeDelivery_FromOnTheWay_ShouldChangeStatusAndSave() {

        delivery.setStatus(DeliveryStatus.ON_THE_WAY);
        delivery.setDispatchedOn(LocalDateTime.now());

        when(deliveryRepository.findById(deliveryId))
                .thenReturn(Optional.of(delivery));

        DeliveryResponseDTO result =
                deliveryService.completeDelivery(deliveryId);

        assertEquals(
                DeliveryStatus.DELIVERED,
                delivery.getStatus()
        );
        assertNotNull(delivery.getDeliveredOn());

        assertEquals(
                DeliveryStatus.DELIVERED,
                result.getStatus()
        );
        assertNotNull(result.getDeliveredOn());

        verify(deliveryRepository).save(delivery);
    }

    @Test
    void completeDelivery_FromCreated_ShouldThrowException() {

        delivery.setStatus(DeliveryStatus.CREATED);

        when(deliveryRepository.findById(deliveryId))
                .thenReturn(Optional.of(delivery));

        assertThrows(
                InvalidDeliveryStatusTransitionException.class,
                () -> deliveryService.completeDelivery(deliveryId)
        );

        verify(deliveryRepository, never())
                .save(any());
    }

    @Test
    void completeDelivery_FromCancelled_ShouldThrowException() {

        delivery.setStatus(DeliveryStatus.CANCELLED);

        when(deliveryRepository.findById(deliveryId))
                .thenReturn(Optional.of(delivery));

        assertThrows(
                InvalidDeliveryStatusTransitionException.class,
                () -> deliveryService.completeDelivery(deliveryId)
        );

        verify(deliveryRepository, never())
                .save(any());
    }

    @Test
    void cancelDelivery_FromCreated_ShouldChangeStatusAndSave() {

        delivery.setStatus(DeliveryStatus.CREATED);

        when(deliveryRepository.findById(deliveryId))
                .thenReturn(Optional.of(delivery));

        deliveryService.cancelDelivery(deliveryId);

        assertEquals(
                DeliveryStatus.CANCELLED,
                delivery.getStatus()
        );

        verify(deliveryRepository).save(delivery);
    }

    @Test
    void cancelDelivery_FromDelivered_ShouldThrowException() {

        delivery.setStatus(DeliveryStatus.DELIVERED);

        when(deliveryRepository.findById(deliveryId))
                .thenReturn(Optional.of(delivery));

        assertThrows(
                InvalidDeliveryStatusTransitionException.class,
                () -> deliveryService.cancelDelivery(deliveryId)
        );

        verify(deliveryRepository, never())
                .save(any());
    }

    @Test
    void cancelDelivery_FromOnTheWay_ShouldThrowException() {

        delivery.setStatus(DeliveryStatus.ON_THE_WAY);

        when(deliveryRepository.findById(deliveryId))
                .thenReturn(Optional.of(delivery));

        assertThrows(
                InvalidDeliveryStatusTransitionException.class,
                () -> deliveryService.cancelDelivery(deliveryId)
        );

        verify(deliveryRepository, never())
                .save(any());
    }

    @Test
    void dispatchDelivery_WhenDeliveryDoesNotExist_ShouldThrowException() {

        when(deliveryRepository.findById(deliveryId))
                .thenReturn(Optional.empty());

        assertThrows(
                DeliveryNotFoundException.class,
                () -> deliveryService.dispatchDelivery(deliveryId)
        );
    }

    @Test
    void completeDelivery_WhenDeliveryDoesNotExist_ShouldThrowException() {

        when(deliveryRepository.findById(deliveryId))
                .thenReturn(Optional.empty());

        assertThrows(
                DeliveryNotFoundException.class,
                () -> deliveryService.completeDelivery(deliveryId)
        );
    }

    @Test
    void cancelDelivery_WhenDeliveryDoesNotExist_ShouldThrowException() {

        when(deliveryRepository.findById(deliveryId))
                .thenReturn(Optional.empty());

        assertThrows(
                DeliveryNotFoundException.class,
                () -> deliveryService.cancelDelivery(deliveryId)
        );
    }

    private DeliveryCreateDTO createDeliveryDTO() {

        DeliveryCreateDTO dto = new DeliveryCreateDTO();

        dto.setOrderId(orderId);
        dto.setAddress("Sofia Center 10");
        dto.setPhoneNumber("+359888123456");

        return dto;
    }

    private void setDeliveryId(
            Delivery delivery,
            UUID id) {

        try {
            Field idField =
                    Delivery.class.getDeclaredField("id");

            idField.setAccessible(true);
            idField.set(delivery, id);

        } catch (NoSuchFieldException
                 | IllegalAccessException exception) {

            throw new RuntimeException(
                    "Could not set delivery ID in test",
                    exception
            );
        }
    }
}