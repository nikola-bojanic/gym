package com.nikolabojanic.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.nikolabojanic.dto.TrainingTypeRequestDto;
import com.nikolabojanic.dto.TrainingTypeResponseDto;
import com.nikolabojanic.entity.TrainingTypeEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainingTypeConverterTest {
    @InjectMocks
    private TrainingTypeConverter trainingTypeConverter;

    @Test
    void convertIdToModelTest() {
        //given
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(5));
        //when
        TrainingTypeEntity type = trainingTypeConverter.convertIdToModel(id);
        //then
        assertThat(type.getId()).isEqualTo(id);
    }

    @Test
    void convertModelToResponseTest() {
        //given
        TrainingTypeEntity type = new TrainingTypeEntity();
        type.setId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        type.setName(RandomStringUtils.randomAlphabetic(10));
        //when
        TrainingTypeResponseDto responseDto = trainingTypeConverter.convertModelToResponse(type);
        //then
        assertThat(responseDto.getId()).isEqualTo(type.getId());
        assertThat(responseDto.getName()).isEqualTo(type.getName());
    }

    @Test
    void convertDtoToModelTest() {
        //assert
        TrainingTypeRequestDto requestDto = new TrainingTypeRequestDto();
        requestDto.setName(RandomStringUtils.randomAlphabetic(5));
        //when
        TrainingTypeEntity type = trainingTypeConverter.convertDtoToModel(requestDto);
        //then
        assertThat(type.getName()).isEqualTo(requestDto.getName());
    }
}