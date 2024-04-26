package com.project.harupuppy.domain.user.domain;

import com.project.harupuppy.domain.dog.domain.Dog;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "HOME")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Home {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "home_id", updatable = false)
    private String homeId;

    @Column(name = "home_name")
    private String homeName;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dog_id")
    private Dog dog;

    @OneToMany(mappedBy = "home", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> mates = new ArrayList<>();

    @Builder
    public Home(String homeName, Dog dog) {
        this.homeName = homeName;
        this.dog = dog;
    }

    public void setMates(User user) {
        mates.add(user);
        user.setHome(this);
    }
}
