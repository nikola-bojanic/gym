package com.nikolabojanic.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class RegistrationResponseDtoTest {
    @Test
    void getterAndSetterTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String password = RandomStringUtils.randomAlphabetic(5);
        RegistrationResponseDto responseDto = new RegistrationResponseDto();
        //when
        responseDto.setUsername(username);
        responseDto.setPassword(password);
        //then
        assertThat(responseDto.getPassword()).isEqualTo(password);
        assertThat(responseDto.getUsername()).isEqualTo(username);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String password = RandomStringUtils.randomAlphabetic(5);
        //when
        RegistrationResponseDto responseDto = new RegistrationResponseDto(username, password);
        //then
        assertThat(responseDto.getPassword()).isEqualTo(password);
        assertThat(responseDto.getUsername()).isEqualTo(username);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        RegistrationResponseDto responseDto = new RegistrationResponseDto();
        //then
        assertThat(responseDto.getUsername()).isNull();
        assertThat(responseDto.getPassword()).isNull();
    }
}