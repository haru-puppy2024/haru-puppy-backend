package com.project.harupuppy.domain.home.domain;

import com.project.harupuppy.domain.dog.domain.Dog;
import com.project.harupuppy.domain.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "HOME")
@ToString(exclude = "mates")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Home {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "home_id", updatable = false)
    private String homeId;

    @Column(name = "home_name", nullable = false)
    private String homeName;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "dog_id")
    private Dog dog;

    @OneToMany(mappedBy = "home", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
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
