package com.project.harupuppy.domain.user.dto.request;

import com.project.harupuppy.domain.dog.domain.Dog;
import com.project.harupuppy.domain.home.domain.Home;
import com.project.harupuppy.domain.user.domain.User;
import com.project.harupuppy.domain.user.domain.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(
        @NotBlank String email,
        @NotBlank String nickName,
        @NotBlank String imgUrl,
        @NotNull UserRole userRole
) {
    public static User fromDto(UserCreateRequest request, Home home, Dog dog){
        return User.builder()
                .email(request.email)
                .nickName(request.nickName)
                .userImg(request.imgUrl)
                .userRole(request.userRole)
                .home(home)
                .dog(dog)
                .build();
    }
}
