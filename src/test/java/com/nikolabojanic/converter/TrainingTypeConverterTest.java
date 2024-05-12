package com.nikolabojanic.converter;

import com.nikolabojanic.dto.TrainingTypeResponseDTO;
import com.nikolabojanic.model.TrainingTypeEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

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
        TrainingTypeResponseDTO responseDTO = trainingTypeConverter.convertModelToResponse(type);
        //then
        assertThat(responseDTO.getId()).isEqualTo(type.getId());
        assertThat(responseDTO.getName()).isEqualTo(type.getName());
    }
}