package com.nikolabojanic.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserPasswordChangeRequestDTOTest {
    @Test
    void getterAndSetterTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(4, 7);
        String oldPassword = RandomStringUtils.randomAlphabetic(4, 7);
        String newPassword = RandomStringUtils.randomAlphabetic(4, 7);
        UserPasswordChangeRequestDTO requestDTO = new UserPasswordChangeRequestDTO();
        //when
        requestDTO.setUsername(username);
        requestDTO.setOldPassword(oldPassword);
        requestDTO.setNewPassword(newPassword);
        //then
        assertThat(requestDTO.getUsername()).isEqualTo(username);
        assertThat(requestDTO.getOldPassword()).isEqualTo(oldPassword);
        assertThat(requestDTO.getNewPassword()).isEqualTo(newPassword);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(4, 7);
        String oldPassword = RandomStringUtils.randomAlphabetic(4, 7);
        String newPassword = RandomStringUtils.randomAlphabetic(4, 7);
        //when
        UserPasswordChangeRequestDTO requestDTO = new UserPasswordChangeRequestDTO(username, oldPassword, newPassword);
        //then
        assertThat(requestDTO.getUsername()).isEqualTo(username);
        assertThat(requestDTO.getOldPassword()).isEqualTo(oldPassword);
        assertThat(requestDTO.getNewPassword()).isEqualTo(newPassword);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        UserPasswordChangeRequestDTO requestDTO = new UserPasswordChangeRequestDTO();
        //then
        assertThat(requestDTO.getUsername()).isNull();
        assertThat(requestDTO.getOldPassword()).isNull();
        assertThat(requestDTO.getNewPassword()).isNull();
    }
}