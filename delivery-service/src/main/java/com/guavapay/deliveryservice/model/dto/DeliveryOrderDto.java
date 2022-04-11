package com.guavapay.deliveryservice.model.dto;


import com.guavapay.deliveryservice.enumeration.DeliveryOrderStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DeliveryOrderDto {

    private Long id;
    private AddressDto addressTo;
    private AddressDto addressFrom;
    private LocalDateTime pickupDate;
    private LocalDate deliveryDate;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private String comment;
    private DeliveryOrderStatus status;
    private OrderDetailDto orderDetail;
    private UserDto owner;
}