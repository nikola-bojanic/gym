package com.nikolabojanic.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.nikolabojanic.dto.TrainerWorkloadResponseDto;
import com.nikolabojanic.service.TrainerService;
import com.nikolabojanic.validation.WorkloadValidation;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class WorkloadControllerTest {
    @Mock
    private TrainerService trainerService;
    @Mock
    private WorkloadValidation workloadValidation;
    @InjectMocks
    private WorkloadController workloadController;

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