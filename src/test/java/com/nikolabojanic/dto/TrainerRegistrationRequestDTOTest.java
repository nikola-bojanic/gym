package com.nikolabojanic.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class TrainerRegistrationRequestDTOTest {
    @Test
    void getterAndSetterTest() {
        //given
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        TrainerRegistrationRequestDTO requestDTO = new TrainerRegistrationRequestDTO();
        //when
        requestDTO.setFirstName(firstName);
        requestDTO.setLastName(lastName);
        requestDTO.setSpecializationId(specializationId);
        //then
        assertThat(requestDTO.getFirstName()).isEqualTo(firstName);
        assertThat(requestDTO.getLastName()).isEqualTo(lastName);
        assertThat(requestDTO.getSpecializationId()).isEqualTo(specializationId);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        //when
        TrainerRegistrationRequestDTO requestDTO = new TrainerRegistrationRequestDTO(
                firstName,
                lastName,
                specializationId);
        //then
        assertThat(requestDTO.getFirstName()).isEqualTo(firstName);
        assertThat(requestDTO.getLastName()).isEqualTo(lastName);
        assertThat(requestDTO.getSpecializationId()).isEqualTo(specializationId);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TrainerRegistrationRequestDTO requestDTO = new TrainerRegistrationRequestDTO();
        //then
        assertThat(requestDTO.getFirstName()).isNull();
        assertThat(requestDTO.getLastName()).isNull();
        assertThat(requestDTO.getSpecializationId()).isNull();
    }
}
