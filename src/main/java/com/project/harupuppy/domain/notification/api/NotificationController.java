package com.project.harupuppy.domain.notification.api;

import com.project.harupuppy.domain.notification.application.NotificationService;
import com.project.harupuppy.domain.user.domain.UserDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
@Validated
@CrossOrigin(originPatterns = "http://localhost:3000", maxAge = 3600)
public class NotificationController {
    private final NotificationService notificationService;

    /** 알림 구독 요청 */
    @GetMapping(value = "/api/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestHeader(value="Last-Event-ID", required = false, defaultValue = "") String lastEventId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetail user = (UserDetail) principal;

        return notificationService.subscribe(user.getUserId(), lastEventId);
    }


}