package com.project.harupuppy.domain.home.dto;

import java.time.LocalDate;

public record ReportDto(
        Integer todayPooCount,
        Integer lastWalkCount,
        LocalDate lastWash,
        LocalDate lastHospitalDate
) {
    public static ReportDto of(
            int todayPooCount,
            int lastWalkCount,
            LocalDate lastWashDate,
            LocalDate lastHospitalVisitDate
    ) {
        return new ReportDto(
                todayPooCount,
                lastWalkCount,
                lastWashDate,
                lastHospitalVisitDate
        );
    }
}
