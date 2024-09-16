package com.project.harupuppy.domain.schedule.repository;

import com.project.harupuppy.domain.schedule.domain.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepositoryCustom {
    List<Schedule> findUpcomingNotifications(LocalDateTime start, LocalDateTime end);
}