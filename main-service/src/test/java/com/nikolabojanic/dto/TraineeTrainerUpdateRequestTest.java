package com.nikolabojanic.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraineeTrainerUpdateRequestTest {
    @Test
    void getterAndSetterTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        TraineeTrainerUpdateRequestDto requestDto = new TraineeTrainerUpdateRequestDto();
        //when
        requestDto.setUsername(username);
        //then
        assertThat(requestDto.getUsername()).isEqualTo(username);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        //when
        TraineeTrainerUpdateRequestDto requestDto = new TraineeTrainerUpdateRequestDto(username);
        //then
        assertThat(requestDto.getUsername()).isEqualTo(username);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TraineeTrainerUpdateRequestDto requestDto = new TraineeTrainerUpdateRequestDto();
        //then
        assertThat(requestDto.getUsername()).isNull();
    }
}