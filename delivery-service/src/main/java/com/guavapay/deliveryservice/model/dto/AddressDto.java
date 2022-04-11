package com.guavapay.deliveryservice.model.dto;

import lombok.Data;

@Data
public class AddressDto {

    private Integer id;
    private String country;
    private String city;
    private String address;
    private Double longitude;
    private Double latitude;
}
