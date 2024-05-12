package com.nikolabojanic.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class TrainingResponseDTOTest {
    @Test
    void getterAndSetterTest() {
        //given
        String traineeUsername = RandomStringUtils.randomAlphabetic(5);
        String trainerUsername = RandomStringUtils.randomAlphabetic(5);
        String name = RandomStringUtils.randomAlphabetic(5);
        LocalDate date = LocalDate.of(2023, 12, 31);
        Double duration = Double.parseDouble(RandomStringUtils.randomNumeric(5));
        TrainingResponseDTO responseDTO = new TrainingResponseDTO();
        //when
        responseDTO.setTraineeUsername(traineeUsername);
        responseDTO.setTrainerUsername(trainerUsername);
        responseDTO.setName(name);
        responseDTO.setDate(date);
        responseDTO.setDuration(duration);
        //then
        assertThat(responseDTO.getTraineeUsername()).isEqualTo(traineeUsername);
        assertThat(responseDTO.getTrainerUsername()).isEqualTo(trainerUsername);
        assertThat(responseDTO.getName()).isEqualTo(name);
        assertThat(responseDTO.getDate()).isEqualTo(date);
        assertThat(responseDTO.getDuration()).isEqualTo(duration);
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
        TrainingResponseDTO responseDTO = new TrainingResponseDTO(
                traineeUsername,
                trainerUsername,
                name,
                date,
                duration);
        //then
        assertThat(responseDTO.getTraineeUsername()).isEqualTo(traineeUsername);
        assertThat(responseDTO.getTrainerUsername()).isEqualTo(trainerUsername);
        assertThat(responseDTO.getName()).isEqualTo(name);
        assertThat(responseDTO.getDate()).isEqualTo(date);
        assertThat(responseDTO.getDuration()).isEqualTo(duration);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TrainingResponseDTO responseDTO = new TrainingResponseDTO();
        //then
        assertThat(responseDTO.getTraineeUsername()).isNull();
        assertThat(responseDTO.getTrainerUsername()).isNull();
        assertThat(responseDTO.getName()).isNull();
        assertThat(responseDTO.getDate()).isNull();
        assertThat(responseDTO.getDuration()).isNull();
    }
}
