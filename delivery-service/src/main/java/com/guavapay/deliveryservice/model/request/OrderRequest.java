package com.guavapay.deliveryservice.model.request;

import com.guavapay.deliveryservice.model.dto.AddressDto;
import com.guavapay.deliveryservice.model.dto.OrderDetailDto;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class OrderRequest {

    private String tags;
    private String comment;

    @NotNull
    private AddressDto addressTo;

    @NotNull
    private AddressDto addressFrom;

    @NotNull
    private String pickupDate;

    @NotNull
    private String deliveryDate;

    @NotNull
    private OrderDetailDto orderDetail;
}
