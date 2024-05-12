package com.nikolabojanic.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class TrainerTrainingResponseDTOTest {
    @Test
    void getterAndSetterTest() {
        //given
        String name = RandomStringUtils.randomAlphabetic(5);
        LocalDate date = LocalDate.of(2023, 12, 31);
        Long trainingTypeId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        Double duration = Double.parseDouble(RandomStringUtils.randomNumeric(5));
        String traineeName = RandomStringUtils.randomAlphabetic(5);
        TrainerTrainingResponseDTO responseDTO = new TrainerTrainingResponseDTO();
        //when
        responseDTO.setName(name);
        responseDTO.setDate(date);
        responseDTO.setTrainingTypeId(trainingTypeId);
        responseDTO.setDuration(duration);
        responseDTO.setTraineeName(traineeName);
        //then
        assertThat(responseDTO.getName()).isEqualTo(name);
        assertThat(responseDTO.getDate()).isEqualTo(date);
        assertThat(responseDTO.getTrainingTypeId()).isEqualTo(trainingTypeId);
        assertThat(responseDTO.getDuration()).isEqualTo(duration);
        assertThat(responseDTO.getTraineeName()).isEqualTo(traineeName);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String name = RandomStringUtils.randomAlphabetic(5);
        LocalDate date = LocalDate.of(2023, 12, 31);
        Long trainingTypeId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        Double duration = Double.parseDouble(RandomStringUtils.randomNumeric(5));
        String traineeName = RandomStringUtils.randomAlphabetic(5);
        //when
        TrainerTrainingResponseDTO responseDTO = new TrainerTrainingResponseDTO(
                name,
                date,
                trainingTypeId,
                duration,
                traineeName);
        //then
        assertThat(responseDTO.getName()).isEqualTo(name);
        assertThat(responseDTO.getDate()).isEqualTo(date);
        assertThat(responseDTO.getTrainingTypeId()).isEqualTo(trainingTypeId);
        assertThat(responseDTO.getDuration()).isEqualTo(duration);
        assertThat(responseDTO.getTraineeName()).isEqualTo(traineeName);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TrainerTrainingResponseDTO responseDTO = new TrainerTrainingResponseDTO();
        //then
        assertThat(responseDTO.getName()).isNull();
        assertThat(responseDTO.getDate()).isNull();
        assertThat(responseDTO.getTrainingTypeId()).isNull();
        assertThat(responseDTO.getDuration()).isNull();
        assertThat(responseDTO.getTraineeName()).isNull();
    }
}
