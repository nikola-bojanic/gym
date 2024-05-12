package com.nikolabojanic.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerTraineeResponseDtoTest {
    @Test
    void getterAndSetterTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        TrainerTraineeResponseDto responseDto = new TrainerTraineeResponseDto();
        //when
        responseDto.setUsername(username);
        responseDto.setFirstName(firstName);
        responseDto.setLastName(lastName);
        //then
        assertThat(responseDto.getUsername()).isEqualTo(username);
        assertThat(responseDto.getFirstName()).isEqualTo(firstName);
        assertThat(responseDto.getLastName()).isEqualTo(lastName);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        //when
        TrainerTraineeResponseDto responseDto = new TrainerTraineeResponseDto(username, firstName, lastName);
        //then
        assertThat(responseDto.getUsername()).isEqualTo(username);
        assertThat(responseDto.getFirstName()).isEqualTo(firstName);
        assertThat(responseDto.getLastName()).isEqualTo(lastName);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TrainerTraineeResponseDto responseDto = new TrainerTraineeResponseDto();
        //then
        assertThat(responseDto.getUsername()).isNull();
        assertThat(responseDto.getFirstName()).isNull();
        assertThat(responseDto.getLastName()).isNull();
    }
}
