package com.nikolabojanic.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.nikolabojanic.converter.TrainingTypeConverter;
import com.nikolabojanic.dto.TrainingTypeRequestDto;
import com.nikolabojanic.dto.TrainingTypeResponseDto;
import com.nikolabojanic.entity.TrainingTypeEntity;
import com.nikolabojanic.service.TrainingTypeService;
import com.nikolabojanic.validation.TrainingTypeValidation;
import io.micrometer.core.instrument.Counter;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class TrainingTypeControllerTest {
    @Mock
    private TrainingTypeService trainingTypeService;
    @Mock
    private TrainingTypeConverter trainingTypeConverter;
    @Mock
    private TrainingTypeValidation trainingTypeValidation;
    @Mock
    private Counter trainingTypeEndpointsHitCounter;
    @InjectMocks
    private TrainingTypeController trainingTypeController;

    @Test
    void getAllTest() {
        //given
        when(trainingTypeService.getAll()).thenReturn(List.of(new TrainingTypeEntity()));
        when(trainingTypeConverter.convertModelToResponse(any(TrainingTypeEntity.class)))
            .thenReturn(new TrainingTypeResponseDto());
        //when
        ResponseEntity<List<TrainingTypeResponseDto>> response = trainingTypeController.getAll();
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void createTypeTest() {
        //arrange
        TrainingTypeRequestDto requestDto = new TrainingTypeRequestDto();
        requestDto.setName(RandomStringUtils.randomAlphabetic(5));
        doNothing().when(trainingTypeValidation).validateUsername(anyString());
        when(trainingTypeConverter.convertDtoToModel(any(TrainingTypeRequestDto.class)))
            .thenReturn(new TrainingTypeEntity());
        when(trainingTypeService.create(any(TrainingTypeEntity.class))).thenReturn(new TrainingTypeEntity());
        when(trainingTypeConverter.convertModelToResponse(any(TrainingTypeEntity.class)))
            .thenReturn(new TrainingTypeResponseDto());
        //act
        ResponseEntity<TrainingTypeResponseDto> response = trainingTypeController
            .createType(requestDto);
        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}