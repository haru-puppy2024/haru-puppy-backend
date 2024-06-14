package com.project.harupuppy.domain.schedule.api;

import com.project.harupuppy.domain.schedule.application.ScheduleService;
import com.project.harupuppy.domain.schedule.dto.request.CompletedScheduleCreateRequest;
import com.project.harupuppy.domain.schedule.dto.request.CompletedScheduleDeleteRequest;
import com.project.harupuppy.domain.schedule.dto.request.ScheduleCreateRequest;
import com.project.harupuppy.domain.schedule.dto.request.ScheduleUpdateRequest;
import com.project.harupuppy.domain.schedule.dto.response.ScheduleResponse;
import com.project.harupuppy.domain.user.domain.UserDetail;
import com.project.harupuppy.global.common.response.ApiResponse;
import com.project.harupuppy.global.common.response.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
@RequiredArgsConstructor
@Validated
@CrossOrigin(originPatterns = "http://localhost:3000", maxAge = 3600)
public class ScheduleController {
    private final ScheduleService scheduleService;

    /**
     * 스케줄 추가
     */
    @PostMapping("")
    public ApiResponse<ScheduleResponse> create(@RequestBody @Valid ScheduleCreateRequest dto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetail user = (UserDetail) principal;
        return ApiResponse.ok(Response.Status.CREATE, scheduleService.create(dto, user.getUserId()));
    }

    /**
     * 완료된 스케줄 추가
     */
    @PostMapping("/complete")
    public ApiResponse<ScheduleResponse> createCompletedSchedule(@RequestBody @Valid CompletedScheduleCreateRequest dto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetail user = (UserDetail) principal;
        return ApiResponse.ok(Response.Status.CREATE, scheduleService.createCompletedSchedule(dto, user.getUserId()));
    }

    /**
     * 완료된 오늘의 가장 최근 스케줄 삭제
     */
    @DeleteMapping("/complete")
    public ApiResponse<Void> deleteCompletedSchedule(@RequestBody @Valid CompletedScheduleDeleteRequest dto) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetail user = (UserDetail) principal;
        scheduleService.deleteCompletedSchedule(dto, user.getUserId());
        return ApiResponse.ok(Response.Status.DELETE);
    }

    /**
     * 반복되지 않는 단일 스케줄 수정
     */
    @PutMapping("/{scheduleId}")
    public ApiResponse<ScheduleResponse> update(@NotNull(message = "스케줄 아이디는 필수입니다") @PathVariable Long scheduleId,
                                                @NotNull @RequestBody @Valid ScheduleUpdateRequest dto,
                                                @NotNull(message = "반복 스케줄 수정여부는 필수입니다") @RequestParam(defaultValue = "false") Boolean all) {
        return ApiResponse.ok(Response.Status.UPDATE, scheduleService.update(scheduleId, dto, all));
    }

    /**
     * 스케줄 삭제
     * all=false 반복되지 않는 단일 스케줄 삭제
     * all=true 반복되는 스케줄 삭제
     */
    @DeleteMapping("/{scheduleId}")
    public ApiResponse<Void> delete(@NotNull(message = "스케줄 아이디는 필수입니다") @PathVariable Long scheduleId,
                                    @NotNull @RequestParam(defaultValue = "false") Boolean all) {
        scheduleService.delete(scheduleId, all);
        return ApiResponse.ok(Response.Status.DELETE);
    }

    /**
     * 스케줄 상태 변경
     */
    @PatchMapping("/{scheduleId}/status")
    public ApiResponse<ScheduleResponse> updateStatus(@NotNull(message = "스케줄 상태는 필수입니다") @RequestParam Boolean active,
                                                      @NotNull(message = "스케줄 아이디는 필수입니다") @PathVariable Long scheduleId) {
        return ApiResponse.ok(Response.Status.UPDATE, scheduleService.updateStatus(scheduleId, active));
    }

    /**
     * 특정 월 또는 특정 일의 스케줄 목록 조회
     */
    @GetMapping("")
    public ApiResponse<List<ScheduleResponse>> getSchedules(
            @NotNull(message = "조회 연도는 필수입니다") @RequestParam("year") Integer year,
            @NotNull(message = "조회 월은 필수입니다") @RequestParam("month") Integer month,
            @RequestParam(value = "day", required = false) Integer day) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetail user = (UserDetail) principal;
        if (day != null) {
            return ApiResponse.ok(Response.Status.RETRIEVE, scheduleService.findSchedulesByDay(user.getUserId(), year, month, day));
        } else {
            return ApiResponse.ok(Response.Status.RETRIEVE, scheduleService.findSchedulesByMonth(user.getUserId(), year, month));
        }
    }

    /**
     * 스케줄 단일 조회
     */
    @GetMapping("/{scheduleId}")
    public ApiResponse<ScheduleResponse> get(@NotNull(message = "스케줄 아이디는 필수입니다") @PathVariable Long scheduleId) {
        return ApiResponse.ok(Response.Status.RETRIEVE, scheduleService.findScheduleById(scheduleId));
    }
}
