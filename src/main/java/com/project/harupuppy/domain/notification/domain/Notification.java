package com.project.harupuppy.domain.notification.domain;

import com.project.harupuppy.domain.schedule.domain.ScheduleType;
import com.project.harupuppy.domain.user.domain.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Table(name = "NOTIFICATIONS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @NotBlank
    private String content;

    @Column(nullable = false)
    private String url; // 관련 링크

    @Column(nullable = false)
    private Boolean isRead; // 알림 확인 여부

    @Column(name = "send_date")
    @CreatedDate
    private LocalDateTime sendDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User receiver; // 알림 받는 사람

    @Builder
    public Notification(String content, String url, NotificationType notificationType, ScheduleType scheduleType, User receiver) {
        this.content = content;
        this.url = url;
        this.notificationType = notificationType;
        this.scheduleType = scheduleType;
        this.isRead = false;
        this.sendDate = LocalDateTime.now();
        this.receiver = receiver;
    }

    public void checkNotification() {
        this.isRead = true;
    }
}