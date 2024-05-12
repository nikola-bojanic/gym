package com.nikolabojanic.config.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.LoadingCache;
import com.nikolabojanic.dto.AuthDtoRequest;
import com.nikolabojanic.dto.AuthDtoResponse;
import com.nikolabojanic.entity.TokenEntity;
import com.nikolabojanic.entity.UserEntity;
import com.nikolabojanic.enumeration.UserRole;
import com.nikolabojanic.service.UserService;
import com.nikolabojanic.service.security.LoginAttemptService;
import com.nikolabojanic.service.security.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
    @Mock
    private ObjectMapper objectMapper;
    @InjectMocks
    private CredentialsAuthenticationFilter authenticationFilter;

    @Test
    void attemptAuthenticationTest() throws IOException {
        //arrange
        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        Authentication authentication = mock(Authentication.class);
        authenticationFilter.setAuthenticationManager(authenticationManager);
        AuthDtoRequest authDtoRequest = new AuthDtoRequest(
            RandomStringUtils.randomAlphabetic(10),
            RandomStringUtils.randomAlphabetic(10)
        );
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        byte[] requestBytes = new ObjectMapper().writeValueAsBytes(authDtoRequest);
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
        UserEntity user = new UserEntity(
            Long.parseLong(RandomStringUtils.randomNumeric(5)),
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            true,
            UserRole.TRAINEE
        );
        Map<String, Object> claims = new HashMap<>();
        claims.put("usr", new Object());
        UserPrincipal principal = new UserPrincipal(
            user.getId(),
            user.getUsername(),
            user.getPassword(),
            new ArrayList<>());
        String token = RandomStringUtils.randomAlphabetic(10);
        LoadingCache<String, Integer> loadingCache = mock(LoadingCache.class);
        when(loginAttemptService.getAttemptsCache()).thenReturn(loadingCache);
        when(jwtIssuer.generateJwt(anyLong(), any(Map.class))).thenReturn(token);
        when(userService.findByUsername(principal.getUsername())).thenReturn(user);
        doNothing().when(tokenService).deleteInvalidUserTokens(user.getId(), token);
        when(tokenService.save(any(TokenEntity.class))).thenReturn(new TokenEntity());
        when(objectMapper.writeValueAsString(any(AuthDtoResponse.class)))
            .thenReturn(RandomStringUtils.randomAlphabetic(5));
        FilterChain filterChain = mock(FilterChain.class);
        Authentication authentication = new UserPrincipalAuthenticationToken(principal);
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
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
