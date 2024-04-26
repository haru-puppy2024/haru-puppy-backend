package com.project.harupuppy.domain.user.repository;

import com.project.harupuppy.domain.user.domain.Home;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HomeRepository extends JpaRepository<Home, Long> {
    Optional<Home> findByHomeId(String homeId);
}
