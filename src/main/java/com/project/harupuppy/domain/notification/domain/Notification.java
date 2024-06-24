package com.project.harupuppy.domain.notification.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "notification")
public class Notification extends TimeAuditingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(nullable = false)
    private String content; // 알림 내용

    @Column(nullable = false)
    private String url; // 관련 링크

    @Column(nullable = false)
    private Boolean isRead; // 알림 확인 여부

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private NotificationType notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User receiver; // 알림 받는 사람

    @Builder
    public Notification(String content, String url, NotificationType notificationType, User receiver) {
        this.content = content;
        this.url = url;
        this.notificationType = notificationType;
        this.isRead = false;
        this.receiver = receiver;
    }

    public void checkNotification() {
        this.isRead = true;
    }
}