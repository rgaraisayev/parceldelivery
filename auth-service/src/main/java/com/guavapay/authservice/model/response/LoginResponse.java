package com.guavapay.authservice.model.response;

import com.guavapay.authservice.model.dto.Token;
import com.guavapay.authservice.model.dto.UserDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private UserDto user;
    private Token token;
}
