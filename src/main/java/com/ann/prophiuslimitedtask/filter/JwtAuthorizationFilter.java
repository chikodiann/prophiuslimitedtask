package com.ann.prophiuslimitedtask.filter;

import com.ann.prophiuslimitedtask.common.AppConstants;
import com.ann.prophiuslimitedtask.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/*
The JwtAuthorizationFilter class is a filter used in Spring Security to handle JWT (JSON Web Token)
authorization for incoming HTTP requests.
It intercepts requests, extracts JWT tokens from the Authorization header, validates them,
and establishes user authentication within the security context based on the information contained in the tokens.
 */

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {
    private final JwtTokenService jwtTokenService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Check if the request method is OPTIONS (preflight request)
        if (request.getMethod().equalsIgnoreCase(AppConstants.OPTIONS_HTTP_METHOD)) {
            // Respond with OK status for OPTIONS request
            response.setStatus(HttpStatus.OK.value());
        } else {
            // Get the Authorization header from the request
            String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            // If the header is missing or does not start with the token prefix
            if (authorizationHeader == null || !authorizationHeader.startsWith(AppConstants.TOKEN_PREFIX)) {
                // Continue the filter chain without any modifications
                filterChain.doFilter(request, response);
                return;
            }
            // Extract the token from the header
            String token = authorizationHeader.substring(AppConstants.TOKEN_PREFIX.length());
            // Get the email (subject) from the token
            String email = jwtTokenService.getSubjectFromToken(token);
            // Check if the token is valid and user is not authenticated yet
            if (jwtTokenService.isTokenValid(email, token) &&
                    SecurityContextHolder.getContext().getAuthentication() == null) {
                // Get authorities (roles) from the token
                List<GrantedAuthority> authorities = jwtTokenService.getAuthoritiesFromToken(token);
                // Create an authentication token with email, authorities, and details
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(email, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Set the authentication in the security context
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                SecurityContextHolder.clearContext();
            }
        }
        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
