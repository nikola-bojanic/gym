package com.nikolabojanic.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.nikolabojanic.converter.TraineeConverter;
import com.nikolabojanic.converter.TrainerConverter;
import com.nikolabojanic.domain.TraineeDomain;
import com.nikolabojanic.dto.RegistrationResponseDto;
import com.nikolabojanic.dto.TraineeRegistrationRequestDto;
import com.nikolabojanic.dto.TraineeResponseDto;
import com.nikolabojanic.dto.TraineeTrainerResponseDto;
import com.nikolabojanic.dto.TraineeUpdateRequestDto;
import com.nikolabojanic.dto.TraineeUpdateResponseDto;
import com.nikolabojanic.entity.TraineeEntity;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.service.TraineeService;
import com.nikolabojanic.validation.TraineeValidation;
import io.micrometer.core.instrument.Counter;
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
class TraineeControllerTest {
    @Mock
    private TraineeService traineeService;
    @Mock
    private TraineeConverter traineeConverter;
    @Mock
    private TrainerConverter trainerConverter;
    @Mock
    private TraineeValidation traineeValidation;
    @Mock
    private Counter traineeEndpointsHitCounter;
    @InjectMocks
    private TraineeController traineeController;

    @Test
    void getTraineeTest() {
        //given
        doNothing().when(traineeValidation).validateUsername(any(String.class));
        when(traineeService.findByUsername(any(String.class))).thenReturn(new TraineeEntity());
        when(traineeConverter.convertModelToResponse(any(TraineeEntity.class))).thenReturn(new TraineeResponseDto());
        //when
        ResponseEntity<TraineeResponseDto> response = traineeController.getTrainee(
            RandomStringUtils.randomAlphabetic(10));
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void updateTraineeTest() {
        //given
        doNothing().when(traineeValidation).validateUsername(any(String.class));
        doNothing().when(traineeValidation).validateUpdateTraineeRequest(any(TraineeUpdateRequestDto.class));
        when(traineeConverter.convertUpdateRequestToModel(any(TraineeUpdateRequestDto.class)))
            .thenReturn(new TraineeEntity());
        when(traineeService.updateTraineeProfile(any(String.class),
            any(TraineeEntity.class))).thenReturn(new TraineeEntity());
        when(traineeConverter.convertModelToUpdateResponse(any(TraineeEntity.class)))
            .thenReturn(new TraineeUpdateResponseDto());
        //when
        ResponseEntity<TraineeUpdateResponseDto> response = traineeController.updateTrainee(
            RandomStringUtils.randomAlphabetic(10),
            new TraineeUpdateRequestDto());
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void registerTraineeTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(10);
        TraineeDomain registered = new TraineeDomain();
        registered.setUsername(username);
        doNothing().when(traineeValidation).validateRegisterRequest(any(TraineeRegistrationRequestDto.class));
        when(traineeConverter.convertRegistrationRequestToModel(any(TraineeRegistrationRequestDto.class)))
            .thenReturn(new TraineeEntity());
        when(traineeService.createTraineeProfile(any(TraineeEntity.class))).thenReturn(registered);
        when(traineeConverter.convertModelToRegistrationResponse(any(TraineeDomain.class)))
            .thenReturn(new RegistrationResponseDto());
        //when
        ResponseEntity<RegistrationResponseDto> response = traineeController
            .registerTrainee(new TraineeRegistrationRequestDto());
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void deleteTraineeTest() {
        //given
        doNothing().when(traineeValidation).validateUsername(any(String.class));
        when(traineeService.deleteByUsername(any(String.class))).thenReturn(new TraineeEntity());
        //when
        ResponseEntity<Void> response = traineeController.deleteTrainee(
            RandomStringUtils.randomAlphabetic(10));
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void updateTraineesTrainersTest() {
        //given
        TrainerEntity trainer = new TrainerEntity();
        List<TrainerEntity> trainers = List.of(trainer);
        TraineeEntity trainee = new TraineeEntity();
        trainee.setTrainers(trainers);
        doNothing().when(traineeValidation).validateUsername(any(String.class));
        doNothing().when(traineeValidation).validateUpdateTrainersRequest(any(List.class));
        when(traineeService.updateTraineeTrainers(any(String.class), any(List.class)))
            .thenReturn(trainee);
        when(trainerConverter.convertModelToTraineeTrainer(trainer)).thenReturn(new TraineeTrainerResponseDto());
        //when
        ResponseEntity<List<TraineeTrainerResponseDto>> response = traineeController.updateTraineesTrainers(
            RandomStringUtils.randomAlphabetic(10),
            new ArrayList<>()
        );
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void changeActiveStatusTest() {
        //given
        doNothing().when(traineeValidation).validateUsername(any(String.class));
        doNothing().when(traineeService).changeActiveStatus(any(String.class), any(Boolean.class));
        //when
        ResponseEntity<Void> response = traineeController.changeActiveStatus(
            RandomStringUtils.randomAlphabetic(10),
            true
        );
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}