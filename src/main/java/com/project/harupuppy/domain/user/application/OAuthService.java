package com.project.harupuppy.domain.user.application;

import com.project.harupuppy.domain.user.dto.response.OAuthLoginResponse;
import com.project.harupuppy.domain.user.dto.response.OAuthTokenResponse;
import com.project.harupuppy.domain.user.dto.response.UserDetailResponse;
import com.project.harupuppy.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class OAuthService {
    private final InMemoryClientRegistrationRepository inMemoryRepository;
    private final UserRepository userRepository;

    /**
     * oAuth 로그인
     */
    @Transactional
    public OAuthLoginResponse login(String providerName, String code) {
        ClientRegistration provider = inMemoryRepository.findByRegistrationId(providerName);
        OAuthTokenResponse oAuthToken = getAccessToken(provider, code);
        Map<String, Object> userAttributes = getUserInfo(provider, oAuthToken);
        String userEmail = (String)((Map<?, ?>)(userAttributes.get("kakao_account"))).get("email");

        AtomicBoolean isAlreadyRegistered = new AtomicBoolean(false);
        AtomicReference<UserDetailResponse> registeredUser = new AtomicReference<>(null);

        userRepository.findByEmail(userEmail).ifPresent(user -> {
            registeredUser.set(UserDetailResponse.of(user));
            isAlreadyRegistered.set(true);
        });

        return new OAuthLoginResponse(userEmail, isAlreadyRegistered.get(), registeredUser.get());
    }

    /**
     * 인가코드로 토큰 발급 요청
     */
    private OAuthTokenResponse getAccessToken(ClientRegistration provider, String code) {
        return WebClient.create()
                .post()
                .uri(provider.getProviderDetails().getTokenUri())
                .headers(header -> {
                    header.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                    header.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
                })
                .bodyValue(getTokenRequest(provider, code))
                .retrieve()
                .bodyToMono(OAuthTokenResponse.class)
                .block();
    }

    private MultiValueMap<String, String> getTokenRequest(ClientRegistration provider, String code) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("code", code);
        formData.add("grant_type", "authorization_code");
        formData.add("redirect_uri", provider.getRedirectUri());
        formData.add("client_secret", provider.getClientSecret());
        formData.add("client_id", provider.getClientId());
        return formData;
    }

    /**
     * 토큰으로 사용자 정보 조회 요청
     */
    private Map<String, Object> getUserInfo(ClientRegistration provider, OAuthTokenResponse token) {
        return WebClient.create()
                .get()
                .uri(provider.getProviderDetails().getUserInfoEndpoint().getUri())
                .headers(header -> header.setBearerAuth(token.accessToken()))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .block();
    }
}
