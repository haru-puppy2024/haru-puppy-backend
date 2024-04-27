package com.project.harupuppy.domain.dog.dto;

import com.project.harupuppy.domain.dog.domain.DogGender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DogUpdateRequest(
        @NotNull Long dogId,
        @NotBlank String name,
        @NotNull Double weight,
        @NotNull DogGender gender,
        @NotBlank String birthday,
        @NotBlank String imgUrl
) {
}
