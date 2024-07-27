package com.project.harupuppy.domain.schedule.dto.response;

import com.project.harupuppy.domain.schedule.domain.*;
import com.project.harupuppy.domain.user.domain.User;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public record ScheduleResponse(
        Long scheduleId,
        ScheduleType scheduleType,
        LocalDate scheduleDate,
        LocalTime scheduleTime,
        String homeId,
        List<Long> mates,
        String repeatId,
        RepeatType repeatType,
        AlertType alertType,
        String memo,
        boolean isActive,
        boolean isDeleted
) {
    public static ScheduleResponse of(Schedule schedule) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        LocalDate formattedDate = LocalDate.parse(schedule.getScheduleDateTime().toLocalDate().format(dateFormatter), dateFormatter);
        LocalTime formattedTime = LocalTime.parse(schedule.getScheduleDateTime().toLocalTime().format(timeFormatter), timeFormatter);

        return new ScheduleResponse(
                schedule.getId(),
                schedule.getScheduleType(),
                formattedDate,
                formattedTime,
                schedule.getHomeId(),
                schedule.getMates().stream()
                        .map(UserSchedule::getUser)
                        .map(User::getUserId)
                        .collect(Collectors.toList()),
                schedule.getRepeatId(),
                schedule.getRepeatType(),
                schedule.getAlertType(),
                schedule.getMemo(),
                schedule.isActive(),
                schedule.isDeleted()
        );
    }
}

