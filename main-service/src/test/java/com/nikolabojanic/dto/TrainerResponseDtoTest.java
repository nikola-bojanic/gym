package com.nikolabojanic.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerResponseDtoTest {
    @Test
    void getterAndSetterTest() {
        //given
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        Boolean isActive = true;
        List<TrainerTraineeResponseDto> trainees = List.of(new TrainerTraineeResponseDto());
        TrainerResponseDto responseDto = new TrainerResponseDto();
        //when
        responseDto.setFirstName(firstName);
        responseDto.setLastName(lastName);
        responseDto.setSpecializationId(specializationId);
        responseDto.setIsActive(isActive);
        responseDto.setTrainees(trainees);
        //then
        assertThat(responseDto.getFirstName()).isEqualTo(firstName);
        assertThat(responseDto.getLastName()).isEqualTo(lastName);
        assertThat(responseDto.getSpecializationId()).isEqualTo(specializationId);
        assertThat(responseDto.getIsActive()).isEqualTo(isActive);
        assertThat(responseDto.getTrainees()).isEqualTo(trainees);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        Boolean isActive = true;
        List<TrainerTraineeResponseDto> trainees = List.of(new TrainerTraineeResponseDto());
        //when
        TrainerResponseDto responseDto = new TrainerResponseDto(
            firstName,
            lastName,
            specializationId,
            isActive,
            trainees);
        //then
        assertThat(responseDto.getFirstName()).isEqualTo(firstName);
        assertThat(responseDto.getLastName()).isEqualTo(lastName);
        assertThat(responseDto.getSpecializationId()).isEqualTo(specializationId);
        assertThat(responseDto.getIsActive()).isEqualTo(isActive);
        assertThat(responseDto.getTrainees()).isEqualTo(trainees);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TrainerResponseDto responseDto = new TrainerResponseDto();
        //then
        assertThat(responseDto.getFirstName()).isNull();
        assertThat(responseDto.getLastName()).isNull();
        assertThat(responseDto.getSpecializationId()).isNull();
        assertThat(responseDto.getIsActive()).isNull();
        assertThat(responseDto.getTrainees()).isNull();
    }
}