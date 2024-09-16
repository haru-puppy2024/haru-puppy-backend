package com.project.harupuppy.domain.notification.api;

import com.project.harupuppy.domain.notification.application.NotificationService;
import com.project.harupuppy.domain.notification.dto.response.NotificationResponseDto;
import com.project.harupuppy.domain.user.domain.UserDetail;
import com.project.harupuppy.global.common.response.ApiResponse;
import com.project.harupuppy.global.common.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
public class NotificationController {
    private final NotificationService notificationService;

    /** 알림 구독 요청 */
    @GetMapping(value = "/api/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@RequestHeader(value="Last-Event-ID", required = false, defaultValue = "") String lastEventId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetail user = (UserDetail) principal;

        return notificationService.subscribe(user.getUserId(), lastEventId);
    }

    /** 알림 목록 조회 */
    @GetMapping("/api/notification")
    public ApiResponse<List<NotificationResponseDto>> findNotification(){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetail user = (UserDetail) principal;

        return ApiResponse.ok(Response.Status.RETRIEVE, notificationService.findNotificationList(user.getUserId()));
    }

    /** 알림 단건 조회 */
    @GetMapping("/api/notification/{notificationId}")
    public ApiResponse<NotificationResponseDto> checkNotification(@PathVariable Long notificationId){
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetail user = (UserDetail) principal;

        return ApiResponse.ok(Response.Status.RETRIEVE, notificationService.findNotification(user.getUserId(), notificationId));
    }

}