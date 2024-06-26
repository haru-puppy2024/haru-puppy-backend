package com.project.harupuppy.domain.user.dto.response;

public record OAuthLoginResponse(
        String email,
        boolean isAlreadyRegistered,
        UserDetailResponse registeredUser
) {
}
