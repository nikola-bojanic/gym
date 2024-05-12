package com.nikolabojanic.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraineeUpdateRequestDtoTest {
    @Test
    void getterAndSetterTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        LocalDate dateOfBirth = LocalDate.of(2023, 12, 31);
        String address = RandomStringUtils.randomAlphabetic(5);
        Boolean isActive = false;
        TraineeUpdateRequestDto requestDto = new TraineeUpdateRequestDto();
        //when
        requestDto.setUsername(username);
        requestDto.setFirstName(firstName);
        requestDto.setLastName(lastName);
        requestDto.setDateOfBirth(dateOfBirth);
        requestDto.setAddress(address);
        requestDto.setIsActive(isActive);
        //then
        assertThat(requestDto.getUsername()).isEqualTo(username);
        assertThat(requestDto.getFirstName()).isEqualTo(firstName);
        assertThat(requestDto.getLastName()).isEqualTo(lastName);
        assertThat(requestDto.getDateOfBirth()).isEqualTo(dateOfBirth);
        assertThat(requestDto.getAddress()).isEqualTo(address);
        assertThat(requestDto.getIsActive()).isEqualTo(isActive);
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
        TraineeUpdateRequestDto requestDto = new TraineeUpdateRequestDto(
            username,
            firstName,
            lastName,
            dateOfBirth,
            address,
            isActive);
        //then
        assertThat(requestDto.getUsername()).isEqualTo(username);
        assertThat(requestDto.getFirstName()).isEqualTo(firstName);
        assertThat(requestDto.getLastName()).isEqualTo(lastName);
        assertThat(requestDto.getDateOfBirth()).isEqualTo(dateOfBirth);
        assertThat(requestDto.getAddress()).isEqualTo(address);
        assertThat(requestDto.getIsActive()).isEqualTo(isActive);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TraineeUpdateRequestDto requestDto = new TraineeUpdateRequestDto();
        //then
        assertThat(requestDto.getUsername()).isNull();
        assertThat(requestDto.getFirstName()).isNull();
        assertThat(requestDto.getLastName()).isNull();
        assertThat(requestDto.getDateOfBirth()).isNull();
        assertThat(requestDto.getAddress()).isNull();
        assertThat(requestDto.getIsActive()).isNull();
    }
}