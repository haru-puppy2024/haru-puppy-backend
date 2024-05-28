package com.project.harupuppy.domain.schedule.domain;

import com.project.harupuppy.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
@ToString(exclude = "schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER_SCHEDULES")
public class UserSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userScheduleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private Schedule schedule;

    public UserSchedule(User user, Schedule schedule) {
        this.user = user;
        this.schedule = schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public static List<UserSchedule> of(List<User> mates, Schedule schedule) {
        return mates.stream()
                .map(user -> new UserSchedule(user, schedule))
                .collect(Collectors.toList());
    }
    public static List<UserSchedule> of(List<User> mates, List<Schedule> repeatSchedules) {
        return repeatSchedules.stream()
                .flatMap(schedule -> mates.stream()
                        .map(user -> new UserSchedule(user, schedule)))
                .collect(Collectors.toList());
    }
}
