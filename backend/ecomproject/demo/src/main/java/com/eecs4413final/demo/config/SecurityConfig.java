package com.eecs4413final.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disables CSRF protection for simplicity
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/index",
                                "/api/users/register",
                                "/hello",
                                "api/categories/**",
                                "/api/categories/**",
                                "/api/categories/get/id/**",
                                "/api/categories/get/name/**",

//                                "api/categories/add",
//                                "api/categories/get/{id}",
//                                "api/categories/del/{id}",
//                                "api/categories/get/{name}",
                                "api/products/all",
                                "api/products/add",
                                "api/products/get/{id}",
                                "api/products/del/{id}"
                        ).permitAll() // Public endpoints
                        .anyRequest().authenticated() // Secures all other endpoints
                )
                .httpBasic(); // Enables HTTP Basic Authentication

        return http.build();
    }
}
