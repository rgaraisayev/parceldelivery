package com.guavapay.deliveryservice.model.response;

import com.guavapay.deliveryservice.model.dto.Token;
import com.guavapay.deliveryservice.model.dto.UserDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private UserDto user;
    private Token token;
}
