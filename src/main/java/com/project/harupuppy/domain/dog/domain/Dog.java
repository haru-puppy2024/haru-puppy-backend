package com.project.harupuppy.domain.dog.domain;

import com.project.harupuppy.domain.user.domain.Home;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Table(name = "DOG")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NonNull
public class Dog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dogId;

    @Column
    @Size(min = 1, max = 5)
    private String name;

    @Column(name = "img_url")
    private String imgUrl;

    @Enumerated(EnumType.STRING)
    private DogGender gender;

    private LocalDate birthday;

    private Double weight;

    @OneToOne
    @JoinColumn(name = "home_id")
    private Home home;

    @Builder
    public Dog(
            String name, String imgUrl, DogGender gender, LocalDate birthday, Double weight, Home home) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.gender = gender;
        this.birthday = birthday;
        this.weight = weight;
        this.home = home;
    }
}
