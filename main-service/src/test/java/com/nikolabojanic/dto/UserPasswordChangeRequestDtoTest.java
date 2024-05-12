package com.nikolabojanic.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserPasswordChangeRequestDtoTest {
    @Test
    void getterAndSetterTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(4, 7);
        String oldPassword = RandomStringUtils.randomAlphabetic(4, 7);
        String newPassword = RandomStringUtils.randomAlphabetic(4, 7);
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto();
        //when
        requestDto.setUsername(username);
        requestDto.setOldPassword(oldPassword);
        requestDto.setNewPassword(newPassword);
        //then
        assertThat(requestDto.getUsername()).isEqualTo(username);
        assertThat(requestDto.getOldPassword()).isEqualTo(oldPassword);
        assertThat(requestDto.getNewPassword()).isEqualTo(newPassword);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(4, 7);
        String oldPassword = RandomStringUtils.randomAlphabetic(4, 7);
        String newPassword = RandomStringUtils.randomAlphabetic(4, 7);
        //when
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto(username, oldPassword, newPassword);
        //then
        assertThat(requestDto.getUsername()).isEqualTo(username);
        assertThat(requestDto.getOldPassword()).isEqualTo(oldPassword);
        assertThat(requestDto.getNewPassword()).isEqualTo(newPassword);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto();
        //then
        assertThat(requestDto.getUsername()).isNull();
        assertThat(requestDto.getOldPassword()).isNull();
        assertThat(requestDto.getNewPassword()).isNull();
    }
}