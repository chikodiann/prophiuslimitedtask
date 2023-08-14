package com.ann.prophiuslimitedtask.config;

import com.ann.prophiuslimitedtask.common.CustomAccessDeniedHandler;
import com.ann.prophiuslimitedtask.common.CustomAuthenticationEntryPoint;
import com.ann.prophiuslimitedtask.common.CustomAuthenticationProvider;
import com.ann.prophiuslimitedtask.filter.JwtAuthorizationFilter;
import com.ann.prophiuslimitedtask.service.CustomUserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/* This is a comprehensive security configuration class that handles authentication and
authorization using Spring Security. It sets up rules for access to different endpoints,
provides authentication providers, and configures filters, such as the JWT authorization filter.
It also defines a list of white-listed URLs that don't require authentication.
This class is crucial for securing your application's endpoints.
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
    //inject required beans and components
    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomUserDetailService userDetailService;
    private final CustomAuthenticationProvider authenticationProvider;

    // URLs that do not require authentication
    public static final String[] WHITE_LIST_URLS = {
            "/api/v1/signup",
            "/api/v1/login",
            "/api/v1/forgot-password",
            "/api/v1/reset-password/{token}",
            "/v2/api-docs",
            "/configuration/ui",
            "/swagger-resources/**",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> {
                    authorize.requestMatchers(WHITE_LIST_URLS)
                            .permitAll()
                            .anyRequest()
                            .authenticated();
                })
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
    @Bean
    public CustomAuthenticationProvider authenticationProvider() {
        return new CustomAuthenticationProvider(userDetailService);
    }

}
