package com.project.harupuppy.domain.dog.dto;

import com.project.harupuppy.domain.dog.domain.Dog;
import com.project.harupuppy.domain.dog.domain.DogGender;
import com.project.harupuppy.global.utils.DateUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record DogCreateRequest(
        @NotBlank String name,
        @NotNull Double weight,
        @NotNull DogGender gender,
        @NotBlank String birthday,
        @NotBlank String imgUrl

) {
    public static Dog fromDto(DogCreateRequest request) {
        DateUtils.validateDate(request.birthday);
        String formattedValue = String.format("%.1f", request.weight);
        return Dog.builder()
                .name(request.name)
                .weight(Double.parseDouble(formattedValue))
                .gender(request.gender)
                .birthday(DateUtils.parseDate(request.birthday))
                .imgUrl(request.imgUrl)
                .build();
    }
}
