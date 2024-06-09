package com.project.harupuppy.domain.schedule.dto.request;

import com.project.harupuppy.domain.schedule.domain.AlertType;
import com.project.harupuppy.domain.schedule.domain.RepeatType;
import com.project.harupuppy.domain.schedule.domain.Schedule;
import com.project.harupuppy.domain.schedule.domain.ScheduleType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.ArrayList;

public record CompletedScheduleCreateRequest(
        @NotNull(message = "스케줄 타입 지정이 필요합니다") ScheduleType scheduleType
) {

    public static Schedule fromDto(CompletedScheduleCreateRequest dto, String homeId) {
        return Schedule.builder()
                .scheduleDateTime(LocalDateTime.now())
                .scheduleType(dto.scheduleType())
                .homeId(homeId)
                .mates(new ArrayList<>())
                .alertType(AlertType.NONE)
                .repeatId(null)
                .repeatType(RepeatType.NONE)
                .memo("")
                .isActive(false)
                .build();
    }

}
