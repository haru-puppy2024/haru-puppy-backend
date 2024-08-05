package com.project.harupuppy.domain.schedule.dto.response;

public record ScheduleUpdateResponse(
        String updateType
) {
    public static ScheduleUpdateResponse of(String updateType) {
        return new ScheduleUpdateResponse(
                updateType
        );
    }
}