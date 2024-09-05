package com.project.harupuppy.domain.schedule.application;

import com.project.harupuppy.domain.notification.application.NotificationService;
import com.project.harupuppy.domain.notification.domain.NotificationType;
import com.project.harupuppy.domain.schedule.domain.AlertType;
import com.project.harupuppy.domain.schedule.domain.Schedule;
import com.project.harupuppy.domain.schedule.repository.ScheduleRepository;
import com.project.harupuppy.domain.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SchedulerService {
    private final TaskScheduler taskScheduler;
    private final ScheduleRepository scheduleRepository;
    private final NotificationService notificationService;

    private final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    private static final int FIVE_MINUTES = 5;
    private static final int THIRTY_MINUTES = 30;
    private static final int ONE_HOUR = 60;

//    @Scheduled(cron = "0 0 * * * *") // 매 시간 실행
    @Scheduled(cron = "0 * * * * *") // 매 분 실행
    protected void scheduleUpcomingNotifications() {
//        log.info("cron 스케줄 실행 - 현재 시각: {}", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime oneHourLater = now.plusHours(1);
        LocalDateTime fiveMinutesLater = now.plusMinutes(5);

        List<Schedule> upcomingSchedules = scheduleRepository.findUpcomingNotifications(now, fiveMinutesLater);
        upcomingSchedules.forEach(schedule ->
                schedule.getMates().forEach(userSchedule ->
                        scheduleNotification(
                                userSchedule.getUser(),
                                schedule,
                                calculateAlertTime(schedule.getScheduleDateTime(), schedule.getAlertType())
                        )
                )
        );

//        printScheduledTasks();
    }

    private LocalDateTime calculateAlertTime(LocalDateTime dateTime, AlertType alertType) {
        return switch (alertType) {
            case ON_TIME -> dateTime;
            case FIVE_MINUTES -> dateTime.minusMinutes(FIVE_MINUTES);
            case THIRTY_MINUTES -> dateTime.minusMinutes(THIRTY_MINUTES);
            case ONE_HOUR -> dateTime.minusMinutes(ONE_HOUR);
            default -> dateTime;
        };
    }

    @Async
    public void scheduleNotification(User user, Schedule schedule, LocalDateTime alertTime) {
        String scheduleTasksKey = schedule.getId() + "_" + user.getUserId();
        // 기존 작업이 있는지 확인하고 취소
        ScheduledFuture<?> existingTask = scheduledTasks.get(scheduleTasksKey);
        if (existingTask != null) {
//            log.info("작업이 중복 등록 되어 삭제 - 스케줄 ID : {}", schedule.getId());
            existingTask.cancel(false);  // 현재 작업 취소
            scheduledTasks.remove(scheduleTasksKey);  // 맵에서 삭제
        }

        // 새로운 작업 등록
        ScheduledFuture<?> newTask = taskScheduler.schedule(() -> {
            try {
                sendNotification(user, schedule);
                schedule.checkNotification();
                scheduleRepository.save(schedule);
                scheduledTasks.remove(scheduleTasksKey);
            } catch (Exception e) {
                log.error("Send notification failed.", e);
            }
        }, alertTime.atZone(ZoneId.systemDefault()).toInstant());

        // 새로운 작업을 맵에 저장
        scheduledTasks.put(scheduleTasksKey, newTask);
    }

    private void sendNotification(User user, Schedule schedule) {
        notificationService.send(
                user.getUserId(),
                NotificationType.SCHEDULE,
                schedule.getMemo(),
                "/api/schedules/" + schedule.getId(),
                schedule.getScheduleType()
        );
    }

    private void printScheduledTasks() {
        log.info("현재 등록된 스케줄 개수: {}", scheduledTasks.size());
        scheduledTasks.forEach((scheduleId, scheduledFuture) -> {
            log.info("스케줄 ID: {}, 작업 상태: {}", scheduleId, scheduledFuture.isDone() ? "완료" : "대기 중");
        });
    }
}
