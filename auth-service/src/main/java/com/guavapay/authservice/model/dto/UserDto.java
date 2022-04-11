package com.guavapay.authservice.model.dto;

import com.guavapay.authservice.enumeration.Role;
import lombok.Data;

@Data
public class UserDto {

    private Long id;
    private String name;
    private String email;
    private Role role;

}
