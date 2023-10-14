package com.example.intat3;

import com.example.intat3.repositories.CustomUserRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomUserRepositoryImpl.class)
public class IntAt3Application {

    public static void main(String[] args) {
        SpringApplication.run(IntAt3Application.class, args);
    }

}
