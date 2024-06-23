package com.project.harupuppy.domain.schedule.repository;

import com.project.harupuppy.domain.schedule.domain.AlertType;
import com.project.harupuppy.domain.schedule.domain.Schedule;
import com.project.harupuppy.domain.schedule.domain.ScheduleType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    @EntityGraph(attributePaths = {"mates"})
    @Query("SELECT s FROM Schedule s " +
            "WHERE s.repeatId = :repeatId " +
            "AND s.scheduleDateTime >= :scheduleDateTime")
    Optional<List<Schedule>> findAllByRepeatIdAndScheduleDateTimeAfterThanEqual(
            @Param("repeatId") String repeatId,
            @Param("scheduleDateTime") LocalDateTime scheduleDateTime
    );

    @EntityGraph(attributePaths = {"mates", "mates.user"})
    @Query("SELECT s FROM Schedule s " +
            "WHERE s.repeatId = :repeatId " +
            "AND s.scheduleDateTime >= :scheduleDateTime")
    Optional<List<Schedule>> findAllByRepeatIdAndScheduleDateTimeAfterThanEqualWithUser(
            @Param("repeatId") String repeatId,
            @Param("scheduleDateTime") LocalDateTime scheduleDateTime
    );

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Schedule s " +
            "SET s.scheduleType = :scheduleType, " +
            "s.alertType = :alertType, " +
            "s.memo = :memo " +
            "WHERE s IN :schedules")
    void updateSchedules(@Param("schedules") List<Schedule> schedules,
                         @Param("scheduleType") ScheduleType scheduleType,
                         @Param("alertType") AlertType alertType,
                         @Param("memo") String memo);

    Optional<List<Schedule>> findAllByRepeatIdAndScheduleDateTimeGreaterThanEqual(String repeatId, LocalDateTime scheduleDateTime);
    Optional<List<Schedule>> findAllByHomeIdAndScheduleDateTimeBetweenOrderByScheduleDateTimeAsc(String homeId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT s.id FROM Schedule s WHERE s.homeId = :homeId")
    List<Long> findScheduleIdsByHomeId(@Param("homeId") String homeId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query(value = "DELETE FROM Schedule s where s.homeId = :homeId")
    void deleteAllByHomeId(@Param("homeId") String homeId);

    @Query("SELECT s FROM Schedule s " +
            "WHERE s.homeId = :homeId " +
            "AND s.scheduleType = :scheduleType " +
            "AND DATE(s.scheduleDateTime) = :date " +
            "AND s.isActive = :isActive " +
            "AND s.repeatType = 'NONE' " +
            "AND s.alertType = 'NONE' " +
            "ORDER BY s.scheduleDateTime DESC")
    Optional<Schedule> findTodayLatestSchedule(
            @Param("homeId") String homeId,
            @Param("scheduleType") ScheduleType scheduleType,
            @Param("date") LocalDate date,
            @Param("isActive") boolean isActive
    );

    @Query("SELECT s FROM Schedule s " +
            "WHERE s.homeId = :homeId " +
            "AND DATE(s.scheduleDateTime) = :date")
    Optional<List<Schedule>> findScheduleByDate(
            @Param("homeId") String homeId,
            @Param("date") LocalDate date
    );

    @Query("SELECT COUNT(s) FROM Schedule s " +
            "WHERE s.homeId = :homeId " +
            "AND s.scheduleType = :scheduleType " +
            "AND DATE(s.scheduleDateTime) = :date " +
            "AND s.isActive = :isActive")
    int countByHomeIdAndScheduleTypeAndDateAndIsActive(
            @Param("homeId") String homeId,
            @Param("scheduleType") ScheduleType scheduleType,
            @Param("date") LocalDate date,
            @Param("isActive") boolean isActive
    );

    @Query("SELECT COUNT(s) FROM Schedule s " +
            "WHERE s.homeId = :homeId " +
            "AND s.scheduleType = :scheduleType " +
            "AND DATE(s.scheduleDateTime) BETWEEN :startDate AND :endDate " +
            "AND s.isActive = :isActive")
    int countByHomeIdAndScheduleTypeAndDateBetweenAndIsActive(
            @Param("homeId") String homeId,
            @Param("scheduleType") ScheduleType scheduleType,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("isActive") boolean isActive
    );

    @Query("SELECT s FROM Schedule s " +
            "WHERE s.homeId = :homeId " +
            "AND s.scheduleType = :scheduleType " +
            "AND DATE(s.scheduleDateTime) <= :date " +
            "AND s.isActive = :isActive " +
            "ORDER BY s.scheduleDateTime DESC")
    Optional<Schedule> findTopByHomeIdAndScheduleTypeAndDateLessThanEqualOrderByDateDesc(
            @Param("homeId") String homeId,
            @Param("scheduleType") ScheduleType scheduleType,
            @Param("date") LocalDate date,
            @Param("isActive") boolean isActive
    );

    @Query("SELECT COUNT(s) FROM Schedule s " +
            "JOIN s.mates m " +
            "WHERE s.homeId = :homeId " +
            "AND m.user.userId = :userId " +
            "AND DATE(s.scheduleDateTime) BETWEEN :startDate AND :endDate " +
            "AND s.isActive = :isActive")
    int countByHomeIdAndUserIdAndDateBetweenAndIsActive(
            @Param("homeId") String homeId,
            @Param("userId") Long userId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("isActive") boolean isActive
    );

}
