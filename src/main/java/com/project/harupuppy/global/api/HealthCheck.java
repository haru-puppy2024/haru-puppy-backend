package com.project.harupuppy.global.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HealthCheck {

    @GetMapping("/healthcheck")
    public ResponseEntity<String> healthCheck() {
        System.out.println("true = " + true);
        return ResponseEntity.status(HttpStatus.OK).body("OK");
    }

}