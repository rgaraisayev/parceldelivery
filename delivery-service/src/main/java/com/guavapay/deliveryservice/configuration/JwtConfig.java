package com.guavapay.deliveryservice.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
public class JwtConfig {

    private String secretKey;
    private String secretKeyRefresh;
    private String issuer;
    private Duration validMins;
    private Duration validMinsRefToken;


    public String getAuthorizationHeader() {
        return HttpHeaders.AUTHORIZATION;
    }
}
