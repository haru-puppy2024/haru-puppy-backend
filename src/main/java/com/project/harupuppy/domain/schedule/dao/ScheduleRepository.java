package com.project.harupuppy.domain.schedule.dao;

import com.project.harupuppy.domain.schedule.domain.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<List<Schedule>> findAllByRepeatIdAndScheduleDateTimeAfter(String repeatId, LocalDateTime scheduleDateTime);
    Optional<List<Schedule>> findAllByRepeatIdAndScheduleDateTimeGreaterThanEqual(String repeatId, LocalDateTime scheduleDateTime);
    Optional<List<Schedule>> findAllByHomeIdAndScheduleDateTimeBetweenOrderByScheduleDateTimeAsc(String homeId, LocalDateTime startDate, LocalDateTime endDate);
}
