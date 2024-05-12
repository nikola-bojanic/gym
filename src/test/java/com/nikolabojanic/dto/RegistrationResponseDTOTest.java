package com.nikolabojanic.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class RegistrationResponseDTOTest {
    @Test
    void getterAndSetterTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String password = RandomStringUtils.randomAlphabetic(5);
        RegistrationResponseDTO responseDTO = new RegistrationResponseDTO();
        //when
        responseDTO.setUsername(username);
        responseDTO.setPassword(password);
        //then
        assertThat(responseDTO.getPassword()).isEqualTo(password);
        assertThat(responseDTO.getUsername()).isEqualTo(username);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String password = RandomStringUtils.randomAlphabetic(5);
        //when
        RegistrationResponseDTO responseDTO = new RegistrationResponseDTO(username, password);
        //then
        assertThat(responseDTO.getPassword()).isEqualTo(password);
        assertThat(responseDTO.getUsername()).isEqualTo(username);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        RegistrationResponseDTO responseDTO = new RegistrationResponseDTO();
        //then
        assertThat(responseDTO.getUsername()).isNull();
        assertThat(responseDTO.getPassword()).isNull();
    }
}