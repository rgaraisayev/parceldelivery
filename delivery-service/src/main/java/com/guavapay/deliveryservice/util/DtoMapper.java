package com.guavapay.deliveryservice.util;

import com.guavapay.deliveryservice.entity.Address;
import com.guavapay.deliveryservice.entity.DeliveryOrder;
import com.guavapay.deliveryservice.entity.OrderDetail;
import com.guavapay.deliveryservice.entity.User;
import com.guavapay.deliveryservice.model.dto.AddressDto;
import com.guavapay.deliveryservice.model.dto.DeliveryOrderDto;
import com.guavapay.deliveryservice.model.dto.OrderDetailDto;
import com.guavapay.deliveryservice.model.dto.UserDto;
import com.guavapay.deliveryservice.model.request.OrderRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DtoMapper {

    UserDto toDto(User user);

    AddressDto toDto(Address entity);

    DeliveryOrderDto toDto(DeliveryOrder entity);

    OrderDetailDto toDto(OrderDetail entity);

    Address toEntity(AddressDto dto);

    OrderDetail toEntity(OrderDetailDto dto);

    DeliveryOrder toEntity(OrderRequest dto);
}
