package com.project.harupuppy;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
public class HaruPuppyApplication {

    public static void main(String[] args) {
        SpringApplication.run(HaruPuppyApplication.class, args);
    }

    @PostConstruct
    void started(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Seoul"));
    }
}
