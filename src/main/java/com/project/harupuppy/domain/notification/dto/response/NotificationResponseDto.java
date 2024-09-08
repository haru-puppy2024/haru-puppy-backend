package com.project.harupuppy.domain.notification.dto.response;

import com.project.harupuppy.domain.notification.domain.Notification;
import com.project.harupuppy.domain.notification.domain.NotificationType;
import com.project.harupuppy.domain.schedule.domain.AlertType;
import com.project.harupuppy.domain.schedule.domain.ScheduleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResponseDto {
    private Long id;
    private String content;
    private String url;
    private Boolean isRead;
    private NotificationType notificationType;
    private AlertType alertType;
    private ScheduleType scheduleType;
    private LocalDateTime sendDate;

    public NotificationResponseDto(Notification notification) {
        this.id = notification.getId();
        this.content = notification.getContent();
        this.url = notification.getUrl();
        this.isRead = notification.getIsRead();
        this.notificationType = notification.getNotificationType();
        this.scheduleType = notification.getScheduleType();
        this.alertType = notification.getAlertType();
        this.sendDate = notification.getSendDate();
    }
}