package com.nikolabojanic.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.nikolabojanic.converter.TrainerConverter;
import com.nikolabojanic.dto.TrainerWorkloadResponseDto;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.service.TrainerService;
import com.nikolabojanic.validation.TrainerValidation;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {
    @Mock
    private TrainerService trainerService;
    @Mock
    private TrainerConverter trainerConverter;
    @Mock
    private TrainerValidation trainerValidation;
    @InjectMocks
    private TrainerController trainerController;

    @Test
    void getTrainerWorkloadTest() {
        //arrange
        when(trainerService.findByUsername(anyString())).thenReturn(new TrainerEntity());
        when(trainerConverter.convertToResponseDto(any(TrainerEntity.class)))
            .thenReturn(new TrainerWorkloadResponseDto());
        doNothing().when(trainerValidation).validateUsernameNotNull(anyString());
        //act
        ResponseEntity<TrainerWorkloadResponseDto> response = trainerController
            .getTrainingSummary(RandomStringUtils.randomAlphabetic(5));
        //assert
        assertThat(response).isNotNull();
    }

    @Test
    void findTrainersByNameTest() {
        //arrange
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        doNothing().when(trainerValidation).validateFirstAndLastName(firstName, lastName);
        when(trainerService.findByFirstAndLastName(firstName, lastName)).thenReturn(List.of(new TrainerEntity()));
        when(trainerConverter.convertToResponseDto(any(TrainerEntity.class))).thenReturn(
            new TrainerWorkloadResponseDto());
        //act
        ResponseEntity<List<TrainerWorkloadResponseDto>> response =
            trainerController.findTrainersByName(firstName, lastName);
        //assert
        assertThat(response.getBody()).isNotNull();
    }
}