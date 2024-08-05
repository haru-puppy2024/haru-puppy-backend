package com.project.harupuppy.domain.dog.api;

import com.project.harupuppy.domain.dog.application.DogService;
import com.project.harupuppy.domain.dog.dto.DogUpdateRequest;
import com.project.harupuppy.domain.dog.dto.DogDetailResponse;
import com.project.harupuppy.global.common.response.ApiResponse;
import com.project.harupuppy.global.common.response.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dogs")
@RequiredArgsConstructor
@Validated
public class DogController {
    private final DogService dogService;

    /**
     * 강아지 프로필 수정
     */
    @PatchMapping("")
    public ApiResponse<DogDetailResponse> update(@NotNull @RequestBody @Valid DogUpdateRequest request) {
        return ApiResponse.ok(Response.Status.UPDATE, dogService.updateDogProfile(request));
    }

}
