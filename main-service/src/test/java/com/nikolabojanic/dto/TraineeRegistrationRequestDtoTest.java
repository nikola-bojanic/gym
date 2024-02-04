package com.nikolabojanic.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraineeRegistrationRequestDtoTest {
    @Test
    void getterAndSetterTest() {
        //given
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        LocalDate dateOfBirth = LocalDate.of(2023, 12, 31);
        String address = RandomStringUtils.randomAlphabetic(5);
        TraineeRegistrationRequestDto requestDto = new TraineeRegistrationRequestDto();
        //when
        requestDto.setFirstName(firstName);
        requestDto.setLastName(lastName);
        requestDto.setAddress(address);
        requestDto.setDateOfBirth(dateOfBirth);
        //then
        assertThat(requestDto.getFirstName()).isEqualTo(firstName);
        assertThat(requestDto.getLastName()).isEqualTo(lastName);
        assertThat(requestDto.getAddress()).isEqualTo(address);
        assertThat(requestDto.getDateOfBirth()).isEqualTo(dateOfBirth);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        LocalDate dateOfBirth = LocalDate.of(2023, 12, 31);
        String address = RandomStringUtils.randomAlphabetic(5);
        //when
        TraineeRegistrationRequestDto requestDto = new TraineeRegistrationRequestDto(
            firstName,
            lastName,
            dateOfBirth,
            address);
        //then
        assertThat(requestDto.getFirstName()).isEqualTo(firstName);
        assertThat(requestDto.getLastName()).isEqualTo(lastName);
        assertThat(requestDto.getAddress()).isEqualTo(address);
        assertThat(requestDto.getDateOfBirth()).isEqualTo(dateOfBirth);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TraineeRegistrationRequestDto requestDto = new TraineeRegistrationRequestDto();
        //then
        assertThat(requestDto.getFirstName()).isNull();
        assertThat(requestDto.getLastName()).isNull();
        assertThat(requestDto.getAddress()).isNull();
        assertThat(requestDto.getDateOfBirth()).isNull();
    }
}