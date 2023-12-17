package com.example.intat3.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.AntPathMatcher;

import static org.springframework.http.HttpMethod.OPTIONS;
import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    private final AuthenticationConfiguration authConfiguration;

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http
                .cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(customAccessDeniedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(OPTIONS).permitAll() // allow CORS option calls for Swagger UI
                        .requestMatchers("/api/token").permitAll()
                        .requestMatchers("/api/token/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/announcements").permitAll()
//                        .requestMatchers(HttpMethod.GET,"/api/announcements/pages").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/announcements/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/categories").permitAll()
                        .requestMatchers("/api/otps/**").permitAll()
                        .requestMatchers("/api/files/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET,"/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST,"/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT,"/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/users/**").hasRole("ADMIN")
                        .anyRequest().authenticated());
        return http.build();
    }

}
