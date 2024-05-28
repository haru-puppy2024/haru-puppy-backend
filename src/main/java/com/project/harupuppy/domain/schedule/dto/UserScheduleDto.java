package com.project.harupuppy.domain.schedule.dto;

import java.util.Objects;

public record UserScheduleDto(Long userId) {
    public UserScheduleDto {
        Objects.requireNonNull(userId);
    }
}
