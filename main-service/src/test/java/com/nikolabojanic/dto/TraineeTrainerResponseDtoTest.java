package com.nikolabojanic.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraineeTrainerResponseDtoTest {
    @Test
    void getterAndSetterTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        TraineeTrainerResponseDto responseDto = new TraineeTrainerResponseDto();
        //when
        responseDto.setUsername(username);
        responseDto.setFirstName(firstName);
        responseDto.setLastName(lastName);
        responseDto.setSpecializationId(specializationId);
        //then
        assertThat(responseDto.getFirstName()).isEqualTo(firstName);
        assertThat(responseDto.getLastName()).isEqualTo(lastName);
        assertThat(responseDto.getUsername()).isEqualTo(username);
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
        TraineeTrainerResponseDto responseDto = new TraineeTrainerResponseDto(
            username,
            firstName,
            lastName,
            specializationId);
        //then
        assertThat(responseDto.getFirstName()).isEqualTo(firstName);
        assertThat(responseDto.getLastName()).isEqualTo(lastName);
        assertThat(responseDto.getUsername()).isEqualTo(username);
        assertThat(responseDto.getSpecializationId()).isEqualTo(specializationId);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TraineeTrainerResponseDto responseDto = new TraineeTrainerResponseDto();
        //then
        assertThat(responseDto.getFirstName()).isNull();
        assertThat(responseDto.getLastName()).isNull();
        assertThat(responseDto.getUsername()).isNull();
        assertThat(responseDto.getSpecializationId()).isNull();
    }
}