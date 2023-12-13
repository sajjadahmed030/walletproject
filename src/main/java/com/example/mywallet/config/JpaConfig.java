package com.example.mywallet.config;

import com.example.mywallet.entities.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class JpaConfig {
    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication == null || !authentication.isAuthenticated()) {
                return Optional.of("System");
            }

            Object principal = authentication.getPrincipal();

            if (principal instanceof User) {
                User user = (User) principal;
                System.out.println(user.getUsername());
                System.out.println(user.getEmail());
                return Optional.of(user.getUsername());
            } else {
                // Handle the case where the principal is not of type User
                return Optional.of("System");
            }
        };


    }
}
