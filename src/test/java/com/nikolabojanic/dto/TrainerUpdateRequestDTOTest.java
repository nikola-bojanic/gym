package com.nikolabojanic.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class TrainerUpdateRequestDTOTest {
    @Test
    void getterAndSetterTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        Long specializationId = Long.parseLong(RandomStringUtils.randomNumeric(5));
        Boolean isActive = false;
        TrainerUpdateRequestDTO requestDTO = new TrainerUpdateRequestDTO();
        //when
        requestDTO.setUsername(username);
        requestDTO.setFirstName(firstName);
        requestDTO.setLastName(lastName);
        requestDTO.setSpecializationId(specializationId);
        requestDTO.setIsActive(isActive);
        //then
        assertThat(requestDTO.getUsername()).isEqualTo(username);
        assertThat(requestDTO.getFirstName()).isEqualTo(firstName);
        assertThat(requestDTO.getLastName()).isEqualTo(lastName);
        assertThat(requestDTO.getSpecializationId()).isEqualTo(specializationId);
        assertThat(requestDTO.getIsActive()).isEqualTo(isActive);
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
        TrainerUpdateRequestDTO requestDTO = new TrainerUpdateRequestDTO(
                username,
                firstName,
                lastName,
                specializationId,
                isActive);
        //then
        assertThat(requestDTO.getUsername()).isEqualTo(username);
        assertThat(requestDTO.getFirstName()).isEqualTo(firstName);
        assertThat(requestDTO.getLastName()).isEqualTo(lastName);
        assertThat(requestDTO.getSpecializationId()).isEqualTo(specializationId);
        assertThat(requestDTO.getIsActive()).isEqualTo(isActive);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TrainerUpdateRequestDTO requestDTO = new TrainerUpdateRequestDTO();
        //then
        assertThat(requestDTO.getUsername()).isNull();
        assertThat(requestDTO.getFirstName()).isNull();
        assertThat(requestDTO.getLastName()).isNull();
        assertThat(requestDTO.getSpecializationId()).isNull();
        assertThat(requestDTO.getIsActive()).isNull();
    }
}
