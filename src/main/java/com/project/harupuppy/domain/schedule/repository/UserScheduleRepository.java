package com.project.harupuppy.domain.schedule.repository;

import com.project.harupuppy.domain.schedule.domain.UserSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserScheduleRepository extends JpaRepository<UserSchedule, Long> {
}
