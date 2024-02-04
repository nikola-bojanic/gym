package com.nikolabojanic.config.security;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nikolabojanic.service.security.LoginAttemptService;
import jakarta.servlet.ServletException;
import java.io.IOException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

@ExtendWith(MockitoExtension.class)
class CustomAuthenticationFailureHandlerTest {
    @Mock
    private LoginAttemptService loginAttemptService;
    @InjectMocks
    private CustomAuthenticationFailureHandler authenticationFailureHandler;

    @Test
    void commenceTest() throws ServletException, IOException {
        //arrange
        AuthenticationException authException = mock(AuthenticationException.class);
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
        httpServletRequest.setRemoteAddr(RandomStringUtils.randomNumeric(5));
        doNothing().when(loginAttemptService).loginFailed(anyString());
        when(loginAttemptService.isBlocked()).thenReturn(true);
        //act
        authenticationFailureHandler.commence(httpServletRequest, httpServletResponse, authException);
        //assert
        verify(loginAttemptService, times(1)).loginFailed(anyString());
        verify(loginAttemptService, times(1)).isBlocked();
    }
}