package com.nikolabojanic.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class TraineeResponseDTOTest {
    @Test
    void getterAndSetterTest() {
        //given
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        LocalDate dateOfBirth = LocalDate.of(2023, 12, 31);
        String address = RandomStringUtils.randomAlphabetic(5);
        Boolean isActive = false;
        List<TraineeTrainerResponseDTO> trainers = List.of(new TraineeTrainerResponseDTO());
        TraineeResponseDTO responseDTO = new TraineeResponseDTO();
        //when
        responseDTO.setFirstName(firstName);
        responseDTO.setLastName(lastName);
        responseDTO.setDateOfBirth(dateOfBirth);
        responseDTO.setAddress(address);
        responseDTO.setIsActive(isActive);
        responseDTO.setTrainers(trainers);
        //then
        assertThat(responseDTO.getFirstName()).isEqualTo(firstName);
        assertThat(responseDTO.getLastName()).isEqualTo(lastName);
        assertThat(responseDTO.getDateOfBirth()).isEqualTo(dateOfBirth);
        assertThat(responseDTO.getAddress()).isEqualTo(address);
        assertThat(responseDTO.getIsActive()).isEqualTo(isActive);
        assertThat(responseDTO.getTrainers()).isEqualTo(trainers);

    }

    @Test
    void allArgsConstructorTest() {
        //given
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        LocalDate dateOfBirth = LocalDate.of(2023, 12, 31);
        String address = RandomStringUtils.randomAlphabetic(5);
        Boolean isActive = false;
        List<TraineeTrainerResponseDTO> trainers = List.of(new TraineeTrainerResponseDTO());
        //when
        TraineeResponseDTO responseDTO = new TraineeResponseDTO(
                firstName,
                lastName,
                dateOfBirth,
                address,
                isActive,
                trainers);
        //then
        assertThat(responseDTO.getFirstName()).isEqualTo(firstName);
        assertThat(responseDTO.getLastName()).isEqualTo(lastName);
        assertThat(responseDTO.getDateOfBirth()).isEqualTo(dateOfBirth);
        assertThat(responseDTO.getAddress()).isEqualTo(address);
        assertThat(responseDTO.getIsActive()).isEqualTo(isActive);
        assertThat(responseDTO.getTrainers()).isEqualTo(trainers);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TraineeResponseDTO responseDTO = new TraineeResponseDTO();
        //then
        assertThat(responseDTO.getFirstName()).isNull();
        assertThat(responseDTO.getLastName()).isNull();
        assertThat(responseDTO.getDateOfBirth()).isNull();
        assertThat(responseDTO.getAddress()).isNull();
        assertThat(responseDTO.getIsActive()).isNull();
        assertThat(responseDTO.getTrainers()).isNull();
    }
}