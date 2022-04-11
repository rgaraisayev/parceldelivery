package com.guavapay.deliveryservice.configuration;

import com.guavapay.deliveryservice.beans.UserBean;
import com.guavapay.deliveryservice.entity.User;
import com.guavapay.deliveryservice.enumeration.Role;
import com.guavapay.deliveryservice.util.JwtTokenUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang.StringUtils.isEmpty;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenVerifier extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final UserBean userBean;
    private final JwtTokenUtil jwtTokenUtil;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Request {}, {}", request.getRequestURL(), request.getQueryString());
        String authorizationHeader = request.getHeader(jwtConfig.getAuthorizationHeader());

        if (isEmpty(authorizationHeader) || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.replace("Bearer ", "");

        try {
            Claims body = jwtTokenUtil.getClaims(token);

            User user = new User();
            user.setEmail(body.getSubject());
            user.setName(String.valueOf(body.get("name")));
            user.setRole(Role.valueOf(String.valueOf(body.get("role"))));
            user.setId(Long.valueOf(String.valueOf(body.get("id"))));
            log.info("Request token {}", token);
            Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(),
                    null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
            userBean.setUser(user);

        } catch (JwtException e) {
            response.sendError(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        filterChain.doFilter(request, response);
    }
}
