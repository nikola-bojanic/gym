package com.nikolabojanic.service.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoginAttemptServiceTest {
    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private LoginAttemptService loginAttemptService;

    @Test
    void loginFailedTest() throws ExecutionException {
        //arrange
        String key = RandomStringUtils.random(3);
        //act
        loginAttemptService.loginFailed(key);
        //assert
        assertThat(loginAttemptService.getAttemptsCache().get(key)).isEqualTo(1);

    }

    @Test
    void isBlockedTest() {
        //arrange
        String remoteAddr = RandomStringUtils.randomNumeric(5);
        when(request.getRemoteAddr()).thenReturn(remoteAddr);
        loginAttemptService.getAttemptsCache().put(remoteAddr, 3);
        //assert
        assertThat(loginAttemptService.isBlocked()).isTrue();
    }

}