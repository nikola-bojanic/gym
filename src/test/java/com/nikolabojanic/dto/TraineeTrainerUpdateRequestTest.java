package com.nikolabojanic.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class TraineeTrainerUpdateRequestTest {
    @Test
    void getterAndSetterTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        TraineeTrainerUpdateRequestDTO requestDTO = new TraineeTrainerUpdateRequestDTO();
        //when
        requestDTO.setUsername(username);
        //then
        assertThat(requestDTO.getUsername()).isEqualTo(username);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        //when
        TraineeTrainerUpdateRequestDTO requestDTO = new TraineeTrainerUpdateRequestDTO(username);
        //then
        assertThat(requestDTO.getUsername()).isEqualTo(username);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TraineeTrainerUpdateRequestDTO requestDTO = new TraineeTrainerUpdateRequestDTO();
        //then
        assertThat(requestDTO.getUsername()).isNull();
    }
}