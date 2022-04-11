package com.guavapay.deliveryservice.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RegisterRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;
}
