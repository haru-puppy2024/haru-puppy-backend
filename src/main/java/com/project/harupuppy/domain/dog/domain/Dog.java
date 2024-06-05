package com.project.harupuppy.domain.dog.domain;

import com.project.harupuppy.domain.dog.dto.DogUpdateRequest;
import com.project.harupuppy.global.utils.DateUtils;
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

//    @OneToOne
//    @JoinColumn(name = "home_id")
//    private Home home;

    @Builder
    public Dog(
            String name, String imgUrl, DogGender gender, LocalDate birthday, Double weight) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.gender = gender;
        this.birthday = birthday;
        this.weight = weight;
//        this.home = home;
    }

    public void update(DogUpdateRequest request) {
        DateUtils.validateDate(request.birthday());
        String formattedValue = String.format("%.1f", request.weight());
        this.name = request.name();
        this.imgUrl = request.imgUrl();
        this.gender = request.gender();
        this.birthday = DateUtils.parseDate(request.birthday());
        this.weight = Double.parseDouble(formattedValue);
    }
}
