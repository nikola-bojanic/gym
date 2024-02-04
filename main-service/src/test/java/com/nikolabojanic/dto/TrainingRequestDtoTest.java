package com.nikolabojanic.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainingRequestDtoTest {
    @Test
    void getterAndSetterTest() {
        //given
        String traineeUsername = RandomStringUtils.randomAlphabetic(5);
        String trainerUsername = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        LocalDate date = LocalDate.of(2023, 12, 31);
        Double duration = Double.parseDouble(RandomStringUtils.randomNumeric(5));
        TrainingRequestDto requestDto = new TrainingRequestDto();
        //when
        requestDto.setTraineeUsername(traineeUsername);
        requestDto.setTrainerUsername(trainerUsername);
        requestDto.setName(name);
        requestDto.setDate(date);
        requestDto.setDuration(duration);
        //then
        assertThat(requestDto.getTraineeUsername()).isEqualTo(traineeUsername);
        assertThat(requestDto.getTrainerUsername()).isEqualTo(trainerUsername);
        assertThat(requestDto.getName()).isEqualTo(name);
        assertThat(requestDto.getDate()).isEqualTo(date);
        assertThat(requestDto.getDuration()).isEqualTo(duration);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String traineeUsername = RandomStringUtils.randomAlphabetic(5);
        String trainerUsername = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        LocalDate date = LocalDate.of(2023, 12, 31);
        Double duration = Double.parseDouble(RandomStringUtils.randomNumeric(5));
        //when
        TrainingRequestDto requestDto = new TrainingRequestDto(traineeUsername, trainerUsername, name, date, duration);
        //then
        assertThat(requestDto.getTraineeUsername()).isEqualTo(traineeUsername);
        assertThat(requestDto.getTrainerUsername()).isEqualTo(trainerUsername);
        assertThat(requestDto.getName()).isEqualTo(name);
        assertThat(requestDto.getDate()).isEqualTo(date);
        assertThat(requestDto.getDuration()).isEqualTo(duration);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TrainingRequestDto requestDto = new TrainingRequestDto();
        //then
        assertThat(requestDto.getTraineeUsername()).isNull();
        assertThat(requestDto.getTrainerUsername()).isNull();
        assertThat(requestDto.getName()).isNull();
        assertThat(requestDto.getDate()).isNull();
        assertThat(requestDto.getDuration()).isNull();
    }
}
