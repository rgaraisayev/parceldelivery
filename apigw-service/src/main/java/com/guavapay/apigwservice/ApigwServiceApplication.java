package com.guavapay.apigwservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class ApigwServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApigwServiceApplication.class, args);
    }

}
