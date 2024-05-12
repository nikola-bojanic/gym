package com.nikolabojanic.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class TraineeRegistrationRequestDTOTest {
    @Test
    void getterAndSetterTest() {
        //given
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        LocalDate dateOfBirth = LocalDate.of(2023, 12, 31);
        String address = RandomStringUtils.randomAlphabetic(5);
        TraineeRegistrationRequestDTO requestDTO = new TraineeRegistrationRequestDTO();
        //when
        requestDTO.setFirstName(firstName);
        requestDTO.setLastName(lastName);
        requestDTO.setAddress(address);
        requestDTO.setDateOfBirth(dateOfBirth);
        //then
        assertThat(requestDTO.getFirstName()).isEqualTo(firstName);
        assertThat(requestDTO.getLastName()).isEqualTo(lastName);
        assertThat(requestDTO.getAddress()).isEqualTo(address);
        assertThat(requestDTO.getDateOfBirth()).isEqualTo(dateOfBirth);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        LocalDate dateOfBirth = LocalDate.of(2023, 12, 31);
        String address = RandomStringUtils.randomAlphabetic(5);
        //when
        TraineeRegistrationRequestDTO requestDTO = new TraineeRegistrationRequestDTO(
                firstName,
                lastName,
                dateOfBirth,
                address);
        //then
        assertThat(requestDTO.getFirstName()).isEqualTo(firstName);
        assertThat(requestDTO.getLastName()).isEqualTo(lastName);
        assertThat(requestDTO.getAddress()).isEqualTo(address);
        assertThat(requestDTO.getDateOfBirth()).isEqualTo(dateOfBirth);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TraineeRegistrationRequestDTO requestDTO = new TraineeRegistrationRequestDTO();
        //then
        assertThat(requestDTO.getFirstName()).isNull();
        assertThat(requestDTO.getLastName()).isNull();
        assertThat(requestDTO.getAddress()).isNull();
        assertThat(requestDTO.getDateOfBirth()).isNull();
    }
}