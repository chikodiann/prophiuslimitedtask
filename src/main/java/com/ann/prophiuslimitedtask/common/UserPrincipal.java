package com.ann.prophiuslimitedtask.common;

import com.ann.prophiuslimitedtask.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Builder
@AllArgsConstructor
public class UserPrincipal implements UserDetails {
    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Mapping user's role to a single authority using SimpleGrantedAuthority
        return List.<GrantedAuthority>of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    //check if account is expired
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //check if account is locked
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    } // Return user's enabled status
}
