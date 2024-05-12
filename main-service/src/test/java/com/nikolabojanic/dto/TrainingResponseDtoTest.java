package com.nikolabojanic.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainingResponseDtoTest {
    @Test
    void getterAndSetterTest() {
        //given
        String traineeUsername = RandomStringUtils.randomAlphabetic(5);
        String trainerUsername = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        LocalDate date = LocalDate.of(2023, 12, 31);
        Double duration = Double.parseDouble(RandomStringUtils.randomNumeric(5));
        TrainingResponseDto responseDto = new TrainingResponseDto();
        //when
        responseDto.setTraineeUsername(traineeUsername);
        responseDto.setTrainerUsername(trainerUsername);
        responseDto.setName(name);
        responseDto.setDate(date);
        responseDto.setDuration(duration);
        //then
        assertThat(responseDto.getTraineeUsername()).isEqualTo(traineeUsername);
        assertThat(responseDto.getTrainerUsername()).isEqualTo(trainerUsername);
        assertThat(responseDto.getName()).isEqualTo(name);
        assertThat(responseDto.getDate()).isEqualTo(date);
        assertThat(responseDto.getDuration()).isEqualTo(duration);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(5));
        String traineeUsername = RandomStringUtils.randomAlphabetic(5);
        String trainerUsername = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        LocalDate date = LocalDate.of(2023, 12, 31);
        Double duration = Double.parseDouble(RandomStringUtils.randomNumeric(5));
        //when
        TrainingResponseDto responseDto = new TrainingResponseDto(
            id,
            traineeUsername,
            trainerUsername,
            name,
            date,
            duration);
        //then
        assertThat(responseDto.getId()).isEqualTo(id);
        assertThat(responseDto.getTraineeUsername()).isEqualTo(traineeUsername);
        assertThat(responseDto.getTrainerUsername()).isEqualTo(trainerUsername);
        assertThat(responseDto.getName()).isEqualTo(name);
        assertThat(responseDto.getDate()).isEqualTo(date);
        assertThat(responseDto.getDuration()).isEqualTo(duration);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TrainingResponseDto responseDto = new TrainingResponseDto();
        //then
        assertThat(responseDto.getTraineeUsername()).isNull();
        assertThat(responseDto.getTrainerUsername()).isNull();
        assertThat(responseDto.getName()).isNull();
        assertThat(responseDto.getDate()).isNull();
        assertThat(responseDto.getDuration()).isNull();
    }
}
