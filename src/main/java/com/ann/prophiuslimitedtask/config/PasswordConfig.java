package com.ann.prophiuslimitedtask.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

/* This configuration class is used to create a PasswordEncoder bean,
specifically the BCryptPasswordEncoder, which is used to securely encode passwords.
It's an essential component for user authentication and registration processes.
 */
