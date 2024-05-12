package com.nikolabojanic.service.security;

import com.nikolabojanic.config.security.UserPrincipal;
import com.nikolabojanic.entity.UserEntity;
import com.nikolabojanic.exception.ScNotAuthorizedException;
import com.nikolabojanic.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final LoginAttemptService loginAttemptService;
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (loginAttemptService.isBlocked()) {
            throw new ScNotAuthorizedException("blocked");
        }
        UserEntity user = userService.findByUsername(username);
        log.info("Creating user details for user {}", username);
        return new UserPrincipal(user.getId(), user.getUsername(), user.getPassword());
    }
}
