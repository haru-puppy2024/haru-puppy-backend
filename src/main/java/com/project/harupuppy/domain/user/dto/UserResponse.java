package com.project.harupuppy.domain.user.dto;

import com.project.harupuppy.domain.user.domain.User;
import com.project.harupuppy.domain.user.domain.UserRole;

public record UserResponse(
        Long userId,
        String email,
        String nickname,
        UserRole userRole,
        boolean isDeleted,
        boolean allowNotification

) {
    public static UserResponse of (User user) {
        return new UserResponse(
                user.getUserId(),
                user.getEmail(),
                user.getNickname(),
                user.getUserRole(),
                user.isDeleted(),
                user.isAllowNotification()
        );
    }
}
