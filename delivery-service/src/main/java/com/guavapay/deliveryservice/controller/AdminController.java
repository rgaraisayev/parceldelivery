package com.guavapay.deliveryservice.controller;

import com.guavapay.deliveryservice.enumeration.DeliveryOrderStatus;
import com.guavapay.deliveryservice.model.dto.DeliveryOrderDto;
import com.guavapay.deliveryservice.model.dto.UserDto;
import com.guavapay.deliveryservice.model.response.BaseResponse;
import com.guavapay.deliveryservice.service.CourierService;
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
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final OrderService orderService;
    private final CourierService courierService;

    @GetMapping("/all-orders")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BaseResponse<List<DeliveryOrderDto>>> getAll(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        return ResponseEntity.ok()
                .body(BaseResponse.<List<DeliveryOrderDto>>builder().data(orderService.getAll(page, size)).build());
    }

    @GetMapping("couriers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BaseResponse<List<UserDto>>> getAllCouriers(@RequestParam("page") Integer page, @RequestParam("size") Integer size) {
        return ResponseEntity.ok()
                .body(BaseResponse.<List<UserDto>>builder().data(courierService.getAllCouriers(page, size)).build());
    }

    @PutMapping("orders/{id}/status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BaseResponse<Void>> changeOrderStatus(@PathVariable("id") Long orderId,
                                                                @RequestParam("newStatus") DeliveryOrderStatus status) {
        orderService.changeStatus(orderId, status);
        return ResponseEntity.ok()
                .body(BaseResponse.<Void>builder().build());
    }


    @PutMapping("orders/{id}/assign")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<BaseResponse<Void>> assignOrderToCourier(@PathVariable("id") Long orderId,
                                                                   @RequestParam("courierId") Long courierId) {
        orderService.assignOrderToCourier(orderId, courierId);
        return ResponseEntity.ok()
                .body(BaseResponse.<Void>builder().build());
    }

}
