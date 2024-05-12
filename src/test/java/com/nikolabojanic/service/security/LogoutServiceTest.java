package com.nikolabojanic.service.security;

import com.nikolabojanic.entity.TokenEntity;
import com.nikolabojanic.entity.UserEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutServiceTest {
    @Mock
    private TokenService tokenService;
    @InjectMocks
    private LogoutService logoutService;

    @Test
    void logout() {
        //arrange
        UserEntity user = new UserEntity();
        user.setUsername(RandomStringUtils.randomAlphabetic(5));
        TokenEntity recievedToken = new TokenEntity();
        recievedToken.setUser(user);
        String token = "Bearer " + RandomStringUtils.random(5);
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", token);
        MockHttpServletResponse response = new MockHttpServletResponse();
        when(tokenService.findByData(anyString())).thenReturn(recievedToken);
        when(tokenService.save(any(TokenEntity.class))).thenReturn(new TokenEntity());
        //act
        logoutService.logout(request, response, mock(Authentication.class));
        //assert
        verify(tokenService, times(1)).findByData(anyString());
        verify(tokenService, times(1)).save(any(TokenEntity.class));
    }

}