package com.nikolabojanic.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.LoadingCache;
import com.nikolabojanic.dto.AuthDTORequest;
import com.nikolabojanic.entity.TokenEntity;
import com.nikolabojanic.entity.UserEntity;
import com.nikolabojanic.service.UserService;
import com.nikolabojanic.service.security.LoginAttemptService;
import com.nikolabojanic.service.security.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CredentialsAuthenticationFilterTest {
    @Mock
    private LoginAttemptService loginAttemptService;
    @Mock
    private JwtIssuer jwtIssuer;
    @Mock
    private TokenService tokenService;
    @Mock
    private UserService userService;
    @InjectMocks
    private CredentialsAuthenticationFilter authenticationFilter;

    @Test
    void attemptAuthenticationTest() throws ServletException, IOException {
        //arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        Authentication authentication = mock(Authentication.class);
        authenticationFilter.setAuthenticationManager(authenticationManager);
        AuthDTORequest authDTORequest = new AuthDTORequest(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10)
        );
        byte[] requestBytes = new ObjectMapper().writeValueAsBytes(authDTORequest);
        request.setContent(requestBytes);
        when(authenticationManager.authenticate(
                any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        //act
        Authentication result = authenticationFilter.attemptAuthentication(
                request, response);
        //assert
        assertThat(result).isNotNull();
    }

    @Test
    void successfulAuthenticationTest() throws IOException, ServletException {
        //arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);
        UserEntity user = new UserEntity(
                Long.parseLong(RandomStringUtils.randomNumeric(5)),
                RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(5),
                RandomStringUtils.randomAlphabetic(5),
                true
        );
        Map<String, Object> claims = new HashMap<>();
        claims.put("usr", new Object());
        UserPrincipal principal = new UserPrincipal(user.getId(), user.getUsername(), user.getPassword());
        Authentication authentication = new UserPrincipalAuthenticationToken(principal);
        String token = RandomStringUtils.randomAlphabetic(10);
        LoadingCache<String, Integer> loadingCache = mock(LoadingCache.class);
        when(loginAttemptService.getAttemptsCache()).thenReturn(loadingCache);
        when(jwtIssuer.generateJwt(anyLong(), any(Map.class))).thenReturn(token);
        when(userService.findByUsername(principal.getUsername())).thenReturn(user);
        doNothing().when(tokenService).deleteInvalidUserTokens(user.getId(), token);
        when(tokenService.save(any(TokenEntity.class))).thenReturn(new TokenEntity());
        //act
        authenticationFilter.successfulAuthentication(request, response, filterChain, authentication);
        //assert
        verify(loginAttemptService, times(1)).getAttemptsCache();
        verify(jwtIssuer, times(1)).generateJwt(anyLong(), any(Map.class));
        verify(userService, times(1)).findByUsername(principal.getUsername());
        verify(tokenService, times(1)).deleteInvalidUserTokens(user.getId(), token);
        verify(tokenService, times(1)).save(any(TokenEntity.class));
    }

}