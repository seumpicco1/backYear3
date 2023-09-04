package com.example.intat3;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

@Configuration
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Argon2PasswordEncoder passwordEncode(){
        return new Argon2PasswordEncoder(16, 32, 1, 60000, 10);
    }
}
