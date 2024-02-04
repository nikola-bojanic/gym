package com.nikolabojanic.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.nikolabojanic.converter.TrainingTypeConverter;
import com.nikolabojanic.dto.TrainingTypeResponseDto;
import com.nikolabojanic.entity.TrainingTypeEntity;
import com.nikolabojanic.service.TrainingTypeService;
import io.micrometer.core.instrument.Counter;
import java.util.List;
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
}