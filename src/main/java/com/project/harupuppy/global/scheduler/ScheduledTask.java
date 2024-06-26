package com.project.harupuppy.global.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTask {

    @Scheduled(cron = "0 * * * * ?") // 매분 실행
    public void sendScheduleNotification() {
        // 스케줄링된 작업 로직 구현

    }

}