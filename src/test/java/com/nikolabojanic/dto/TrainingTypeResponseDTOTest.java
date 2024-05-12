package com.nikolabojanic.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class TrainingTypeResponseDTOTest {
    @Test
    void getterAndSetterTest() {
        //given
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(5));
        String name = RandomStringUtils.randomAlphabetic(5);
        TrainingTypeResponseDTO responseDTO = new TrainingTypeResponseDTO();
        //when
        responseDTO.setId(id);
        responseDTO.setName(name);
        //then
        assertThat(responseDTO.getId()).isEqualTo(id);
        assertThat(responseDTO.getName()).isEqualTo(name);
    }

    @Test
    void allArgsConstructorTest() {
        //given
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(5));
        String name = RandomStringUtils.randomAlphabetic(5);
        //when
        TrainingTypeResponseDTO responseDTO = new TrainingTypeResponseDTO(id, name);
        //then
        assertThat(responseDTO.getId()).isEqualTo(id);
        assertThat(responseDTO.getName()).isEqualTo(name);
    }

    @Test
    void noArgsConstructorTest() {
        //when
        TrainingTypeResponseDTO responseDTO = new TrainingTypeResponseDTO();
        //then
        assertThat(responseDTO.getId()).isNull();
        assertThat(responseDTO.getName()).isNull();
    }
}
