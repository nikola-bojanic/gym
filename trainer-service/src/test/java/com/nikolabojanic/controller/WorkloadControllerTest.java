package com.nikolabojanic.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.nikolabojanic.dto.TrainerWorkloadRequestDto;
import com.nikolabojanic.dto.TrainerWorkloadResponseDto;
import com.nikolabojanic.entity.TrainingEntity;
import com.nikolabojanic.enumeration.Action;
import com.nikolabojanic.service.TrainerService;
import com.nikolabojanic.service.TrainingService;
import com.nikolabojanic.validation.WorkloadValidation;
import java.time.LocalDate;
import java.util.ArrayList;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class WorkloadControllerTest {
    @Mock
    private TrainingService trainingService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private WorkloadValidation workloadValidation;
    @InjectMocks
    private WorkloadController workloadController;

    @Test
    void updateAddWorkloadTest() {
        //arrange
        TrainerWorkloadRequestDto requestDto = new TrainerWorkloadRequestDto();
        requestDto.setAction(Action.ADD);
        when(trainingService.addTraining(any(TrainingEntity.class))).thenReturn(new TrainingEntity());
        //act
        ResponseEntity<Void> response = workloadController.updateWorkload(requestDto);
        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void updateDeleteWorkloadTest() {
        //arrange
        TrainerWorkloadRequestDto requestDto = new TrainerWorkloadRequestDto();
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(5));
        requestDto.setDate(LocalDate.now());
        requestDto.setAction(Action.DELETE);
        when(trainingService.deleteTrainings(anyString(), any(LocalDate.class))).thenReturn(new ArrayList<>());
        //act
        ResponseEntity<Void> response = workloadController.updateWorkload(requestDto);
        //assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void getTrainerWorkloadTest() {
        //arrange
        when(trainerService.getWorkload(anyString())).thenReturn(new TrainerWorkloadResponseDto());
        doNothing().when(workloadValidation).validateUsernameNotNull(anyString());
        //act
        ResponseEntity<TrainerWorkloadResponseDto> response = workloadController
            .getTrainerWorkload(RandomStringUtils.randomAlphabetic(5));
        //assert
        assertThat(response).isNotNull();

    }
}