package com.project.harupuppy.domain.user.application;

import com.project.harupuppy.domain.dog.domain.Dog;
import com.project.harupuppy.domain.dog.repository.DogRepository;
import com.project.harupuppy.domain.user.domain.Home;
import com.project.harupuppy.domain.user.domain.User;
import com.project.harupuppy.domain.user.domain.UserDetail;
import com.project.harupuppy.domain.user.dto.UserResponse;
import com.project.harupuppy.domain.user.dto.UserUpdateRequest;
import com.project.harupuppy.domain.user.dto.request.DogCreateRequest;
import com.project.harupuppy.domain.user.dto.request.HomeCreateRequest;
import com.project.harupuppy.domain.user.dto.request.UserCreateRequest;
import com.project.harupuppy.domain.user.dto.response.DogDetailResponse;
import com.project.harupuppy.domain.user.dto.response.HomeDetailResponse;
import com.project.harupuppy.domain.user.dto.response.UserCreateResponse;
import com.project.harupuppy.domain.user.dto.response.UserDetailResponse;
import com.project.harupuppy.domain.user.repository.HomeRepository;
import com.project.harupuppy.domain.user.repository.UserRepository;
import com.project.harupuppy.global.common.exception.CustomException;
import com.project.harupuppy.global.common.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final HomeRepository homeRepository;
    private final DogRepository dogRepository;

    @Transactional
    public UserCreateResponse create(HomeCreateRequest request) {
        Dog dog = DogCreateRequest.fromDto(request.dogRequest());
        DogDetailResponse dogDetail = DogDetailResponse.of(dogRepository.save(dog));

        Home home = Home.builder()
                .dog(dog)
                .homeName(request.homeName())
                .build();
        HomeDetailResponse homeDetail = HomeDetailResponse.of(homeRepository.save(home));

        User user = UserCreateRequest.fromDto(request.userRequest(), home, dog);
        UserDetailResponse userDetail = UserDetailResponse.of(userRepository.save(user));

        return UserCreateResponse.of(userDetail, homeDetail, dogDetail, null);
    }

    @Transactional
    public UserCreateResponse create(UserCreateRequest request, String homeId) {
        Home home = homeRepository.findByHomeId(homeId)
                .orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_HOME));
        Dog dog = home.getDog();
        User invitedUser = UserCreateRequest.fromDto(request, home, dog);
        userRepository.save(invitedUser);
        return UserCreateResponse.of(UserDetailResponse.of(invitedUser), HomeDetailResponse.of(home),
                DogDetailResponse.of(dog), null);
    }

    @Transactional
    public String withdraw(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_USER));
        Home home = user.getHome();

        // 유저가 홈에 혼자 있는 경우, 홈과 도그를 포함하여 삭제
        if (userRepository.countByHomeId(home.getHomeId()) == 1) {
            homeRepository.delete(home);
        } else {
            userRepository.delete(user);
        }

        return user.getEmail();
    }

    @Transactional
    public UserDetail loadByUserId(Long userId) {
        User registedUser = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_USER));
        return UserDetail.of(registedUser);
    }

    @Transactional
    public UserResponse updateUserInformation(UserDetail userDetail, UserUpdateRequest request) {
        User user = userRepository.findUserByUserId(userDetail.getUserId())
                .orElseThrow(() -> new CustomException(Response.ErrorCode.NOT_FOUND_USER));
        user.update(request);
        return UserResponse.of(user);
    }
}

