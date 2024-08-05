package com.project.harupuppy.domain.user.api;

import com.project.harupuppy.domain.user.application.UserFacadeService;
import com.project.harupuppy.domain.user.application.UserService;
import com.project.harupuppy.domain.user.domain.UserDetail;
import com.project.harupuppy.domain.user.dto.UserResponse;
import com.project.harupuppy.domain.user.dto.request.UserUpdateRequest;
import com.project.harupuppy.domain.home.dto.HomeCreateRequest;
import com.project.harupuppy.domain.user.dto.request.UserCreateRequest;
import com.project.harupuppy.domain.user.dto.response.UserCreateResponse;
import com.project.harupuppy.global.common.response.ApiResponse;
import com.project.harupuppy.global.common.response.Response;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {
    private final UserFacadeService facadeService;
    private final UserService userService;

    /**
     * 회원 가입
     */
    @PostMapping("/register")
    public ApiResponse<UserCreateResponse> create(@NotNull @RequestBody @Valid HomeCreateRequest request) {
        return ApiResponse.ok(Response.Status.CREATE, facadeService.create(request));
    }

    /**
     * 초대 유저 회원 가입
     */
    @PostMapping("/invitation/{homeId}")
    public ApiResponse<UserCreateResponse> create(@NotNull @RequestBody @Valid UserCreateRequest request,
                                                  @NotBlank @PathVariable("homeId") String homeId) {
        return ApiResponse.ok(Response.Status.CREATE, facadeService.create(request, homeId));
    }

    /**
     * 유저 프로필 수정
     */
    @PutMapping("/profile")
    public ApiResponse<UserResponse> updateProfile(@AuthenticationPrincipal UserDetail requestUser,
                                                   @RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.ok(Response.Status.UPDATE, userService.updateUserInformation(requestUser, request));
    }

    /**
     * 유저 탈퇴
     */
    @PostMapping("/withdraw")
    public ApiResponse<Void> withdraw(@AuthenticationPrincipal UserDetail requestUser) {
        facadeService.withdraw(requestUser);
        return ApiResponse.ok(Response.Status.DELETE);
    }
}
