package com.guavapay.notificationservice.service;


import com.guavapay.notificationservice.entity.Notification;
import com.guavapay.notificationservice.rabbitmq.request.NotificationRequest;
import com.guavapay.notificationservice.repository.NotificationRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public void send(NotificationRequest notificationRequest) {

        notificationRepository.save(
                Notification.builder()
                        .toDeviceToken(notificationRequest.getToDeviceToken())
                        .toEmail(notificationRequest.getToEmail())
                        .sender(notificationRequest.getSender())
                        .message(notificationRequest.getMessage())
                        .sentAt(LocalDateTime.now())
                        .build()
        );
        log.info("Saved to database");

        log.info("Sending push notification ...");
        log.info("Sending email ...");
    }
}
