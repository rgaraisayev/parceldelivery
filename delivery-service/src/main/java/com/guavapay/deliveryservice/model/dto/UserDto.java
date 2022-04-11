package com.guavapay.deliveryservice.model.dto;

import com.guavapay.deliveryservice.enumeration.Role;
import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private Role role;

}
