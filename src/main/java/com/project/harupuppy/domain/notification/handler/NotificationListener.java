package com.project.harupuppy.domain.notification.handler;

import com.project.harupuppy.domain.notification.application.NotificationService;
import com.project.harupuppy.domain.notification.dto.NotificationPublishDto;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationService notificationService;

    @TransactionalEventListener
    @Async
    public void handleNotification(NotificationPublishDto requestDto) {
        notificationService.send(requestDto.getReceiverId(), requestDto.getNotificationType(),
                requestDto.getContent(), requestDto.getUrl(), requestDto.getScheduleType(), requestDto.getAlertType());
    }
}