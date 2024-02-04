package com.nikolabojanic.service.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nikolabojanic.entity.TokenEntity;
import com.nikolabojanic.entity.UserEntity;
import com.nikolabojanic.exception.ScEntityNotFoundException;
import com.nikolabojanic.repository.TokenRepository;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {
    @Mock
    private TokenRepository tokenRepository;
    @InjectMocks
    private TokenService tokenService;

    @Test
    void saveTest() {
        //arrange
        UserEntity user = new UserEntity();
        user.setUsername(RandomStringUtils.randomAlphabetic(5));
        TokenEntity token = new TokenEntity();
        token.setUser(user);
        when(tokenRepository.save(token)).thenReturn(new TokenEntity());
        //act
        TokenEntity savedToken = tokenService.save(token);
        //assert
        assertThat(token).isNotNull();
    }


    @Test
    void findByDataTest() {
        //arrange
        when(tokenRepository.findByData(anyString())).thenReturn(Optional.of(new TokenEntity()));
        //act
        TokenEntity token = tokenService.findByData(RandomStringUtils.random(5));
        //assert
        assertThat(token).isNotNull();
    }

    @Test
    void findByNonExistingDataTest() {
        //arrange
        when(tokenRepository.findByData(anyString())).thenReturn(Optional.empty());
        //act
        assertThatThrownBy(() -> tokenService.findByData(RandomStringUtils.random(5)))
            //assert
            .isInstanceOf(ScEntityNotFoundException.class).hasMessage("");
    }

    @Test
    void isValidTokenValid() {
        //arrange
        TokenEntity validToken = new TokenEntity();
        validToken.setExpired(false);
        validToken.setRevoked(false);
        when(tokenRepository.findByData(anyString())).thenReturn(Optional.of(validToken));
        //act
        Boolean isValid = tokenService.isTokenValid(RandomStringUtils.random(5));
        //assert
        assertThat(isValid).isTrue();
    }

    @Test
    void isInvalidTokenValid() {
        //arrange
        TokenEntity validToken = new TokenEntity();
        validToken.setExpired(true);
        validToken.setRevoked(false);
        when(tokenRepository.findByData(anyString())).thenReturn(Optional.of(validToken));
        //act
        Boolean isValid = tokenService.isTokenValid(RandomStringUtils.random(5));
        //assert
        assertThat(isValid).isFalse();
    }

    @Test
    void deleteInvalidUserTokens() {
        //arrange
        doNothing().when(tokenRepository).deleteInvalidTokens(anyLong(), anyString());
        //act
        tokenService.deleteInvalidUserTokens(
            Long.parseLong(RandomStringUtils.randomNumeric(5)),
            RandomStringUtils.random(10));
        //assert
        verify(tokenRepository, times(1)).deleteInvalidTokens(anyLong(), anyString());
    }
}