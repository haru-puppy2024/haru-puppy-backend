package com.project.harupuppy.domain.home.dto;

import com.project.harupuppy.domain.user.domain.User;

public record RankingDto(
        Long userId,
        String imgUrl,
        String nickName,
        String userRole,
        int count
) {
    public static RankingDto of(User user, int scheduleCount) {
        return new RankingDto(
                user.getUserId(),
                user.getImgUrl(),
                user.getNickName(),
                user.getUserRole().getDesc(),
                scheduleCount
        );
    }
}