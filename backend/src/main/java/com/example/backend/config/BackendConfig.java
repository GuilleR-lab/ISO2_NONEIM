package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class BackendConfig {
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // desactivar CSRF para facilitar peticiones POST desde React
            .cors().and()     // habilitar CORS
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.sameOrigin())) // permitir frames
            .authorizeRequests((requests) -> requests
                .requestMatchers("/api/auth/**", "/h2-console/**").permitAll() // Permitir acceso sin autenticación a /api/usuarios
                .anyRequest().authenticated() // Requiere autenticación para cualquier otra solicitud
            );

        return http.build();
    }
}
