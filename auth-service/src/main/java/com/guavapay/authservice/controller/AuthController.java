package com.guavapay.authservice.controller;

import com.guavapay.authservice.model.request.LoginRequest;
import com.guavapay.authservice.model.request.RegisterRequest;
import com.guavapay.authservice.model.response.BaseResponse;
import com.guavapay.authservice.model.response.LoginResponse;
import com.guavapay.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponse>> login(HttpServletResponse response,
                                                             @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok()
                .body(BaseResponse.<LoginResponse>builder().data(authService.login(response, loginRequest)).build());
    }

    @PostMapping("register")
    public ResponseEntity<BaseResponse<Void>> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        authService.registerUser(registerRequest);
        return ResponseEntity.ok()
                .body(BaseResponse.<Void>builder().build());
    }

    @PostMapping("register/courier")
    public ResponseEntity<BaseResponse<Void>> registerCourier(@RequestBody RegisterRequest registerRequest) {
        authService.registerCourier(registerRequest);
        return ResponseEntity.ok()
                .body(BaseResponse.<Void>builder().build());
    }
}
