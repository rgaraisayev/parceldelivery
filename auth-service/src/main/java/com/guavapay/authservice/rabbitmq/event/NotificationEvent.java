package com.guavapay.authservice.rabbitmq.event;

import com.guavapay.authservice.rabbitmq.request.NotificationRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationEvent {

    private NotificationRequest notificationRequest;
}
