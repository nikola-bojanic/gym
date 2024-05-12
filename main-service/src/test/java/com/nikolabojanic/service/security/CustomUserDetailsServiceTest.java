package com.nikolabojanic.service.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.nikolabojanic.entity.UserEntity;
import com.nikolabojanic.enumeration.UserRole;
import com.nikolabojanic.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private LoginAttemptService loginAttemptService;
    @InjectMocks
    private CustomUserDetailsService userDetailsService;

    @Test
    void loadUserByUsernameTest() {
        //arrange
        when(loginAttemptService.isBlocked()).thenReturn(false);
        UserEntity user = new UserEntity();
        user.setRole(UserRole.TRAINEE);
        when(userService.findByUsername(anyString())).thenReturn(user);
        //act
        UserDetails userPrincipal = userDetailsService.loadUserByUsername(RandomStringUtils.randomAlphabetic(5));
        //assert
        assertThat(userPrincipal).isNotNull();
    }
}