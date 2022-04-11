package com.guavapay.deliveryservice.model.dto;

import lombok.Data;

@Data
public class OrderDetailDto {

    private Integer id;
    private Double weight;
    private Double height;
    private Double width;
}
