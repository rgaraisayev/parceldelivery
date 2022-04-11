package com.guavapay.notificationservice.rabbitmq.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationRequest {
    private String toDeviceToken;
    private String toEmail;
    private String sender;
    private String message;
}
