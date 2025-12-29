package com.studing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class StudyingApplication {
    public static void main(String[] args) {
        SpringApplication.run(StudyingApplication.class, args);
    }
}