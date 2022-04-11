package com.guavapay.deliveryservice.service;

import com.guavapay.deliveryservice.beans.UserBean;
import com.guavapay.deliveryservice.entity.Address;
import com.guavapay.deliveryservice.entity.DeliveryOrder;
import com.guavapay.deliveryservice.entity.OrderDetail;
import com.guavapay.deliveryservice.entity.User;
import com.guavapay.deliveryservice.enumeration.DeliveryOrderStatus;
import com.guavapay.deliveryservice.model.dto.AddressDto;
import com.guavapay.deliveryservice.model.dto.DeliveryOrderDto;
import com.guavapay.deliveryservice.model.exception.GenericException;
import com.guavapay.deliveryservice.model.request.OrderRequest;
import com.guavapay.deliveryservice.rabbitmq.RabbitMQMessageProducer;
import com.guavapay.deliveryservice.rabbitmq.event.NotificationEvent;
import com.guavapay.deliveryservice.rabbitmq.request.NotificationRequest;
import com.guavapay.deliveryservice.repository.AddressRepository;
import com.guavapay.deliveryservice.repository.OrderDetailRepository;
import com.guavapay.deliveryservice.repository.OrderRepository;
import com.guavapay.deliveryservice.repository.UserRepository;
import com.guavapay.deliveryservice.util.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    private final DtoMapper dtoMapper;
    private final UserBean userBean;

    public void createOrder(@Valid OrderRequest orderRequest) {
        DeliveryOrder deliveryOrder = new DeliveryOrder();
        deliveryOrder.setDeliveryDate(LocalDate.parse(orderRequest.getDeliveryDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        Address addressTo = new Address();
        addressTo.setAddress(orderRequest.getAddressTo().getAddress());
        addressTo.setCountry(orderRequest.getAddressTo().getCountry());
        addressTo.setCity(orderRequest.getAddressTo().getCity());
        addressTo.setLatitude(orderRequest.getAddressTo().getLatitude());
        addressTo.setLongitude(orderRequest.getAddressTo().getLongitude());
        deliveryOrder.setAddressTo(addressTo);

        Address addressFrom = new Address();
        addressFrom.setAddress(orderRequest.getAddressFrom().getAddress());
        addressFrom.setCountry(orderRequest.getAddressFrom().getCountry());
        addressFrom.setCity(orderRequest.getAddressFrom().getCity());
        addressFrom.setLatitude(orderRequest.getAddressFrom().getLatitude());
        addressFrom.setLongitude(orderRequest.getAddressFrom().getLongitude());
        deliveryOrder.setAddressFrom(addressFrom);

        deliveryOrder.setStatus(DeliveryOrderStatus.CREATED);
        deliveryOrder.setDateCreated(LocalDateTime.now());

        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setHeight(orderRequest.getOrderDetail().getHeight());
        orderDetail.setWeight(orderRequest.getOrderDetail().getWeight());
        orderDetail.setWidth(orderRequest.getOrderDetail().getWidth());
        deliveryOrder.setOrderDetail(orderDetail);

        deliveryOrder.setOwner(userBean.getUser());
        orderRepository.save(deliveryOrder);


        rabbitMQMessageProducer.publishNotification(NotificationEvent.builder()
                .notificationRequest(NotificationRequest.builder()
                        .toDeviceToken("2")
                        .message("New order !")
                        .sender("Parcel Delivery")
                        .toEmail("admin@gmail.com").build()).build());
    }

    public void changeOrderDestination(Long orderId, AddressDto destination) {
        DeliveryOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new GenericException("Order not found"));

        if (!(order.getStatus() == DeliveryOrderStatus.CREATED
                || order.getStatus() == DeliveryOrderStatus.ON_DELIVERY
                || order.getStatus() == DeliveryOrderStatus.CANCELLED)) {
            throw new GenericException("Cannot change order destination");
        }
        Address addressTo = new Address();
        addressTo.setAddress(destination.getAddress());
        addressTo.setCountry(destination.getCountry());
        addressTo.setCity(destination.getCity());
        addressTo.setLatitude(destination.getLatitude());
        addressTo.setLongitude(destination.getLongitude());
        order.setAddressTo(addressTo);
        order.setDateUpdated(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Transactional(readOnly = true)
    public List<DeliveryOrderDto> getAllUserOrders(Integer page, Integer size) {
        return orderRepository.findAllByOwnerId(userBean.getUser().getId(), PageRequest.of(page, size))
                .stream().map(dtoMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DeliveryOrderDto> getAll(Integer page, Integer size) {
        return orderRepository.findAll(PageRequest.of(page, size))
                .stream().map(dtoMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DeliveryOrderDto getById(Long orderId) {
        return dtoMapper.toDto(orderRepository.findById(orderId)
                .orElseThrow(() -> new GenericException("Order not found")));
    }

    public void cancelOrder(Long orderId) {
        DeliveryOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new GenericException("Order not found"));

        order.setStatus(DeliveryOrderStatus.CANCELLED);
        order.setDateUpdated(LocalDateTime.now());
        orderRepository.save(order);
    }

    public void changeStatus(Long orderId, DeliveryOrderStatus status) {
        DeliveryOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new GenericException("Order not found"));

        if ((status == DeliveryOrderStatus.ASSIGNED
                || status == DeliveryOrderStatus.ON_DELIVERY
                || status == DeliveryOrderStatus.DELIVERED) && order.getCourier() == null) {
            throw new GenericException("Cannot change order status. Courier not assigned");
        }

        order.setStatus(status);
        order.setDateUpdated(LocalDateTime.now());
        orderRepository.save(order);
    }

    public void assignOrderToCourier(Long orderId, Long courierId) {
        DeliveryOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new GenericException("Order not found"));

        User courier = userRepository.findById(courierId)
                .orElseThrow(() -> new GenericException("Courier not found"));

        order.setCourier(courier);
        order.setStatus(DeliveryOrderStatus.ASSIGNED);
        order.setDateUpdated(LocalDateTime.now());
        orderRepository.save(order);

        rabbitMQMessageProducer.publishNotification(NotificationEvent.builder()
                .notificationRequest(NotificationRequest.builder()
                        .toDeviceToken("2")
                        .message("You have new order to deliver")
                        .sender("Parcel Delivery")
                        .toEmail(courier.getEmail()).build()).build());
    }

    @Transactional(readOnly = true)
    public DeliveryOrderDto getCourierOrderById(Long orderId) {
        return dtoMapper.toDto(orderRepository.findByIdAndCourierId(orderId, userBean.getUser().getId())
                .orElseThrow(() -> new GenericException("Order not found")));
    }
}
