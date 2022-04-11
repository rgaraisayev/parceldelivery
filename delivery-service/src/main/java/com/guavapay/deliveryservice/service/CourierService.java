package com.guavapay.deliveryservice.service;

import com.guavapay.deliveryservice.beans.UserBean;
import com.guavapay.deliveryservice.enumeration.Role;
import com.guavapay.deliveryservice.model.dto.UserDto;
import com.guavapay.deliveryservice.rabbitmq.RabbitMQMessageProducer;
import com.guavapay.deliveryservice.repository.AddressRepository;
import com.guavapay.deliveryservice.repository.OrderDetailRepository;
import com.guavapay.deliveryservice.repository.OrderRepository;
import com.guavapay.deliveryservice.repository.UserRepository;
import com.guavapay.deliveryservice.util.DtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CourierService {

    private final OrderDetailRepository orderDetailRepository;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;
    private final DtoMapper dtoMapper;
    private final UserBean userBean;


    public List<UserDto> getAllCouriers(Integer page, Integer size) {
        return userRepository.findAllByRole(Role.COURIER, PageRequest.of(page, size))
                .stream().map(dtoMapper::toDto).collect(Collectors.toList());
    }


}
