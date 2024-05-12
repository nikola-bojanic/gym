package com.nikolabojanic.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class TrainerUpdateResponseDTOTest {
    @Test
    void getterAndSetterTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        Boolean isActive = false;
        List<TrainerTraineeResponseDTO> trainees = List.of(new TrainerTraineeResponseDTO());
        TrainerUpdateResponseDTO responseDTO = new TrainerUpdateResponseDTO();
        //when
        responseDTO.setUsername(username);
        responseDTO.setFirstName(firstName);
        responseDTO.setLastName(lastName);
        responseDTO.setSpecializationId(specializationId);
        responseDTO.setIsActive(isActive);
        responseDTO.setTrainees(trainees);
        //then
        assertThat(responseDTO.getUsername()).isEqualTo(username);
        assertThat(responseDTO.getFirstName()).isEqualTo(firstName);
        assertThat(responseDTO.getLastName()).isEqualTo(lastName);
        assertThat(responseDTO.getSpecializationId()).isEqualTo(specializationId);
        assertThat(responseDTO.getIsActive()).isEqualTo(isActive);
        assertThat(responseDTO.getTrainees()).isEqualTo(trainees);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        Boolean isActive = false;
        List<TrainerTraineeResponseDTO> trainees = List.of(new TrainerTraineeResponseDTO());
        //when
        TrainerUpdateResponseDTO responseDTO = new TrainerUpdateResponseDTO(
                username,
                firstName,
                lastName,
                specializationId,
                isActive,
                trainees);
        //then
        assertThat(responseDTO.getUsername()).isEqualTo(username);
        assertThat(responseDTO.getFirstName()).isEqualTo(firstName);
        assertThat(responseDTO.getLastName()).isEqualTo(lastName);
        assertThat(responseDTO.getSpecializationId()).isEqualTo(specializationId);
        assertThat(responseDTO.getIsActive()).isEqualTo(isActive);
        assertThat(responseDTO.getTrainees()).isEqualTo(trainees);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TrainerUpdateResponseDTO responseDTO = new TrainerUpdateResponseDTO();
        //then
        assertThat(responseDTO.getUsername()).isNull();
        assertThat(responseDTO.getFirstName()).isNull();
        assertThat(responseDTO.getLastName()).isNull();
        assertThat(responseDTO.getSpecializationId()).isNull();
        assertThat(responseDTO.getIsActive()).isNull();
        assertThat(responseDTO.getTrainees()).isNull();
    }
}
