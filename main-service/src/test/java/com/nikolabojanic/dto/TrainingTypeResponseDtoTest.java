package com.nikolabojanic.dto;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainingTypeResponseDtoTest {
    @Test
    void getterAndSetterTest() {
        //given
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(5));
        String name = RandomStringUtils.randomAlphabetic(5);
        TrainingTypeResponseDto responseDto = new TrainingTypeResponseDto();
        //when
        responseDto.setId(id);
        responseDto.setName(name);
        //then
        assertThat(responseDto.getId()).isEqualTo(id);
        assertThat(responseDto.getName()).isEqualTo(name);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(5));
        String name = RandomStringUtils.randomAlphabetic(5);
        //when
        TrainingTypeResponseDto responseDto = new TrainingTypeResponseDto(id, name);
        //then
        assertThat(responseDto.getId()).isEqualTo(id);
        assertThat(responseDto.getName()).isEqualTo(name);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TrainingTypeResponseDto responseDto = new TrainingTypeResponseDto();
        //then
        assertThat(responseDto.getId()).isNull();
        assertThat(responseDto.getName()).isNull();
    }
}
