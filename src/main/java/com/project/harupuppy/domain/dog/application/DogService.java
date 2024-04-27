package com.project.harupuppy.domain.dog.application;

import com.project.harupuppy.domain.dog.domain.Dog;
import com.project.harupuppy.domain.dog.dto.DogUpdateRequest;
import com.project.harupuppy.domain.dog.repository.DogRepository;
import com.project.harupuppy.domain.user.dto.response.DogDetailResponse;
import com.project.harupuppy.global.common.exception.CustomException;
import com.project.harupuppy.global.common.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class DogService {
    private final DogRepository dogRepository;

    /**
     * 강아지 프로필 수정
     */
    @Transactional
    public DogDetailResponse updateDogProfile(DogUpdateRequest request) {
        Dog dog = dogRepository.findById(request.dogId())
                .orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_DOG));
        dog.update(request);

        return DogDetailResponse.of(dog);
    }

}
