package com.project.harupuppy.domain.schedule.domain;

import com.project.harupuppy.domain.schedule.dto.request.ScheduleUpdateRequest;
import com.project.harupuppy.global.common.DateEntity;
import com.project.harupuppy.global.utils.DateUtils;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "SCHEDULES")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Schedule extends DateEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ScheduleType scheduleType;

    @NotNull
    @Column(name = "schedule_datetime")
    private LocalDateTime scheduleDateTime;

    @NotNull
    private String homeId;

    @OneToMany(mappedBy = "schedule", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserSchedule> mates = new ArrayList<>();

    private String repeatId;

    @Enumerated(EnumType.STRING)
    private RepeatType repeatType = RepeatType.NONE;

    @Enumerated(EnumType.STRING)
    private AlertType alertType = AlertType.NONE;

    @Column(columnDefinition = "TEXT")
    private String memo;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    private boolean isActive = true;

    @Builder
    public Schedule(
            ScheduleType scheduleType,
            LocalDateTime scheduleDateTime,
            String homeId,
            List<UserSchedule> mates,
            String repeatId,
            RepeatType repeatType,
            AlertType alertType,
            String memo,
            boolean isActive) {
        this.scheduleType = scheduleType;
        this.scheduleDateTime = scheduleDateTime;
        this.homeId = homeId;
        this.mates = mates;
        this.repeatId = repeatId;
        this.repeatType = repeatType;
        this.alertType = alertType;
        this.memo = memo;
        this.isActive = isActive;
    }

    public static Schedule of(Schedule schedule, String repeatId, LocalDateTime repeatDateTime) {
        List<UserSchedule> copiedMates = new ArrayList<>();
        for (UserSchedule mate : schedule.mates) {
            copiedMates.add(new UserSchedule(mate.getUser(), null)); // 새로운 UserSchedule 인스턴스를 생성
        }
        return Schedule.builder()
                .scheduleType(schedule.scheduleType)
                .scheduleDateTime(repeatDateTime)
                .homeId(schedule.homeId)
                .mates(copiedMates)
                .repeatId(repeatId)
                .repeatType(schedule.repeatType)
                .alertType(schedule.alertType)
                .memo(schedule.memo)
                .build();
    }

    public static List<Schedule> of(List<LocalDateTime> dateTimesUntilNextYear, Schedule schedule, String repeatId) {
        return dateTimesUntilNextYear.stream()
                .map(datetime -> Schedule.of(schedule, repeatId, datetime))
                .collect(Collectors.toList());
    }

    public void update(ScheduleUpdateRequest dto, List<UserSchedule> mates) {
        this.scheduleType = dto.scheduleType();
        this.scheduleDateTime = DateUtils.parseDateTime(dto.scheduleDate(), dto.scheduleTime());
        this.mates = mates;
        this.repeatType = dto.repeatType();
        this.alertType = dto.alertType();
        this.memo = dto.memo();
    }

    public void done() {
        isActive = true;
    }

    public void planned() {
        isActive = false;
    }

    public void updateScheduleDateTime(LocalDateTime dateTime) {
        this.scheduleDateTime = dateTime;
    }

    public void updateRepeatId(String repeatId) {
        this.repeatId = repeatId;
    }

    public void updateMate(List<UserSchedule> mates) {
        this.mates = mates;
    }

    public void addMate(UserSchedule mate) {
        mates.add(mate);
    }
}
