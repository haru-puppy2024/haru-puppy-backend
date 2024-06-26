package com.project.harupuppy.domain.schedule.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AlertType {
    NONE("없음"),
    ON_TIME("정각"),
    FIVE_MINUTES("5분전"),
    THIRTY_MINUTES("30분전"),
    ONE_HOUR("1시간전");

    private final String desc;

    @JsonCreator
    public AlertType fromString(String value) {
        return AlertType.valueOf(value.toUpperCase());
    }
}
