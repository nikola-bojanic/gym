package com.nikolabojanic.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class TrainerTraineeResponseDTOTest {
    @Test
    void getterAndSetterTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        TrainerTraineeResponseDTO responseDTO = new TrainerTraineeResponseDTO();
        //when
        responseDTO.setUsername(username);
        responseDTO.setFirstName(firstName);
        responseDTO.setLastName(lastName);
        //then
        assertThat(responseDTO.getUsername()).isEqualTo(username);
        assertThat(responseDTO.getFirstName()).isEqualTo(firstName);
        assertThat(responseDTO.getLastName()).isEqualTo(lastName);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        //when
        TrainerTraineeResponseDTO responseDTO = new TrainerTraineeResponseDTO(username, firstName, lastName);
        //then
        assertThat(responseDTO.getUsername()).isEqualTo(username);
        assertThat(responseDTO.getFirstName()).isEqualTo(firstName);
        assertThat(responseDTO.getLastName()).isEqualTo(lastName);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TrainerTraineeResponseDTO responseDTO = new TrainerTraineeResponseDTO();
        //then
        assertThat(responseDTO.getUsername()).isNull();
        assertThat(responseDTO.getFirstName()).isNull();
        assertThat(responseDTO.getLastName()).isNull();
    }
}
