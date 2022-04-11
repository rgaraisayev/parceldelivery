package com.guavapay.notificationservice.listener;

import com.guavapay.notificationservice.rabbitmq.event.NotificationEvent;
import com.guavapay.notificationservice.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationListener {

    private final NotificationService notificationService;

    @RabbitListener(queues = "${rabbitmq.queues.notification}")
    public void consumer(NotificationEvent notificationEvent) {
        log.info("Consumed {} from queue", notificationEvent);
        notificationService.send(notificationEvent.getNotificationRequest());
    }
}
