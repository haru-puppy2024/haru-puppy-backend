package com.project.harupuppy.global.scheduler;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.Instant;
import java.time.LocalDateTime;

@Component
public class ScheduledTask {

    @Scheduled(cron = "0 * * * * ?") // 매분 실행
    public void sendScheduleNotification() {
        // 스케줄링된 작업 로직 구현

    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    public void addEntryAlertSchedule(TicketCreateEvent event) {
        EntryAlertResponse entryAlert = entryAlertService.create(event.stageId(), event.entryTime());
        addSchedule(entryAlert);
    }

    private void addSchedule(EntryAlertResponse entryAlert) {
        Long entryAlertId = entryAlert.id();
        Instant alertTime = toInstant(entryAlert.alertTime());
        log.info("EntryAlert 스케줄링 추가. entryAlertId: {}, alertTime: {}", entryAlertId, entryAlert.alertTime());
        taskScheduler.schedule(() -> entryAlertService.sendEntryAlert(entryAlertId, alertTime), alertTime);
    }

    private Instant toInstant(LocalDateTime localDateTime) {
        return localDateTime.atZone(Clock.getZone()).toInstant();
    }

}