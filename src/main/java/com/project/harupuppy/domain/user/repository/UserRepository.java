package com.project.harupuppy.domain.user.repository;

import com.project.harupuppy.domain.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUserId (Long userId);

    Optional<User> findByEmail(String email);

    @Query("SELECT COUNT(u) FROM User u WHERE u.home.homeId = :homeId")
    long countByHomeId(@Param("homeId") String homeId);
}
