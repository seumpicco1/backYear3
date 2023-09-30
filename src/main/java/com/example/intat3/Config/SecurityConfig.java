package com.example.intat3.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.OPTIONS;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authConfiguration;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http
                .cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(OPTIONS).permitAll() // allow CORS option calls for Swagger UI
                        .requestMatchers("/api/token").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/announcements/pages").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/categories").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/announcements/{id:\\d+}").permitAll()
                        .anyRequest().authenticated()).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic();

        return http.build();
    }

}