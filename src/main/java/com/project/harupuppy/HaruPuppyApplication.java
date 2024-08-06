package com.project.harupuppy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HaruPuppyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HaruPuppyApplication.class, args);
    }

}
