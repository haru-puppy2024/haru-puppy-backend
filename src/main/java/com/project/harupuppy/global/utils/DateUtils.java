package com.project.harupuppy.global.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;

public class DateUtils {
    static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static void validateDateTime(String date, String time) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (Exception e) {
            throw new IllegalArgumentException("스케줄 날짜와 시간이 유효하지 않습니다", e);
        }
    }
    public static void validateDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            throw new IllegalArgumentException("생일 날짜가 유효하지 않습니다", e);
        }
    }

    public static LocalDateTime parseDateTime(String date, String time) {
        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        LocalTime localTime = LocalTime.parse(time, timeFormatter);
        return LocalDateTime.of(localDate, localTime);
    }
    public static LocalDate parseDate(String date) {
        return LocalDate.parse(date, dateFormatter);
    }

    public static LocalDate getLastWeekMondayDate() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.MONDAY) {
            return today.minusWeeks(1);
        } else {
            return today.minusWeeks(1).with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        }
    }

    public static LocalDate getLastWeekSundayDate() {
        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();
        if (dayOfWeek == DayOfWeek.SUNDAY) {
            return today.minusWeeks(1);
        } else {
            return today.minusDays(dayOfWeek.getValue());
        }
    }

    public static LocalDate getThisWeekStartDate() {
        LocalDate today = LocalDate.now();
        int dayOfWeek = today.getDayOfWeek().getValue();
        int daysToSubtract = dayOfWeek - 1;
        return today.minusDays(daysToSubtract);
    }

    public static LocalDate getTodayDate() {
        return LocalDate.now();
    }

}
