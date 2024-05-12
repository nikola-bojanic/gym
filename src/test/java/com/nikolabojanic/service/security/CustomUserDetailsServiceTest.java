package com.nikolabojanic.service.security;

import com.nikolabojanic.entity.UserEntity;
import com.nikolabojanic.service.UserService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

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
        when(userService.findByUsername(anyString())).thenReturn(new UserEntity());
        //act
        UserDetails userPrincipal = userDetailsService.loadUserByUsername(RandomStringUtils.randomAlphabetic(5));
        //assert
        assertThat(userPrincipal).isNotNull();
    }
}