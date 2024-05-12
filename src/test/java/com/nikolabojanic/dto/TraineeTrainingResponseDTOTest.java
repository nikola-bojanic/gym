package com.nikolabojanic.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class TraineeTrainingResponseDTOTest {
    @Test
    void getterAndSetterTest() {
        //given
        String name = RandomStringUtils.randomAlphabetic(5);
        LocalDate date = LocalDate.of(2023, 12, 31);
        Long trainingTypeId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        Double duration = Double.parseDouble(RandomStringUtils.randomNumeric(5));
        String trainerName = RandomStringUtils.randomAlphabetic(5);
        TraineeTrainingResponseDTO responseDTO = new TraineeTrainingResponseDTO();
        //when
        responseDTO.setName(name);
        responseDTO.setDate(date);
        responseDTO.setTrainingTypeId(trainingTypeId);
        responseDTO.setDuration(duration);
        responseDTO.setTrainerName(trainerName);
        //then
        assertThat(responseDTO.getName()).isEqualTo(name);
        assertThat(responseDTO.getDate()).isEqualTo(date);
        assertThat(responseDTO.getTrainingTypeId()).isEqualTo(trainingTypeId);
        assertThat(responseDTO.getDuration()).isEqualTo(duration);
        assertThat(responseDTO.getTrainerName()).isEqualTo(trainerName);

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
        TraineeTrainingResponseDTO responseDTO = new TraineeTrainingResponseDTO(
                name,
                date,
                trainingTypeId,
                duration,
                trainerName);
        //then
        assertThat(responseDTO.getName()).isEqualTo(name);
        assertThat(responseDTO.getDate()).isEqualTo(date);
        assertThat(responseDTO.getTrainingTypeId()).isEqualTo(trainingTypeId);
        assertThat(responseDTO.getDuration()).isEqualTo(duration);
        assertThat(responseDTO.getTrainerName()).isEqualTo(trainerName);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TraineeTrainingResponseDTO responseDTO = new TraineeTrainingResponseDTO();
        //then
        assertThat(responseDTO.getName()).isNull();
        assertThat(responseDTO.getDate()).isNull();
        assertThat(responseDTO.getTrainingTypeId()).isNull();
        assertThat(responseDTO.getDuration()).isNull();
        assertThat(responseDTO.getTrainerName()).isNull();
    }
}