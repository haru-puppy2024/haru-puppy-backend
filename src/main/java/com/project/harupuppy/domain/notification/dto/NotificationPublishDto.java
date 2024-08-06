package com.project.harupuppy.domain.notification.dto;

import com.project.harupuppy.domain.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationPublishDto {
    private Long receiverId;
    private NotificationType notificationType;
    private String content;
    private String url;
}