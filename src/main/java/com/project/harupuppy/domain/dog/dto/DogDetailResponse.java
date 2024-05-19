package com.project.harupuppy.domain.dog.dto;

import com.project.harupuppy.domain.dog.domain.Dog;
import com.project.harupuppy.domain.dog.domain.DogGender;

import java.time.LocalDate;

public record DogDetailResponse(
        Long dogId,
        String name,
        Double weight,
        DogGender gender,
        LocalDate birthday,
        String imgUrl
) {
    public static DogDetailResponse of(Dog dog){
        return new DogDetailResponse(
            dog.getDogId(),
            dog.getName(),
            dog.getWeight(),
            dog.getGender(),
            dog.getBirthday(),
            dog.getImgUrl()
        );
    }
}
