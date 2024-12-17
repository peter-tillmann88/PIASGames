package com.eecs4413final.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${ALLOWED_ORIGIN}")
    private String allowedOrigin;

    public void addCorsMappings(CorsRegistry registry, @Value("${ALLOWED_ORIGIN}") String allowedOrigin) {
        registry.addMapping("/**")
                .allowedOrigins(allowedOrigin)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Authorization", "Content-Type") 
                .allowCredentials(true); 
    }
}
