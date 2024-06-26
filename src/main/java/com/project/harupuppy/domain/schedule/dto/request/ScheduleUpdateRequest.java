package com.project.harupuppy.domain.schedule.dto.request;

import com.project.harupuppy.domain.schedule.domain.AlertType;
import com.project.harupuppy.domain.schedule.domain.RepeatType;
import com.project.harupuppy.domain.schedule.domain.ScheduleType;
import com.project.harupuppy.domain.schedule.dto.UserScheduleDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ScheduleUpdateRequest(
        @NotNull(message = "스케줄 타입 지정이 필요합니다") ScheduleType scheduleType,
        @NotNull(message = "메이트 지정이 필요합니다") List<UserScheduleDto> mates,
        @NotBlank(message = "스케줄 날짜 지정이 필요합니다") String scheduleDate,
        @NotBlank(message = "스케줄 시간 지정이 필요합니다") String scheduleTime,
        String repeatId,
        RepeatType repeatType,
        AlertType alertType,
        String memo
) {
}
