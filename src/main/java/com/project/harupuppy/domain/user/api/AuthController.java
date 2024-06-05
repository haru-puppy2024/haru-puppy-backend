package com.project.harupuppy.domain.user.api;

import com.project.harupuppy.domain.user.application.UserFacadeService;
import com.project.harupuppy.domain.user.dto.TokenDto;
import com.project.harupuppy.domain.user.dto.response.LoginResponse;
import com.project.harupuppy.global.common.response.ApiResponse;
import com.project.harupuppy.global.common.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
@CrossOrigin(origins = "*", exposedHeaders = {"Content-Disposition"})
public class AuthController {
    private final UserFacadeService userFacadeService;

    /**
     * oAuth 로그인
     */
    @GetMapping("/login/{provider}")
    public ApiResponse<LoginResponse> login(@PathVariable("provider") String provider,
                                            @RequestParam("code") String code) {
        LoginResponse response = userFacadeService.login(provider, code);
        return ApiResponse.ok(Response.Status.CREATE, response);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout(HttpServletRequest request){
        String accessToken = request.getHeader("Authorization");
        userFacadeService.logout(accessToken);
        return ApiResponse.ok(Response.Status.CREATE);
    }

    /**
     * 토큰 재발급
     */
    @PostMapping("/refresh")
    public ApiResponse<TokenDto> reissue(HttpServletRequest request) {
        String refreshToken = request.getHeader("Authorization");
        return ApiResponse.ok(Response.Status.CREATE, userFacadeService.reissue(refreshToken));
    }
}
