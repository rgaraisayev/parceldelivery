package com.guavapay.authservice.model.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Token {
    private String token;
    private String refreshToken;
}
