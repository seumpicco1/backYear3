package com.example.intat3;


import com.example.intat3.Properties.JwtProperties;
import com.example.intat3.repositories.UserRepository;
import com.example.intat3.validation.UniqueValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Argon2PasswordEncoder passwordEncode(){
        return new Argon2PasswordEncoder(16, 32, 1, 60000, 10);
    }

    @Autowired
    private UserRepository repository;

    @Bean
    public UniqueValidator uniqueValidator(){
        return new UniqueValidator(repository);
    }
}
