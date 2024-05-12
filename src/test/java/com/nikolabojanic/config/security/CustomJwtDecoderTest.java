package com.nikolabojanic.config.security;

import com.nikolabojanic.service.security.TokenService;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomJwtDecoderTest {
    @Mock
    private JwtDecoder jwtDecoder;
    @Mock
    private TokenService tokenService;
    @InjectMocks
    private CustomJwtDecoder customJwtDecoder;

    @Test
    void decodeTokenTest() {
        //arrange
        Jwt jwt = mock(Jwt.class);
        when(tokenService.isTokenValid(anyString())).thenReturn(true);
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);
        //act
        Jwt decoded = customJwtDecoder.decode(RandomStringUtils.randomAlphabetic(10));
        //assert
        assertThat(jwt).isNotNull();
    }

    @Test
    void decodeInvalidTokenTest() {
        //arrange
        when(tokenService.isTokenValid(anyString())).thenReturn(false);
        //act
        assertThatThrownBy(() -> customJwtDecoder.decode(RandomStringUtils.randomAlphabetic(10)))
                //assert
                .isInstanceOf(JwtException.class)
                .hasMessage("Cannot authorize request");

    }

}