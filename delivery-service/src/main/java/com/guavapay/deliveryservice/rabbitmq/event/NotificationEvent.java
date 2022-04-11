package com.guavapay.deliveryservice.rabbitmq.event;

import com.guavapay.deliveryservice.rabbitmq.request.NotificationRequest;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationEvent {

    private NotificationRequest notificationRequest;
}
