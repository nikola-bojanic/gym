package com.nikolabojanic.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerRegistrationRequestDtoTest {
    @Test
    void getterAndSetterTest() {
        //given
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        TrainerRegistrationRequestDto requestDto = new TrainerRegistrationRequestDto();
        //when
        requestDto.setFirstName(firstName);
        requestDto.setLastName(lastName);
        requestDto.setSpecializationId(specializationId);
        //then
        assertThat(requestDto.getFirstName()).isEqualTo(firstName);
        assertThat(requestDto.getLastName()).isEqualTo(lastName);
        assertThat(requestDto.getSpecializationId()).isEqualTo(specializationId);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        //when
        TrainerRegistrationRequestDto requestDto = new TrainerRegistrationRequestDto(
            firstName,
            lastName,
            specializationId);
        //then
        assertThat(requestDto.getFirstName()).isEqualTo(firstName);
        assertThat(requestDto.getLastName()).isEqualTo(lastName);
        assertThat(requestDto.getSpecializationId()).isEqualTo(specializationId);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TrainerRegistrationRequestDto requestDto = new TrainerRegistrationRequestDto();
        //then
        assertThat(requestDto.getFirstName()).isNull();
        assertThat(requestDto.getLastName()).isNull();
        assertThat(requestDto.getSpecializationId()).isNull();
    }
}
