package com.nikolabojanic.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class TrainerResponseDTOTest {
    @Test
    void getterAndSetterTest() {
        //given
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        Boolean isActive = true;
        List<TrainerTraineeResponseDTO> trainees = List.of(new TrainerTraineeResponseDTO());
        TrainerResponseDTO responseDTO = new TrainerResponseDTO();
        //when
        responseDTO.setFirstName(firstName);
        responseDTO.setLastName(lastName);
        responseDTO.setSpecializationId(specializationId);
        responseDTO.setIsActive(isActive);
        responseDTO.setTrainees(trainees);
        //then
        assertThat(responseDTO.getFirstName()).isEqualTo(firstName);
        assertThat(responseDTO.getLastName()).isEqualTo(lastName);
        assertThat(responseDTO.getSpecializationId()).isEqualTo(specializationId);
        assertThat(responseDTO.getIsActive()).isEqualTo(isActive);
        assertThat(responseDTO.getTrainees()).isEqualTo(trainees);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        Boolean isActive = true;
        List<TrainerTraineeResponseDTO> trainees = List.of(new TrainerTraineeResponseDTO());
        //when
        TrainerResponseDTO responseDTO = new TrainerResponseDTO(
                firstName,
                lastName,
                specializationId,
                isActive,
                trainees);
        //then
        assertThat(responseDTO.getFirstName()).isEqualTo(firstName);
        assertThat(responseDTO.getLastName()).isEqualTo(lastName);
        assertThat(responseDTO.getSpecializationId()).isEqualTo(specializationId);
        assertThat(responseDTO.getIsActive()).isEqualTo(isActive);
        assertThat(responseDTO.getTrainees()).isEqualTo(trainees);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TrainerResponseDTO responseDTO = new TrainerResponseDTO();
        //then
        assertThat(responseDTO.getFirstName()).isNull();
        assertThat(responseDTO.getLastName()).isNull();
        assertThat(responseDTO.getSpecializationId()).isNull();
        assertThat(responseDTO.getIsActive()).isNull();
        assertThat(responseDTO.getTrainees()).isNull();
    }
}