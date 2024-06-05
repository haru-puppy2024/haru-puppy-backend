package com.project.harupuppy.domain.user.application;

import com.project.harupuppy.domain.user.domain.UserDetail;
import com.project.harupuppy.domain.user.dto.TokenDto;
import com.project.harupuppy.domain.home.dto.HomeCreateRequest;
import com.project.harupuppy.domain.user.dto.request.UserCreateRequest;
import com.project.harupuppy.domain.user.dto.response.LoginResponse;
import com.project.harupuppy.domain.user.dto.response.OAuthLoginResponse;
import com.project.harupuppy.domain.user.dto.response.UserCreateResponse;
import com.project.harupuppy.domain.user.dto.response.UserDetailResponse;
import com.project.harupuppy.global.common.exception.CustomException;
import com.project.harupuppy.global.common.response.Response;
import com.project.harupuppy.global.utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserFacadeService {

    private final OAuthService oAuthService;
    private final UserService userService;
    private final RedisService redisService;
    private final JwtTokenUtils jwtTokenUtils;

    @Value("${jwt.refresh-expired-time-ms}")
    private Long refreshExpiredTimeMs;

    /**
     * oAuth 로그인
     */
    @Transactional
    public LoginResponse login(String provider, String code) {
        OAuthLoginResponse response = oAuthService.login(provider, code);

        if (response.isAlreadyRegistered()) {
            redisService.deleteValuesByUserId(String.valueOf(response.registeredUser().userId()));
            TokenDto token = createTokenAndSave(response.registeredUser());
            return new LoginResponse(response, token.accessToken(), token.refreshToken());
        }

        return LoginResponse.of(response);
    }

    /**
     * 회원 가입
     */
    @Transactional
    public UserCreateResponse create(HomeCreateRequest request) {
        UserCreateResponse userDetail = userService.create(request);
        TokenDto token = createTokenAndSave(userDetail.getUserResponse());
        userDetail.setToken(token);
        return userDetail;
    }

    /**
     * 초대 유저 회원 가입
     */
    @Transactional
    public UserCreateResponse create(UserCreateRequest request, String homeId) {
        UserCreateResponse userDetail = userService.create(request, homeId);
        TokenDto token = createTokenAndSave(userDetail.getUserResponse());
        userDetail.setToken(token);
        return userDetail;
    }

    @Transactional
    public TokenDto createTokenAndSave(UserDetailResponse response) {
        TokenDto token = jwtTokenUtils.generateToken(response);
        redisService.setValueWithDuration(token.tokenKey(), token.refreshToken(), Duration.ofMillis(refreshExpiredTimeMs));
        return token;
    }

    /**
     * 유저 탈퇴
     */
    @Transactional
    public void withdraw(UserDetail requestUser){
        String email = userService.withdraw(requestUser.getUserId());
        redisService.deleteValue(email);
    }

    /**
     * 토큰 재발급
     */
    @Transactional
    public TokenDto reissue(String refreshToken){
        final String validToken = validateToken(refreshToken);
        Long userId = jwtTokenUtils.resolveUserId(validToken);
        String key = jwtTokenUtils.resolveTokenKey(validToken);

        if(!redisService.getValues(key).map(value -> value.equals(validToken)).orElse(false)) {
            throw new CustomException(Response.ErrorCode.INVALID_TOKEN);
        }

        UserDetail userDetail = userService.loadByUserId(userId);
        TokenDto newToken = createTokenAndSave(UserDetailResponse.of(userDetail));
        redisService.deleteValue(key);
        return newToken;
    }

    /**
     * 로그아웃
     */
    @Transactional
    public void logout(String accessToken){
        final String validToken = validateToken(accessToken);
        String key = jwtTokenUtils.resolveTokenKey(validToken);
        redisService.deleteValue(key);
    }

    private String validateToken(String refreshToken){
        if(!refreshToken.startsWith("Bearer ")){
            throw new CustomException(Response.ErrorCode.INVALID_TOKEN);
        }

        refreshToken = refreshToken.split(" ")[1];
        if(jwtTokenUtils.isExpired(refreshToken)) {
            throw new CustomException(Response.ErrorCode.EXPIRED_TOKEN);
        }

        return refreshToken;
    }
}
