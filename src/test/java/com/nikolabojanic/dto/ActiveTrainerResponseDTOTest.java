package com.nikolabojanic.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class ActiveTrainerResponseDTOTest {
    @Test
    void getterAndSetterTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        ActiveTrainerResponseDTO responseDTO = new ActiveTrainerResponseDTO();
        //when
        responseDTO.setUsername(username);
        responseDTO.setFirstName(firstName);
        responseDTO.setLastName(lastName);
        responseDTO.setSpecializationId(specializationId);
        //then
        assertThat(responseDTO.getUsername()).isEqualTo(username);
        assertThat(responseDTO.getFirstName()).isEqualTo(firstName);
        assertThat(responseDTO.getLastName()).isEqualTo(lastName);
        assertThat(responseDTO.getSpecializationId()).isEqualTo(specializationId);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        //when
        ActiveTrainerResponseDTO responseDTO = new ActiveTrainerResponseDTO(
                username,
                firstName,
                lastName,
                specializationId);
        //then
        assertThat(responseDTO.getUsername()).isEqualTo(username);
        assertThat(responseDTO.getFirstName()).isEqualTo(firstName);
        assertThat(responseDTO.getLastName()).isEqualTo(lastName);
        assertThat(responseDTO.getSpecializationId()).isEqualTo(specializationId);

    }

    @Test
    void noArgsConstructorTest() {
        //when
        ActiveTrainerResponseDTO responseDTO = new ActiveTrainerResponseDTO();
        //then
        assertThat(responseDTO.getSpecializationId()).isNull();
        assertThat(responseDTO.getUsername()).isNull();
        assertThat(responseDTO.getFirstName()).isNull();
        assertThat(responseDTO.getLastName()).isNull();
    }
}