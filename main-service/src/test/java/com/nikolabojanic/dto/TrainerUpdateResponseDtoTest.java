package com.nikolabojanic.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerUpdateResponseDtoTest {
    @Test
    void getterAndSetterTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        Boolean isActive = false;
        List<TrainerTraineeResponseDto> trainees = List.of(new TrainerTraineeResponseDto());
        TrainerUpdateResponseDto responseDto = new TrainerUpdateResponseDto();
        //when
        responseDto.setUsername(username);
        responseDto.setFirstName(firstName);
        responseDto.setLastName(lastName);
        responseDto.setSpecializationId(specializationId);
        responseDto.setIsActive(isActive);
        responseDto.setTrainees(trainees);
        //then
        assertThat(responseDto.getUsername()).isEqualTo(username);
        assertThat(responseDto.getFirstName()).isEqualTo(firstName);
        assertThat(responseDto.getLastName()).isEqualTo(lastName);
        assertThat(responseDto.getSpecializationId()).isEqualTo(specializationId);
        assertThat(responseDto.getIsActive()).isEqualTo(isActive);
        assertThat(responseDto.getTrainees()).isEqualTo(trainees);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        Boolean isActive = false;
        List<TrainerTraineeResponseDto> trainees = List.of(new TrainerTraineeResponseDto());
        //when
        TrainerUpdateResponseDto responseDto = new TrainerUpdateResponseDto(
            username,
            firstName,
            lastName,
            specializationId,
            isActive,
            trainees);
        //then
        assertThat(responseDto.getUsername()).isEqualTo(username);
        assertThat(responseDto.getFirstName()).isEqualTo(firstName);
        assertThat(responseDto.getLastName()).isEqualTo(lastName);
        assertThat(responseDto.getSpecializationId()).isEqualTo(specializationId);
        assertThat(responseDto.getIsActive()).isEqualTo(isActive);
        assertThat(responseDto.getTrainees()).isEqualTo(trainees);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TrainerUpdateResponseDto responseDto = new TrainerUpdateResponseDto();
        //then
        assertThat(responseDto.getUsername()).isNull();
        assertThat(responseDto.getFirstName()).isNull();
        assertThat(responseDto.getLastName()).isNull();
        assertThat(responseDto.getSpecializationId()).isNull();
        assertThat(responseDto.getIsActive()).isNull();
        assertThat(responseDto.getTrainees()).isNull();
    }
}
