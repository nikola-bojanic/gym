package com.nikolabojanic.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerTrainingResponseDtoTest {
    @Test
    void getterAndSetterTest() {
        //given
        String name = RandomStringUtils.randomAlphabetic(5);
        LocalDate date = LocalDate.of(2023, 12, 31);
        Long trainingTypeId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        Double duration = Double.parseDouble(RandomStringUtils.randomNumeric(5));
        String traineeName = RandomStringUtils.randomAlphabetic(5);
        TrainerTrainingResponseDto responseDto = new TrainerTrainingResponseDto();
        //when
        responseDto.setName(name);
        responseDto.setDate(date);
        responseDto.setTrainingTypeId(trainingTypeId);
        responseDto.setDuration(duration);
        responseDto.setTraineeName(traineeName);
        //then
        assertThat(responseDto.getName()).isEqualTo(name);
        assertThat(responseDto.getDate()).isEqualTo(date);
        assertThat(responseDto.getTrainingTypeId()).isEqualTo(trainingTypeId);
        assertThat(responseDto.getDuration()).isEqualTo(duration);
        assertThat(responseDto.getTraineeName()).isEqualTo(traineeName);
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
        TrainerTrainingResponseDto responseDto = new TrainerTrainingResponseDto(
            name,
            date,
            trainingTypeId,
            duration,
            traineeName);
        //then
        assertThat(responseDto.getName()).isEqualTo(name);
        assertThat(responseDto.getDate()).isEqualTo(date);
        assertThat(responseDto.getTrainingTypeId()).isEqualTo(trainingTypeId);
        assertThat(responseDto.getDuration()).isEqualTo(duration);
        assertThat(responseDto.getTraineeName()).isEqualTo(traineeName);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TrainerTrainingResponseDto responseDto = new TrainerTrainingResponseDto();
        //then
        assertThat(responseDto.getName()).isNull();
        assertThat(responseDto.getDate()).isNull();
        assertThat(responseDto.getTrainingTypeId()).isNull();
        assertThat(responseDto.getDuration()).isNull();
        assertThat(responseDto.getTraineeName()).isNull();
    }
}
