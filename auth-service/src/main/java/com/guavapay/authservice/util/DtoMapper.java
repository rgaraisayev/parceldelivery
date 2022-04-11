package com.guavapay.authservice.util;

import com.guavapay.authservice.entity.User;
import com.guavapay.authservice.model.dto.UserDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    UserDto toUserDto(User user);
}
