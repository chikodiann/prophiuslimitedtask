package com.ann.prophiuslimitedtask.service;

import com.ann.prophiuslimitedtask.common.UserPrincipal;
import com.ann.prophiuslimitedtask.entity.User;
import com.ann.prophiuslimitedtask.exception.UserNotFoundException;
import com.ann.prophiuslimitedtask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        User user = optionalUser.orElseThrow(() ->
                new UsernameNotFoundException("No user exists with this email.")
        );
        return new UserPrincipal(user);
    }

}
