package com.nikolabojanic.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraineeUpdateResponseDtoTest {
    @Test
    void getterAndSetterTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        LocalDate dateOfBirth = LocalDate.of(2023, 12, 31);
        String address = RandomStringUtils.randomAlphabetic(5);
        Boolean isActive = false;
        List<TraineeTrainerResponseDto> trainers = List.of(new TraineeTrainerResponseDto());
        TraineeUpdateResponseDto responseDto = new TraineeUpdateResponseDto();
        //when
        responseDto.setUsername(username);
        responseDto.setFirstName(firstName);
        responseDto.setLastName(lastName);
        responseDto.setDateOfBirth(dateOfBirth);
        responseDto.setAddress(address);
        responseDto.setIsActive(isActive);
        responseDto.setTrainers(trainers);
        //then
        assertThat(responseDto.getUsername()).isEqualTo(username);
        assertThat(responseDto.getFirstName()).isEqualTo(firstName);
        assertThat(responseDto.getLastName()).isEqualTo(lastName);
        assertThat(responseDto.getDateOfBirth()).isEqualTo(dateOfBirth);
        assertThat(responseDto.getAddress()).isEqualTo(address);
        assertThat(responseDto.getIsActive()).isEqualTo(isActive);
        assertThat(responseDto.getTrainers()).isEqualTo(trainers);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        LocalDate dateOfBirth = LocalDate.of(2023, 12, 31);
        String address = RandomStringUtils.randomAlphabetic(5);
        Boolean isActive = false;
        List<TraineeTrainerResponseDto> trainers = List.of(new TraineeTrainerResponseDto());
        //when
        TraineeUpdateResponseDto responseDto = new TraineeUpdateResponseDto(
            username,
            firstName,
            lastName,
            dateOfBirth,
            address,
            isActive,
            trainers);
        //then
        assertThat(responseDto.getUsername()).isEqualTo(username);
        assertThat(responseDto.getFirstName()).isEqualTo(firstName);
        assertThat(responseDto.getLastName()).isEqualTo(lastName);
        assertThat(responseDto.getDateOfBirth()).isEqualTo(dateOfBirth);
        assertThat(responseDto.getAddress()).isEqualTo(address);
        assertThat(responseDto.getIsActive()).isEqualTo(isActive);
        assertThat(responseDto.getTrainers()).isEqualTo(trainers);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TraineeUpdateResponseDto responseDto = new TraineeUpdateResponseDto();
        //then
        assertThat(responseDto.getUsername()).isNull();
        assertThat(responseDto.getFirstName()).isNull();
        assertThat(responseDto.getLastName()).isNull();
        assertThat(responseDto.getDateOfBirth()).isNull();
        assertThat(responseDto.getAddress()).isNull();
        assertThat(responseDto.getIsActive()).isNull();
        assertThat(responseDto.getTrainers()).isNull();
    }
}
