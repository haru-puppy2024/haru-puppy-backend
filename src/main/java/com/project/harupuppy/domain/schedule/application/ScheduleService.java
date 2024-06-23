package com.project.harupuppy.domain.schedule.application;

import com.project.harupuppy.domain.schedule.domain.RepeatType;
import com.project.harupuppy.domain.schedule.domain.Schedule;
import com.project.harupuppy.domain.schedule.domain.UserSchedule;
import com.project.harupuppy.domain.schedule.dto.UserScheduleDto;
import com.project.harupuppy.domain.schedule.dto.request.CompletedScheduleCreateRequest;
import com.project.harupuppy.domain.schedule.dto.request.CompletedScheduleDeleteRequest;
import com.project.harupuppy.domain.schedule.dto.request.ScheduleCreateRequest;
import com.project.harupuppy.domain.schedule.dto.request.ScheduleUpdateRequest;
import com.project.harupuppy.domain.schedule.dto.response.ScheduleResponse;
import com.project.harupuppy.domain.schedule.repository.ScheduleRepository;
import com.project.harupuppy.domain.schedule.repository.UserScheduleRepository;
import com.project.harupuppy.domain.user.domain.User;
import com.project.harupuppy.domain.user.repository.UserRepository;
import com.project.harupuppy.global.common.exception.CustomException;
import com.project.harupuppy.global.common.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserScheduleRepository userScheduleRepository;
    private final UserRepository userRepository;

    /**
     * 스케줄 추가
     */
    @Transactional
    public ScheduleResponse create(ScheduleCreateRequest dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_USER));
        String homeId = user.getHome().getHomeId();

        List<User> mates = validateMates(dto.mates());

        // 먼저 요청 온 정보로 Schedule 하나와 각 mate 만큼 UserSchedule 을 생성 후 저장
        Schedule schedule = ScheduleCreateRequest.fromDto(dto, homeId);
        List<UserSchedule> userSchedules = UserSchedule.of(mates, schedule);
        schedule.updateMate(userSchedules);
        scheduleRepository.save(schedule);

        // 반복 스케줄이라면 반복되는 만큼 생성 후 저장
        if (dto.repeatType() != null && !RepeatType.NONE.equals(dto.repeatType())) {
            createRepeatSchedule(schedule, mates);
        }
        return ScheduleResponse.of(schedule);
    }

    private List<User> validateMates(List<UserScheduleDto> mates) {
        return mates.stream()
                .map(mate -> userRepository.findById(mate.userId())
                        .orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_USER)))
                .collect(Collectors.toList());
    }

    private void createRepeatSchedule(Schedule schedule, List<User> mates) {
        LocalDateTime startDate = schedule.getScheduleDateTime();
        LocalDateTime endDate = startDate.plusYears(1).withMonth(12).withDayOfMonth(31); // 1년 후 연말까지

        String repeatId = UUID.randomUUID().toString();
        schedule.setRepeatId(repeatId);// 등록 일자의 스케줄에도 repeat id 부여

        List<LocalDateTime> dateTimesUntilNextYear = getDateTimesUntilNextYear(
                schedule.getRepeatType(), startDate, endDate, new ArrayList<>());

        List<Schedule> schedules = Schedule.of(dateTimesUntilNextYear, schedule, repeatId);
        for (Schedule var : schedules) {
            List<UserSchedule> userSchedules = UserSchedule.of(mates, var);
            var.updateMate(userSchedules);
        }
        scheduleRepository.saveAll(schedules);
    }

    public static List<LocalDateTime> getDateTimesUntilNextYear(RepeatType type, LocalDateTime startDate,
                                                                LocalDateTime endDate, List<LocalDateTime> repeatedDateTimes) {
        LocalDateTime current;
        switch (type) {
            case DAY -> {
                current = startDate.plusDays(1);
                while (current.isBefore(endDate)) {
                    repeatedDateTimes.add(current);
                    current = current.plusDays(1);
                }
            }
            case WEEK -> {
                current = startDate.plusWeeks(1);
                while (current.isBefore(endDate)) {
                    repeatedDateTimes.add(current);
                    current = current.plusWeeks(1);
                }
            }
            case MONTH -> {
                current = startDate.plusMonths(1);
                while (current.isBefore(endDate)) {
                    repeatedDateTimes.add(current);
                    current = current.plusMonths(1);
                }
            }
            case YEAR -> {
                repeatedDateTimes = List.of(startDate.plusYears(1));
            }
        }
        return repeatedDateTimes;
    }

    /**
     * 완료된 스케줄 추가
     */
    @Transactional
    public ScheduleResponse createCompletedSchedule(CompletedScheduleCreateRequest dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_USER));
        String homeId = user.getHome().getHomeId();

        Schedule schedule = CompletedScheduleCreateRequest.fromDto(dto, homeId);
        UserSchedule userSchedule = new UserSchedule(user, schedule);
        schedule.addMate(userSchedule);
        scheduleRepository.save(schedule);

        return ScheduleResponse.of(schedule);
    }

    /**
     * 완료된 오늘의 가장 최근 스케줄 삭제
     */
    @Transactional
    public void deleteCompletedSchedule(CompletedScheduleDeleteRequest dto, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_USER));
        String homeId = user.getHome().getHomeId();
        Pageable limit = PageRequest.of(0, 1);
        List<Schedule> todayLatestSchedule = scheduleRepository.findTodayLatestSchedule(homeId, dto.scheduleType(), LocalDate.now(), false, limit);
        if (!todayLatestSchedule.isEmpty()) {
            Schedule todaySchedule = todayLatestSchedule.stream().findFirst().get();
            scheduleRepository.delete(todaySchedule);
        } else {
            throw new CustomException(Response.ErrorCode.NOT_FOUND_SCHEDULE);
        }
    }

    /**
     * 스케줄 삭제
     */
    @Transactional
    public void delete(Long scheduleId, boolean all) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_SCHEDULE));

        if (schedule.getRepeatId() != null && all) {
            List<Schedule> repeatedSchedules = scheduleRepository.findAllByRepeatIdAndScheduleDateTimeGreaterThanEqual(
                            schedule.getRepeatId(), schedule.getScheduleDateTime())
                    .orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_SCHEDULE));
            scheduleRepository.deleteAll(repeatedSchedules);
        } else {
            scheduleRepository.delete(schedule);
        }
    }

    @Transactional
    public ScheduleResponse update(Long scheduleId, ScheduleUpdateRequest dto, boolean all) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_SCHEDULE));
        List<UserSchedule> newMates = UserSchedule.of(validateMates(dto.mates()), schedule);
        schedule.update(dto, newMates);
        if (dto.repeatId() != null && all) {
            String repeatId = Objects.requireNonNull(dto.repeatId());
            List<Schedule> repeatedSchedules = scheduleRepository.findAllByRepeatIdAndScheduleDateTimeAfter(repeatId,
                            schedule.getScheduleDateTime())
                    .orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_SCHEDULE));
            repeatedSchedules.forEach(repeatSchedule -> repeatSchedule.update(dto, newMates));
        }
        return ScheduleResponse.of(schedule);
    }

    @Transactional
    public ScheduleResponse updateStatus(Long scheduleId, boolean status) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_SCHEDULE));
        if (status) {
            schedule.done();
        } else {
            schedule.planned();
        }
        return ScheduleResponse.of(schedule);
    }

    /**
     * 특정 월의 스케줄 목록 조회
     */
    public List<ScheduleResponse> findSchedulesByMonth(Long userId, int year, int month) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_USER));
        String homeId = user.getHome().getHomeId();

        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(year, month, YearMonth.of(year, month).lengthOfMonth(), 23, 59);

        List<Schedule> scheduleList = scheduleRepository.findAllByHomeIdAndScheduleDateTimeBetweenOrderByScheduleDateTimeAsc(
                        homeId, startDate, endDate)
                .orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_SCHEDULE));

        return scheduleList.stream()
                .map(ScheduleResponse::of)
                .collect(Collectors.toList());
    }

    /**
     * 특정 일의 스케줄 목록 조회
     */
    public List<ScheduleResponse> findSchedulesByDay(Long userId, int year, int month, int day) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_USER));
        String homeId = user.getHome().getHomeId();
        LocalDate date = LocalDate.of(year, month, day);

        List<Schedule> scheduleList = scheduleRepository.findScheduleByDate(homeId, date)
                .orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_SCHEDULE));

        return scheduleList.stream()
                .map(ScheduleResponse::of)
                .collect(Collectors.toList());
    }

    /**
     * 스케줄 단일 조회
     */
    public ScheduleResponse findScheduleById(Long ScheduleId) {
        Schedule schedule = scheduleRepository.findById(ScheduleId)
                .orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_SCHEDULE));
        return ScheduleResponse.of(schedule);
    }

}
