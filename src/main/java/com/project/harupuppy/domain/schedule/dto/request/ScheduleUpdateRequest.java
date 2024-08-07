package com.project.harupuppy.domain.schedule.dto.request;

import com.project.harupuppy.domain.schedule.domain.AlertType;
import com.project.harupuppy.domain.schedule.domain.RepeatType;
import com.project.harupuppy.domain.schedule.domain.Schedule;
import com.project.harupuppy.domain.schedule.domain.ScheduleType;
import com.project.harupuppy.domain.schedule.dto.UserScheduleDto;
import com.project.harupuppy.global.utils.DateUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    public static Schedule fromDto(ScheduleUpdateRequest dto, String homeId) {
        validateDateTime(dto.scheduleDate, dto.scheduleTime);
        return Schedule.builder()
                .scheduleDateTime(DateUtils.parseDateTime(dto.scheduleDate(), dto.scheduleTime()))
                .scheduleType(dto.scheduleType())
                .homeId(homeId)
                .mates(new ArrayList<>())
                .alertType(dto.alertType())
                .repeatId(null)
                .repeatType(dto.repeatType())
                .memo(dto.memo())
                .build();
    }

    private static void validateDateTime(String date, String time) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("uuuu-MM-dd"));
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            throw new IllegalArgumentException("스케줄 날짜와 시간이 유효하지 않습니다", e);
        }
    }
}
