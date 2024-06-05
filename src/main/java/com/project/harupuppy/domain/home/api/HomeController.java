package com.project.harupuppy.domain.home.api;

import com.project.harupuppy.domain.home.application.HomeService;
import com.project.harupuppy.domain.home.dto.HomeResponse;
import com.project.harupuppy.domain.user.domain.UserDetail;
import com.project.harupuppy.global.common.response.ApiResponse;
import com.project.harupuppy.global.common.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
@Validated
@CrossOrigin(originPatterns = "http://localhost:3000", maxAge = 3600)
public class HomeController {
    private final HomeService homeService;

    /**
     * 홈 정보 조회
     */
    @GetMapping("")
    public ApiResponse<HomeResponse> getHome() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetail user = (UserDetail) principal;
        return ApiResponse.ok(Response.Status.RETRIEVE, homeService.getHome(user.getUserId()));
    }


}
