package bg.softuni.delivery_service.service.impl;

import bg.softuni.delivery_service.exception.DeliveryAlreadyExistsException;
import bg.softuni.delivery_service.exception.DeliveryNotFoundException;
import bg.softuni.delivery_service.model.dto.DeliveryCreateDTO;
import bg.softuni.delivery_service.model.dto.DeliveryResponseDTO;
import bg.softuni.delivery_service.model.entity.Delivery;
import bg.softuni.delivery_service.model.enums.DeliveryStatus;
import bg.softuni.delivery_service.repository.DeliveryRepository;
import bg.softuni.delivery_service.service.DeliveryService;
import bg.softuni.delivery_service.exception.InvalidDeliveryStatusTransitionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class DeliveryServiceImpl implements DeliveryService {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DeliveryServiceImpl.class);

    private final DeliveryRepository deliveryRepository;

    public DeliveryServiceImpl(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public DeliveryResponseDTO createDelivery(DeliveryCreateDTO dto) {

        if (deliveryRepository.existsByOrderId(dto.getOrderId())) {

            LOGGER.warn(
                    "Rejected duplicate delivery creation: orderId={}",
                    dto.getOrderId()
            );

            throw new DeliveryAlreadyExistsException();
        }

        Delivery delivery = new Delivery();

        delivery.setOrderId(dto.getOrderId());
        delivery.setAddress(dto.getAddress());
        delivery.setPhoneNumber(dto.getPhoneNumber());
        delivery.setStatus(DeliveryStatus.CREATED);
        delivery.setCreatedOn(LocalDateTime.now());

        deliveryRepository.save(delivery);

        LOGGER.info(
                "Delivery created successfully: deliveryId={}, orderId={}",
                delivery.getId(),
                delivery.getOrderId()
        );

        return map(delivery);
    }

    @Override
    public DeliveryResponseDTO getByOrderId(UUID orderId) {

        Delivery delivery = deliveryRepository
                .findByOrderId(orderId)
                .orElseThrow(DeliveryNotFoundException::new);

        return map(delivery);
    }

    @Override
    public DeliveryResponseDTO dispatchDelivery(UUID id) {

        Delivery delivery = deliveryRepository
                .findById(id)
                .orElseThrow(DeliveryNotFoundException::new);

        if (delivery.getStatus() != DeliveryStatus.CREATED) {
            throw new InvalidDeliveryStatusTransitionException();
        }

        DeliveryStatus previousStatus = delivery.getStatus();

        delivery.setStatus(DeliveryStatus.ON_THE_WAY);
        delivery.setDispatchedOn(LocalDateTime.now());

        deliveryRepository.save(delivery);

        LOGGER.info(
                "Delivery dispatched: deliveryId={}, orderId={}, previousStatus={}, newStatus={}",
                delivery.getId(),
                delivery.getOrderId(),
                previousStatus,
                delivery.getStatus()
        );

        return map(delivery);
    }
    @Override
    public DeliveryResponseDTO completeDelivery(UUID id) {

        Delivery delivery = deliveryRepository
                .findById(id)
                .orElseThrow(DeliveryNotFoundException::new);

        if (delivery.getStatus() != DeliveryStatus.ON_THE_WAY) {
            throw new InvalidDeliveryStatusTransitionException();
        }

        DeliveryStatus previousStatus = delivery.getStatus();

        delivery.setStatus(DeliveryStatus.DELIVERED);
        delivery.setDeliveredOn(LocalDateTime.now());

        deliveryRepository.save(delivery);

        LOGGER.info(
                "Delivery completed: deliveryId={}, orderId={}, previousStatus={}, newStatus={}",
                delivery.getId(),
                delivery.getOrderId(),
                previousStatus,
                delivery.getStatus()
        );

        return map(delivery);
    }

    @Override
    public void cancelDelivery(UUID id) {

        Delivery delivery = deliveryRepository
                .findById(id)
                .orElseThrow(DeliveryNotFoundException::new);

        if (delivery.getStatus() != DeliveryStatus.CREATED) {
            throw new InvalidDeliveryStatusTransitionException();
        }

        DeliveryStatus previousStatus = delivery.getStatus();

        delivery.setStatus(DeliveryStatus.CANCELLED);

        deliveryRepository.save(delivery);

        LOGGER.info(
                "Delivery cancelled: deliveryId={}, orderId={}, previousStatus={}, newStatus={}",
                delivery.getId(),
                delivery.getOrderId(),
                previousStatus,
                delivery.getStatus()
        );
    }

    private DeliveryResponseDTO map(Delivery delivery) {

        DeliveryResponseDTO dto = new DeliveryResponseDTO();

        dto.setId(delivery.getId());
        dto.setOrderId(delivery.getOrderId());
        dto.setAddress(delivery.getAddress());
        dto.setPhoneNumber(delivery.getPhoneNumber());
        dto.setStatus(delivery.getStatus());
        dto.setCreatedOn(delivery.getCreatedOn());
        dto.setDispatchedOn(delivery.getDispatchedOn());
        dto.setDeliveredOn(delivery.getDeliveredOn());

        return dto;
    }
}