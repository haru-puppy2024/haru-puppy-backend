package com.project.harupuppy.domain.home.repository;

import com.project.harupuppy.domain.home.domain.Home;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HomeRepository extends JpaRepository<Home, String> {
    Optional<Home> findByHomeId(String homeId);
}
