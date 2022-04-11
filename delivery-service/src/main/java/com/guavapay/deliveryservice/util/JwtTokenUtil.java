package com.guavapay.deliveryservice.util;


import com.guavapay.deliveryservice.configuration.JwtConfig;
import com.guavapay.deliveryservice.entity.User;
import com.guavapay.deliveryservice.model.dto.Token;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    public Claims getClaims(String token) {
        Jws<Claims> claimsJws = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .requireIssuer(jwtConfig.getIssuer()).build()
                .parseClaimsJws(token);
        return claimsJws.getBody();
    }

    public boolean isInvalid(String token) {
        return getClaims(token).getExpiration().before(new Date());
    }


    public Token generateToken(User user) {
        String token = Jwts.builder()
                .setSubject(user.getEmail())
                .claim("name", user.getName())
                .claim("surname", user.getEmail())
                .claim("role", user.getRole())
                .claim("id", String.valueOf(user.getId()))
                .setIssuer(jwtConfig.getIssuer())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getValidMins().toMillis()))
                .signWith(secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", String.valueOf(user.getId()))
                .setIssuer(jwtConfig.getIssuer())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getValidMinsRefToken().toMillis()))
                .signWith(secretKey)
                .compact();

        return Token.builder().token(token).refreshToken(refreshToken).build();
    }
}
