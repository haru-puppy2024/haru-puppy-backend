package com.project.harupuppy.domain.schedule.repository;

import com.project.harupuppy.domain.schedule.domain.UserSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserScheduleRepository extends JpaRepository<UserSchedule, Long> {
    @Modifying
    @Query("DELETE FROM UserSchedule us WHERE us.schedule.id IN :scheduleIds")
    void deleteByScheduleIds(@Param("scheduleIds") List<Long> scheduleIds);

    @Modifying
    @Query("DELETE FROM UserSchedule us WHERE us.userScheduleId IN :ids")
    void deleteByIds(@Param("ids") List<Long> ids);
}
