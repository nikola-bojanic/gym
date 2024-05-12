package com.nikolabojanic.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class TrainingRequestDTOTest {
    @Test
    void getterAndSetterTest() {
        //given
        String traineeUsername = RandomStringUtils.randomAlphabetic(5);
        String trainerUsername = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        LocalDate date = LocalDate.of(2023, 12, 31);
        Double duration = Double.parseDouble(RandomStringUtils.randomNumeric(5));
        TrainingRequestDTO requestDTO = new TrainingRequestDTO();
        //when
        requestDTO.setTraineeUsername(traineeUsername);
        requestDTO.setTrainerUsername(trainerUsername);
        requestDTO.setName(name);
        requestDTO.setDate(date);
        requestDTO.setDuration(duration);
        //then
        assertThat(requestDTO.getTraineeUsername()).isEqualTo(traineeUsername);
        assertThat(requestDTO.getTrainerUsername()).isEqualTo(trainerUsername);
        assertThat(requestDTO.getName()).isEqualTo(name);
        assertThat(requestDTO.getDate()).isEqualTo(date);
        assertThat(requestDTO.getDuration()).isEqualTo(duration);
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
        TrainingRequestDTO requestDTO = new TrainingRequestDTO(traineeUsername, trainerUsername, name, date, duration);
        //then
        assertThat(requestDTO.getTraineeUsername()).isEqualTo(traineeUsername);
        assertThat(requestDTO.getTrainerUsername()).isEqualTo(trainerUsername);
        assertThat(requestDTO.getName()).isEqualTo(name);
        assertThat(requestDTO.getDate()).isEqualTo(date);
        assertThat(requestDTO.getDuration()).isEqualTo(duration);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TrainingRequestDTO requestDTO = new TrainingRequestDTO();
        //then
        assertThat(requestDTO.getTraineeUsername()).isNull();
        assertThat(requestDTO.getTrainerUsername()).isNull();
        assertThat(requestDTO.getName()).isNull();
        assertThat(requestDTO.getDate()).isNull();
        assertThat(requestDTO.getDuration()).isNull();
    }
}
