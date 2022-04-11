package com.guavapay.deliveryservice.controller;

import com.guavapay.deliveryservice.model.dto.AddressDto;
import com.guavapay.deliveryservice.model.dto.DeliveryOrderDto;
import com.guavapay.deliveryservice.model.request.OrderRequest;
import com.guavapay.deliveryservice.model.response.BaseResponse;
import com.guavapay.deliveryservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseResponse<Void>> createOrder(@RequestBody OrderRequest orderRequest) {
        orderService.createOrder(orderRequest);
        return ResponseEntity.ok()
                .body(BaseResponse.<Void>builder().build());
    }

    @PostMapping("{id}/destination")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseResponse<Void>> changeOrderDestination(@PathVariable("id") Long orderId,
                                                                     @RequestBody AddressDto destination) {
        orderService.changeOrderDestination(orderId, destination);
        return ResponseEntity.ok()
                .body(BaseResponse.<Void>builder().build());
    }

    @GetMapping("/my-orders")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseResponse<List<DeliveryOrderDto>>> getAllUserOrders(@RequestParam("page") Integer page,
                                                                                 @RequestParam("size") Integer size) {
        return ResponseEntity.ok()
                .body(BaseResponse.<List<DeliveryOrderDto>>builder()
                        .data(orderService.getAllUserOrders(page, size))
                        .build());
    }

    @GetMapping("/user-orders/{id}")
    @PreAuthorize("hasRole('ROLE_USER') && hasRole('ROLE_ADMIN')")
    public ResponseEntity<BaseResponse<DeliveryOrderDto>> getById(@PathVariable("id") Long orderId) {
        return ResponseEntity.ok()
                .body(BaseResponse.<DeliveryOrderDto>builder().data(orderService.getById(orderId)).build());
    }

    @DeleteMapping("/{id}/cancel")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<BaseResponse<Void>> cancelOrder(@PathVariable("id") Long orderId) {
        orderService.cancelOrder(orderId);
        return ResponseEntity.ok()
                .body(BaseResponse.<Void>builder().build());
    }

}
