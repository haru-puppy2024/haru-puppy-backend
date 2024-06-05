package com.project.harupuppy.domain.home.dto;

import com.project.harupuppy.domain.user.domain.User;

public record MateDto(
        Long userId,
        String imgUrl,
        String nickName,
        String userRole
) {
    public static MateDto of(User user) {
        return new MateDto(
                user.getUserId(),
                user.getImgUrl(),
                user.getNickName(),
                user.getUserRole().name()
        );
    }
}
