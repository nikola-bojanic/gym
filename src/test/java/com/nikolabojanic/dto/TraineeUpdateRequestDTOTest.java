package com.nikolabojanic.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class TraineeUpdateRequestDTOTest {
    @Test
    void getterAndSetterTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        LocalDate dateOfBirth = LocalDate.of(2023, 12, 31);
        String address = RandomStringUtils.randomAlphabetic(5);
        Boolean isActive = false;
        TraineeUpdateRequestDTO requestDTO = new TraineeUpdateRequestDTO();
        //when
        requestDTO.setUsername(username);
        requestDTO.setFirstName(firstName);
        requestDTO.setLastName(lastName);
        requestDTO.setDateOfBirth(dateOfBirth);
        requestDTO.setAddress(address);
        requestDTO.setIsActive(isActive);
        //then
        assertThat(requestDTO.getUsername()).isEqualTo(username);
        assertThat(requestDTO.getFirstName()).isEqualTo(firstName);
        assertThat(requestDTO.getLastName()).isEqualTo(lastName);
        assertThat(requestDTO.getDateOfBirth()).isEqualTo(dateOfBirth);
        assertThat(requestDTO.getAddress()).isEqualTo(address);
        assertThat(requestDTO.getIsActive()).isEqualTo(isActive);
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
        //when
        TraineeUpdateRequestDTO requestDTO = new TraineeUpdateRequestDTO(
                username,
                firstName,
                lastName,
                dateOfBirth,
                address,
                isActive);
        //then
        assertThat(requestDTO.getUsername()).isEqualTo(username);
        assertThat(requestDTO.getFirstName()).isEqualTo(firstName);
        assertThat(requestDTO.getLastName()).isEqualTo(lastName);
        assertThat(requestDTO.getDateOfBirth()).isEqualTo(dateOfBirth);
        assertThat(requestDTO.getAddress()).isEqualTo(address);
        assertThat(requestDTO.getIsActive()).isEqualTo(isActive);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TraineeUpdateRequestDTO requestDTO = new TraineeUpdateRequestDTO();
        //then
        assertThat(requestDTO.getUsername()).isNull();
        assertThat(requestDTO.getFirstName()).isNull();
        assertThat(requestDTO.getLastName()).isNull();
        assertThat(requestDTO.getDateOfBirth()).isNull();
        assertThat(requestDTO.getAddress()).isNull();
        assertThat(requestDTO.getIsActive()).isNull();
    }
}