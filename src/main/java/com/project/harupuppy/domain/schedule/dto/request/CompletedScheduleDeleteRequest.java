package com.project.harupuppy.domain.schedule.dto.request;

import com.project.harupuppy.domain.schedule.domain.ScheduleType;
import jakarta.validation.constraints.NotNull;

public record CompletedScheduleDeleteRequest(
        @NotNull(message = "스케줄 타입 지정이 필요합니다") ScheduleType scheduleType
) {
}
