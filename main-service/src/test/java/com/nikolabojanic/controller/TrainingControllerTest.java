package com.nikolabojanic.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nikolabojanic.converter.TrainingConverter;
import com.nikolabojanic.dto.TraineeTrainingResponseDto;
import com.nikolabojanic.dto.TrainerTrainingResponseDto;
import com.nikolabojanic.dto.TrainingRequestDto;
import com.nikolabojanic.dto.TrainingResponseDto;
import com.nikolabojanic.entity.TrainingEntity;
import com.nikolabojanic.service.TrainingService;
import com.nikolabojanic.validation.TrainingValidation;
import io.micrometer.core.instrument.Counter;
import java.time.LocalDate;
import java.util.ArrayList;
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
class TrainingControllerTest {
    @Mock
    private TrainingService trainingService;
    @Mock
    private TrainingConverter trainingConverter;
    @Mock
    private TrainingValidation trainingValidation;
    @Mock
    private Counter trainingEndpointsHitCounter;
    @InjectMocks
    private TrainingController trainingController;

    @Test
    void createTrainingTest() {
        //given
        doNothing().when(trainingValidation).validateCreateTrainingRequest(any(TrainingRequestDto.class));
        when(trainingConverter.convertToEntity(any(TrainingRequestDto.class)))
            .thenReturn(new TrainingEntity());
        when(trainingService.create(any(TrainingEntity.class), anyString()))
            .thenReturn(new TrainingEntity());
        when(trainingConverter.convertToDto(any(TrainingEntity.class)))
            .thenReturn(new TrainingResponseDto());
        //when
        ResponseEntity<TrainingResponseDto> response = trainingController.createTraining(
            new TrainingRequestDto(), RandomStringUtils.randomAlphabetic(10));
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void getTrainingsByTrainerAndFilterTest() {
        //given
        doNothing().when(trainingValidation).validateUsernameNotNull(any(String.class));
        when(trainingService.findByTrainerAndFilter(
            any(String.class),
            any(LocalDate.class),
            any(LocalDate.class),
            any(String.class)))
            .thenReturn(new ArrayList<>());
        //when
        ResponseEntity<List<TrainerTrainingResponseDto>> response = trainingController.getTrainingsByTrainerAndFilter(
            RandomStringUtils.randomAlphabetic(5),
            LocalDate.now(),
            LocalDate.now(),
            RandomStringUtils.randomAlphabetic(5));
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void deleteTrainingTest() {
        //arrange
        doNothing().when(trainingEndpointsHitCounter).increment();
        doNothing().when(trainingValidation).validateIdNotNull(anyLong());
        when(trainingService.deleteTraining(anyLong(), anyString())).thenReturn(new TrainingEntity());
        //act
        trainingController.deleteTraining(
            Long.parseLong(RandomStringUtils.randomNumeric(5)),
            RandomStringUtils.randomAlphabetic(5));
        //assert
        verify(trainingService, times(1)).deleteTraining(anyLong(), anyString());
    }

    @Test
    void getTrainingsByTraineeAndFilterTest() {
        //given
        doNothing().when(trainingValidation).validateUsernameNotNull(any(String.class));
        when(trainingService.findByTraineeAndFilter(
            any(String.class),
            any(LocalDate.class),
            any(LocalDate.class),
            any(String.class),
            any(Long.class)))
            .thenReturn(new ArrayList<>());
        //when
        ResponseEntity<List<TraineeTrainingResponseDto>> response = trainingController.getTrainingsByTraineeAndFilter(
            RandomStringUtils.randomAlphabetic(5),
            LocalDate.now(),
            LocalDate.now(),
            RandomStringUtils.randomAlphabetic(5),
            Long.parseLong(RandomStringUtils.randomNumeric(5)));
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }
}
