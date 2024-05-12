package com.nikolabojanic.controller;

import com.nikolabojanic.converter.TrainingConverter;
import com.nikolabojanic.dto.TraineeTrainingResponseDTO;
import com.nikolabojanic.dto.TrainerTrainingResponseDTO;
import com.nikolabojanic.dto.TrainingRequestDTO;
import com.nikolabojanic.dto.TrainingResponseDTO;
import com.nikolabojanic.entity.TrainingEntity;
import com.nikolabojanic.service.TrainingService;
import com.nikolabojanic.validation.TrainingValidation;
import io.micrometer.core.instrument.Counter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

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
        doNothing().when(trainingValidation).validateCreateTrainingRequest(any(TrainingRequestDTO.class));
        when(trainingConverter.convertToEntity(any(TrainingRequestDTO.class)))
                .thenReturn(new TrainingEntity());
        when(trainingService.create(any(TrainingEntity.class)))
                .thenReturn(new TrainingEntity());
        when(trainingConverter.convertToDto(any(TrainingEntity.class)))
                .thenReturn(new TrainingResponseDTO());
        //when
        ResponseEntity<TrainingResponseDTO> response = trainingController.createTraining(
                new TrainingRequestDTO());
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
        ResponseEntity<List<TrainerTrainingResponseDTO>> response = trainingController.getTrainingsByTrainerAndFilter(
                RandomStringUtils.randomAlphabetic(5),
                LocalDate.now(),
                LocalDate.now(),
                RandomStringUtils.randomAlphabetic(5));
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
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
        ResponseEntity<List<TraineeTrainingResponseDTO>> response = trainingController.getTrainingsByTraineeAndFilter(
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
