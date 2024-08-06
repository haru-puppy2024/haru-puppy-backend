package com.project.harupuppy.domain.schedule.repository;

import com.project.harupuppy.domain.schedule.domain.AlertType;
import com.project.harupuppy.domain.schedule.domain.Schedule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.time.LocalDateTime;
import java.util.List;

public class ScheduleRepositoryCustomImpl implements ScheduleRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Schedule> findUpcomingNotifications(LocalDateTime start, LocalDateTime end) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Schedule> query = cb.createQuery(Schedule.class);
        Root<Schedule> schedule = query.from(Schedule.class);

        // UserSchedule을 fetch join
        schedule.fetch("mates", JoinType.LEFT);

        // notified가 false인 조건 추가
        Predicate notifiedFalse = cb.isFalse(schedule.get("notified"));

        // 각 알림 타입에 따라 Predicate 추가
        Predicate onTimePredicate = cb.and(
                cb.equal(schedule.get("alertType"), AlertType.ON_TIME),
                cb.between(schedule.get("scheduleDateTime"), start, end)
        );

        Predicate fiveMinutesPredicate = cb.and(
                cb.equal(schedule.get("alertType"), AlertType.FIVE_MINUTES),
                cb.between(schedule.get("scheduleDateTime"), start.plusMinutes(5), end.plusMinutes(5))
        );

        Predicate thirtyMinutesPredicate = cb.and(
                cb.equal(schedule.get("alertType"), AlertType.THIRTY_MINUTES),
                cb.between(schedule.get("scheduleDateTime"), start.plusMinutes(30), end.plusMinutes(30))
        );

        Predicate oneHourPredicate = cb.and(
                cb.equal(schedule.get("alertType"), AlertType.ONE_HOUR),
                cb.between(schedule.get("scheduleDateTime"), start.plusHours(1), end.plusHours(1))
        );

        // 전체 Predicate를 조합
        Predicate finalPredicate = cb.and(notifiedFalse,
                cb.or(onTimePredicate, fiveMinutesPredicate, thirtyMinutesPredicate, oneHourPredicate));

        // 쿼리에 조건 추가
        query.where(finalPredicate);

        return entityManager.createQuery(query).getResultList();
    }
}