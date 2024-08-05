package com.project.harupuppy.domain.home.application;

import com.project.harupuppy.domain.dog.dto.DogDetailResponse;
import com.project.harupuppy.domain.home.domain.Home;
import com.project.harupuppy.domain.home.dto.HomeResponse;
import com.project.harupuppy.domain.home.dto.MateDto;
import com.project.harupuppy.domain.home.dto.RankingDto;
import com.project.harupuppy.domain.home.dto.ReportDto;
import com.project.harupuppy.domain.schedule.domain.Schedule;
import com.project.harupuppy.domain.schedule.domain.ScheduleType;
import com.project.harupuppy.domain.schedule.repository.ScheduleRepository;
import com.project.harupuppy.domain.user.domain.User;
import com.project.harupuppy.domain.user.repository.UserRepository;
import com.project.harupuppy.global.common.exception.CustomException;
import com.project.harupuppy.global.common.response.Response;
import com.project.harupuppy.global.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class HomeService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    /**
     * 홈 정보 조회
     */
    public HomeResponse getHome(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_USER));
        Home home = user.getHome();
        String homeId = home.getHomeId();
        List<User> mates = home.getMates();

        // MateDto 생성
        List<MateDto> mateDtoList = getMateDto(mates);
        // ReportDto 생성
        ReportDto reportDto = getReportDto(homeId);
        // RankingDto 생성
        List<RankingDto> rankingDto = getRankingDto(mates, homeId);

        return HomeResponse.of(DogDetailResponse.of(home.getDog()), mateDtoList, reportDto, rankingDto);
    }

    private List<MateDto> getMateDto(List<User> mates) {
        return mates.stream()
                .map(MateDto::of)
                .toList();
    }

    private ReportDto getReportDto(String homeId) {
        int pooCount = scheduleRepository.countByHomeIdAndScheduleTypeAndDateAndIsActive(
                homeId, ScheduleType.POO, LocalDate.now(), false
        );
        int lastWalkCount = scheduleRepository.countByHomeIdAndScheduleTypeAndDateBetweenAndIsActive(
                homeId, ScheduleType.WALK, DateUtils.getLastWeekMondayDate(), DateUtils.getLastWeekSundayDate(), false
        );
        LocalDate lastWashDate = getLastScheduleDate(homeId, ScheduleType.WASH);
        LocalDate lastHospitalVisitDate = getLastScheduleDate(homeId, ScheduleType.HOSPITAL);

        return ReportDto.of(
                pooCount,
                lastWalkCount,
                lastWashDate,
                lastHospitalVisitDate
        );
    }

    private LocalDate getLastScheduleDate(String homeId, ScheduleType type) {
        Pageable limit = PageRequest.of(0, 1);
        List<Schedule> lastSchedule = scheduleRepository.findTopByHomeIdAndScheduleTypeAndDateLessThanEqualOrderByDateDesc(
                homeId, type, LocalDate.now(), false, limit
        );
        return lastSchedule.stream().findFirst().map(Schedule::getScheduleDateTime)
                .map(LocalDateTime::toLocalDate)
                .orElse(null);
    }


    private List<RankingDto> getRankingDto(List<User> mates, String homeId) {
        return mates.stream()
                .map(mate -> {
                    int mateScheduleCount = scheduleRepository.countByHomeIdAndUserIdAndDateBetweenAndIsActive(
                            homeId, mate.getUserId(), ScheduleType.WALK, DateUtils.getThisWeekStartDate(), DateUtils.getTodayDate(), false
                    );
                    return RankingDto.of(mate, mateScheduleCount);
                })
                .sorted(Comparator.comparingInt(RankingDto::count).reversed()) // 내림차순 정렬
                .toList();
    }

}
