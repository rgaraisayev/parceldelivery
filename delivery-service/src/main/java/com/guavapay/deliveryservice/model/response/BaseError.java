package com.guavapay.deliveryservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BaseError {
    private Integer code;
    private String message;
}
