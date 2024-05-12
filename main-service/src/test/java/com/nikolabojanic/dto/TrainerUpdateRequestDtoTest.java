package com.nikolabojanic.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerUpdateRequestDtoTest {
    @Test
    void getterAndSetterTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        Boolean isActive = false;
        TrainerUpdateRequestDto requestDto = new TrainerUpdateRequestDto();
        //when
        requestDto.setUsername(username);
        requestDto.setFirstName(firstName);
        requestDto.setLastName(lastName);
        requestDto.setSpecializationId(specializationId);
        requestDto.setIsActive(isActive);
        //then
        assertThat(requestDto.getUsername()).isEqualTo(username);
        assertThat(requestDto.getFirstName()).isEqualTo(firstName);
        assertThat(requestDto.getLastName()).isEqualTo(lastName);
        assertThat(requestDto.getSpecializationId()).isEqualTo(specializationId);
        assertThat(requestDto.getIsActive()).isEqualTo(isActive);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        Boolean isActive = false;
        //when
        TrainerUpdateRequestDto requestDto = new TrainerUpdateRequestDto(
            username,
            firstName,
            lastName,
            specializationId,
            isActive);
        //then
        assertThat(requestDto.getUsername()).isEqualTo(username);
        assertThat(requestDto.getFirstName()).isEqualTo(firstName);
        assertThat(requestDto.getLastName()).isEqualTo(lastName);
        assertThat(requestDto.getSpecializationId()).isEqualTo(specializationId);
        assertThat(requestDto.getIsActive()).isEqualTo(isActive);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TrainerUpdateRequestDto requestDto = new TrainerUpdateRequestDto();
        //then
        assertThat(requestDto.getUsername()).isNull();
        assertThat(requestDto.getFirstName()).isNull();
        assertThat(requestDto.getLastName()).isNull();
        assertThat(requestDto.getSpecializationId()).isNull();
        assertThat(requestDto.getIsActive()).isNull();
    }
}
