package com.project.harupuppy.domain.home.dto;

import com.project.harupuppy.domain.dog.dto.DogCreateRequest;
import com.project.harupuppy.domain.user.dto.request.UserCreateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HomeCreateRequest(
        @NotNull @Valid UserCreateRequest userRequest,
        @NotNull @Valid DogCreateRequest dogRequest,
        @NotBlank String homeName
) {
}
