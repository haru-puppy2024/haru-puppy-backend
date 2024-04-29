package com.project.harupuppy.domain.user.domain;

import com.project.harupuppy.domain.dog.domain.Dog;
import com.project.harupuppy.domain.user.dto.UserUpdateRequest;
import com.project.harupuppy.global.utils.KoreanNickname;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "USERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Email
    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "img_url")
    private String imgUrl;

    @KoreanNickname
    @Column(name = "nickname")
    private String nickName;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;

    @Column(name = "is_deleted", columnDefinition = "TINYINT(1)")
    private boolean isDeleted;

    @Column(name = "allow_notification", columnDefinition = "TINYINT(1)")
    private boolean allowNotification;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dog_id")
    private Dog dog;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "home_id")
    private Home home;

    @Builder
    public User(String email, String userImg, String nickName, UserRole userRole, Home home, Dog dog) {
        this.email = email;
        this.imgUrl = userImg;
        this.nickName = nickName;
        this.userRole = userRole;
        this.dog = dog;
        this.home = home;
        this.isDeleted = false;
        this.allowNotification = true;
    }

    public void update(UserUpdateRequest updateRequest) {
        this.nickName = updateRequest.nickName();
        this.userRole = updateRequest.userRole();
    }

    public void setHome(Home home) {
        this.home = home;
    }

    public void setDog(Dog dog) {
        this.dog = dog;
    }

    public void delete() {
        this.isDeleted = true;
    }
}
