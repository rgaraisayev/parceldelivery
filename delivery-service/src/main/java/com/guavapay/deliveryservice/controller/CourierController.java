package com.guavapay.deliveryservice.controller;

import com.guavapay.deliveryservice.enumeration.DeliveryOrderStatus;
import com.guavapay.deliveryservice.model.dto.DeliveryOrderDto;
import com.guavapay.deliveryservice.model.response.BaseResponse;
import com.guavapay.deliveryservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/couriers")
@RequiredArgsConstructor
public class CourierController {

    private final OrderService orderService;

    @GetMapping("orders")
    @PreAuthorize("hasRole('ROLE_COURIER')")
    public ResponseEntity<BaseResponse<List<DeliveryOrderDto>>> getAll(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        return ResponseEntity.ok()
                .body(BaseResponse.<List<DeliveryOrderDto>>builder().data(orderService.getAllUserOrders(page, size)).build());
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("hasRole('ROLE_COURIER')")
    public ResponseEntity<BaseResponse<DeliveryOrderDto>> getById(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok()
                .body(BaseResponse.<DeliveryOrderDto>builder().data(orderService.getCourierOrderById(orderId)).build());
    }

    @PutMapping("/orders/{id}/status")
    @PreAuthorize("hasRole('ROLE_COURIER')")
    public ResponseEntity<BaseResponse<Void>> changeOrderStatus(@PathVariable("id") Long orderId,
                                                                @RequestParam("newStatus") DeliveryOrderStatus status) {
        orderService.changeStatus(orderId, status);
        return ResponseEntity.ok()
                .body(BaseResponse.<Void>builder().build());
    }


}
