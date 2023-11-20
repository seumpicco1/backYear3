package com.example.intat3;


import com.example.intat3.Properties.JwtProperties;
import com.example.intat3.repositories.UserRepository;
import com.example.intat3.validation.UniqueValidator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
@EnableScheduling
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

    @Bean
    public JavaMailSender getJavaMailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
        mailSender.setUsername("intproj222at3@gmail.com");
        mailSender.setPassword("zqqehkrmsuorgyye");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
