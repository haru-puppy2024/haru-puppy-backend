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
import com.project.harupuppy.domain.schedule.dto.response.ScheduleUpdateResponse;
import com.project.harupuppy.domain.schedule.repository.ScheduleRepository;
import com.project.harupuppy.domain.schedule.repository.UserScheduleRepository;
import com.project.harupuppy.domain.user.domain.User;
import com.project.harupuppy.domain.user.repository.UserRepository;
import com.project.harupuppy.global.common.exception.CustomException;
import com.project.harupuppy.global.common.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final UserScheduleRepository userScheduleRepository;
    private final UserRepository userRepository;

    private enum UpdateType {
        SINGLE_TO_SINGLE,
        SINGLE_TO_REPEAT,
        REPEAT_TO_SINGLE,
        REPEAT_TO_REPEAT,
        REPEAT_TO_REPEAT_NEW_DATE;

        private static UpdateType getType(ScheduleUpdateRequest dto, Schedule schedule, boolean all) {
            boolean isSingleSchedule = schedule.getRepeatId() == null && schedule.getRepeatType().equals(RepeatType.NONE);
            boolean isRepeatSchedule = schedule.getRepeatId() != null && !schedule.getRepeatType().equals(RepeatType.NONE);

            if (isSingleSchedule) {
                return dto.repeatType().equals(RepeatType.NONE) ? SINGLE_TO_SINGLE : SINGLE_TO_REPEAT;
            }

            if (isRepeatSchedule) {
                if (!all) {
                    return REPEAT_TO_SINGLE;
                }
                return checkChangedField(dto, schedule) ? REPEAT_TO_REPEAT : REPEAT_TO_REPEAT_NEW_DATE;
            }

            throw new CustomException(Response.ErrorCode.BAD_REQUEST_SCHEDULE);
        }

        private static boolean checkChangedField(ScheduleUpdateRequest dto, Schedule schedule) {
            LocalDateTime scheduleDateTime = LocalDateTime.of(
                    LocalDate.parse(dto.scheduleDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd")),
                    LocalTime.parse(dto.scheduleTime(), DateTimeFormatter.ofPattern("HH:mm"))
            );
            return schedule.getRepeatType().equals(dto.repeatType()) && schedule.getScheduleDateTime().equals(scheduleDateTime);
        }
    }

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
        schedule.updateRepeatId(repeatId);// 등록 일자의 스케줄에도 repeat id 부여

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

        Optional<Schedule> todayLatestSchedule = scheduleRepository.findTodayLatestSchedule(homeId, dto.scheduleType(), LocalDate.now(), false);
        if (todayLatestSchedule.isPresent()) {
            Schedule todaySchedule = todayLatestSchedule.get();
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

    /**
     * 스케줄 수정
     */
    @Transactional
    public ScheduleUpdateResponse update(Long scheduleId, ScheduleUpdateRequest dto, boolean all) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_SCHEDULE));
        List<User> newUserList = validateMates(dto.mates());
        List<UserSchedule> newUserScheduleList = UserSchedule.of(newUserList, schedule);

        UpdateType updateType = UpdateType.getType(dto, schedule, all);
        switch (updateType) {
            case SINGLE_TO_SINGLE -> {
                schedule.update(dto, newUserScheduleList);
            }
            case SINGLE_TO_REPEAT -> {
                schedule.update(dto, newUserScheduleList);
                createRepeatSchedule(schedule, newUserList);
            }
            case REPEAT_TO_SINGLE -> {
                schedule.update(dto, newUserScheduleList);
                schedule.convertToSingleSchedule();
            }
            case REPEAT_TO_REPEAT -> {
                List<Schedule> repeatedSchedules = scheduleRepository.findAllByRepeatIdAndScheduleDateTimeAfterThanEqual(
                                schedule.getRepeatId(), schedule.getScheduleDateTime())
                        .orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_SCHEDULE));
                scheduleRepository.updateSchedules(repeatedSchedules, dto.scheduleType(), dto.alertType(), dto.memo());

                List<Schedule> repeatedSchedulesAfter = scheduleRepository.findAllByRepeatIdAndScheduleDateTimeAfterThanEqualWithUser(
                                schedule.getRepeatId(), schedule.getScheduleDateTime())
                        .orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_SCHEDULE));
                updateMates(repeatedSchedulesAfter, newUserList);
            }
            case REPEAT_TO_REPEAT_NEW_DATE -> {
                if (dto.repeatType().equals(RepeatType.NONE)) {
                    throw new CustomException(Response.ErrorCode.BAD_REQUEST_SCHEDULE);
                }
                // 기존의 반복 스케줄 제거
                List<Schedule> repeatedSchedules = scheduleRepository.findAllByRepeatIdAndScheduleDateTimeAfterThanEqual(
                                schedule.getRepeatId(), schedule.getScheduleDateTime())
                        .orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_SCHEDULE));
                List<UserSchedule> repeatedUserSchedules = repeatedSchedules.stream()
                        .flatMap(s -> s.getMates().stream())
                        .toList();
                userScheduleRepository.deleteAllInBatch(repeatedUserSchedules);
                scheduleRepository.deleteAllInBatch(repeatedSchedules);

                // 새로 반복스케줄 생성
                Schedule newSchedule = ScheduleUpdateRequest.fromDto(dto, schedule.getHomeId());
                List<UserSchedule> userSchedules = UserSchedule.of(newUserList, newSchedule);
                newSchedule.updateMate(userSchedules);
                scheduleRepository.save(newSchedule);

                createRepeatSchedule(newSchedule, newUserList);
            }
        }

        return ScheduleUpdateResponse.of(updateType.name());
    }

    private void updateMates(List<Schedule> repeatedSchedules, List<User> newMates) {
        // 기존의 UserSchedule 목록을 추출
        List<UserSchedule> existUserSchedules = repeatedSchedules.stream()
                .flatMap(s -> s.getMates().stream())
                .toList();
        // 기존의 User 목록을 추출
        List<User> existUsers = existUserSchedules.stream()
                .map(UserSchedule::getUser)
                .distinct()
                .toList();
        if (hasDifferentUsers(newMates, existUsers)) {
            deleteUserSchedules(newMates, existUserSchedules);
            addUserSchedules(repeatedSchedules, newMates, existUsers);
        }
    }

    private boolean hasDifferentUsers(List<User> newMates, List<User> existUsers) {
        // 새로운 사용자의 ID 목록
        List<Long> newUserIds = newMates.stream()
                .map(User::getUserId)
                .toList();
        // 기존 사용자의 ID 목록
        List<Long> existUserIds = existUsers.stream()
                .map(User::getUserId)
                .toList();
        // 새로운 사용자 ID와 기존 사용자 ID를 비교
        return !newUserIds.equals(existUserIds);
    }
    private void deleteUserSchedules(List<User> newMates, List<UserSchedule> existUserSchedules) {
        // 삭제될 UserSchedule 목록 삭제
        List<UserSchedule> deleteMates = existUserSchedules.stream()
                .filter(us -> newMates.stream()
                        .noneMatch(nm -> nm.getUserId().equals(us.getUser().getUserId())))
                .toList();
        List<Long> deleteIds = deleteMates.stream()
                .map(UserSchedule::getUserScheduleId)
                .collect(Collectors.toList());
        if (!deleteIds.isEmpty()) {
            userScheduleRepository.deleteByIds(deleteIds);
        }
    }
    private void addUserSchedules(List<Schedule> repeatedSchedules, List<User> newMates, List<User> existUsers) {
        List<User> newAddUsers = newMates.stream()
                .filter(newUser -> existUsers.stream()
                        .noneMatch(existUser -> existUser.getUserId().equals(newUser.getUserId())))
                .toList();

        List<UserSchedule> newUserSchedules = newAddUsers.stream()
                .flatMap(user -> repeatedSchedules.stream().map(scheduleItem -> new UserSchedule(user, scheduleItem)))
                .collect(Collectors.toList());

        userScheduleRepository.saveAll(newUserSchedules);
    }

    /**
     * 스케줄 상태 변경
     */
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
