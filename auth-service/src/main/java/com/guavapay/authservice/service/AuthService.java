package com.guavapay.authservice.service;

import com.guavapay.authservice.entity.User;
import com.guavapay.authservice.enumeration.Role;
import com.guavapay.authservice.model.dto.Token;
import com.guavapay.authservice.model.exception.GenericException;
import com.guavapay.authservice.model.request.LoginRequest;
import com.guavapay.authservice.model.request.RegisterRequest;
import com.guavapay.authservice.model.response.LoginResponse;
import com.guavapay.authservice.rabbitmq.RabbitMQMessageProducer;
import com.guavapay.authservice.rabbitmq.event.NotificationEvent;
import com.guavapay.authservice.rabbitmq.request.NotificationRequest;
import com.guavapay.authservice.repository.UserRepository;
import com.guavapay.authservice.util.DtoMapper;
import com.guavapay.authservice.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final DtoMapper dtoMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RabbitMQMessageProducer rabbitMQMessageProducer;

    @SneakyThrows
    public LoginResponse login(HttpServletResponse response, LoginRequest loginRequest) {
        log.info("login request ({})", loginRequest.getEmail());
        try {
            System.out.println(userRepository.findUserByEmail(loginRequest.getEmail()).isPresent());
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword());

            Authentication authenticate = authenticationManager
                    .authenticate(auth);

            User user = (User) authenticate.getPrincipal();
            Token token = jwtTokenUtil.generateToken(user);
            log.debug("user {} token {}", user.getUsername(), token);
            return LoginResponse.builder()
                    .user(dtoMapper.toUserDto(user))
                    .token(token).build();

        } catch (BadCredentialsException ex) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
        }
        return LoginResponse.builder().build();
    }


    public void registerUser(@Valid RegisterRequest registerRequest) {
        Optional<User> userByEmail = userRepository.findUserByEmail(registerRequest.getEmail());
        if (userByEmail.isPresent()) {
            throw new GenericException("User email is present. Try different one");
        }

        User user = populateUser(Role.USER, registerRequest);
        userRepository.save(user);
    }

    public void registerCourier(@Valid RegisterRequest registerRequest) {
        Optional<User> userByEmail = userRepository.findUserByEmail(registerRequest.getEmail());
        if (userByEmail.isPresent()) {
            throw new GenericException("User email is present. Try different one");
        }

        User user = populateUser(Role.COURIER, registerRequest);
        userRepository.save(user);

        rabbitMQMessageProducer.publishNotification(NotificationEvent.builder()
                .notificationRequest(NotificationRequest.builder()
                        .toDeviceToken("1")
                        .message("Your account have been created")
                        .sender("Parcel Delivery")
                        .toEmail(user.getEmail()).build()).build());
    }

    private User populateUser(Role courier, RegisterRequest registerRequest) {
        User user = new User();
        user.setRole(courier);
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setDateRegistered(LocalDateTime.now());
        return user;
    }
}
