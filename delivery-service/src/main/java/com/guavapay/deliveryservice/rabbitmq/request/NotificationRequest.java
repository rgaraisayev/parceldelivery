package com.guavapay.deliveryservice.rabbitmq.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationRequest {
    private String toDeviceToken;
    private String toEmail;
    private String sender;
    private String message;
}
