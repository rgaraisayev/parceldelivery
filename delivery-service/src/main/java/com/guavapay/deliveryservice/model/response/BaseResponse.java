package com.guavapay.deliveryservice.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class BaseResponse<T> {

    private T data;
    private BaseError error;
}
