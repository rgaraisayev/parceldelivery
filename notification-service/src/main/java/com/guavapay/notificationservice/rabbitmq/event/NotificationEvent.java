package com.guavapay.notificationservice.rabbitmq.event;

import com.guavapay.notificationservice.rabbitmq.request.NotificationRequest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {

    private NotificationRequest notificationRequest;
}
