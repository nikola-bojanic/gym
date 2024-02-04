package com.nikolabojanic.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ActiveTrainerResponseDtoTest {
    @Test
    void getterAndSetterTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        ActiveTrainerResponseDto responseDto = new ActiveTrainerResponseDto();
        //when
        responseDto.setUsername(username);
        responseDto.setFirstName(firstName);
        responseDto.setLastName(lastName);
        responseDto.setSpecializationId(specializationId);
        //then
        assertThat(responseDto.getUsername()).isEqualTo(username);
        assertThat(responseDto.getFirstName()).isEqualTo(firstName);
        assertThat(responseDto.getLastName()).isEqualTo(lastName);
        assertThat(responseDto.getSpecializationId()).isEqualTo(specializationId);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        //when
        ActiveTrainerResponseDto responseDto = new ActiveTrainerResponseDto(
            username,
            firstName,
            lastName,
            specializationId);
        //then
        assertThat(responseDto.getUsername()).isEqualTo(username);
        assertThat(responseDto.getFirstName()).isEqualTo(firstName);
        assertThat(responseDto.getLastName()).isEqualTo(lastName);
        assertThat(responseDto.getSpecializationId()).isEqualTo(specializationId);

    }

    @Test
    void noArgsConstructorTest() {
        //when
        ActiveTrainerResponseDto responseDto = new ActiveTrainerResponseDto();
        //then
        assertThat(responseDto.getSpecializationId()).isNull();
        assertThat(responseDto.getUsername()).isNull();
        assertThat(responseDto.getFirstName()).isNull();
        assertThat(responseDto.getLastName()).isNull();
    }
}