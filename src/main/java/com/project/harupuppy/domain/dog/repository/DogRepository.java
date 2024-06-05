package com.project.harupuppy.domain.dog.repository;

import com.project.harupuppy.domain.dog.domain.Dog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DogRepository extends JpaRepository<Dog, Long> {
}
