package com.project.harupuppy.domain.user.dto.request;

import com.project.harupuppy.domain.user.domain.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
        @NotNull(message = "유저번호는 필수항목입니다.")
        Long userId,
        @NotBlank(message = "닉네임을 입력해주세요.")
        @Size(max = 8, message = "닉네임은 8글자 이하로 입력해주세요.")
        String nickName,
        @NotNull(message = "역할을 입력해주세요.")
        UserRole userRole,
        String imgUrl
) {
}
