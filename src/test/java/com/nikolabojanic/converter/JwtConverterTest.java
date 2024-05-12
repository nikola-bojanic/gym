package com.nikolabojanic.converter;

import com.nikolabojanic.config.security.UserPrincipalAuthenticationToken;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JwtConverterTest {
    @InjectMocks
    private JwtConverter jwtConverter;

    @Test
    void convertJwtToUserPrincipal() {
        //arrange
        Jwt jwt = mock(Jwt.class);
        when(jwt.getSubject()).thenReturn(RandomStringUtils.randomNumeric(5));
        when(jwt.getClaim("usr")).thenReturn(RandomStringUtils.randomAlphabetic(5));
        //act
        UserPrincipalAuthenticationToken principal = jwtConverter.convert(jwt);
        //assert
        assertThat(principal).isNotNull();
    }
}