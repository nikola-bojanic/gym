package com.nikolabojanic.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraineeTrainingResponseDtoTest {
    @Test
    void getterAndSetterTest() {
        //given
        String name = RandomStringUtils.randomAlphabetic(5);
        LocalDate date = LocalDate.of(2023, 12, 31);
        Long trainingTypeId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        Double duration = Double.parseDouble(RandomStringUtils.randomNumeric(5));
        String trainerName = RandomStringUtils.randomAlphabetic(5);
        TraineeTrainingResponseDto responseDto = new TraineeTrainingResponseDto();
        //when
        responseDto.setName(name);
        responseDto.setDate(date);
        responseDto.setTrainingTypeId(trainingTypeId);
        responseDto.setDuration(duration);
        responseDto.setTrainerName(trainerName);
        //then
        assertThat(responseDto.getName()).isEqualTo(name);
        assertThat(responseDto.getDate()).isEqualTo(date);
        assertThat(responseDto.getTrainingTypeId()).isEqualTo(trainingTypeId);
        assertThat(responseDto.getDuration()).isEqualTo(duration);
        assertThat(responseDto.getTrainerName()).isEqualTo(trainerName);

    }

    @Test
    void allArgsConstructorTest() {
        //given
        String name = RandomStringUtils.randomAlphabetic(5);
        LocalDate date = LocalDate.of(2023, 12, 31);
        Long trainingTypeId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        Double duration = Double.parseDouble(RandomStringUtils.randomNumeric(5));
        String trainerName = RandomStringUtils.randomAlphabetic(5);
        //when
        TraineeTrainingResponseDto responseDto = new TraineeTrainingResponseDto(
            name,
            date,
            trainingTypeId,
            duration,
            trainerName);
        //then
        assertThat(responseDto.getName()).isEqualTo(name);
        assertThat(responseDto.getDate()).isEqualTo(date);
        assertThat(responseDto.getTrainingTypeId()).isEqualTo(trainingTypeId);
        assertThat(responseDto.getDuration()).isEqualTo(duration);
        assertThat(responseDto.getTrainerName()).isEqualTo(trainerName);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TraineeTrainingResponseDto responseDto = new TraineeTrainingResponseDto();
        //then
        assertThat(responseDto.getName()).isNull();
        assertThat(responseDto.getDate()).isNull();
        assertThat(responseDto.getTrainingTypeId()).isNull();
        assertThat(responseDto.getDuration()).isNull();
        assertThat(responseDto.getTrainerName()).isNull();
    }
}