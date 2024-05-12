package com.nikolabojanic.controller;

import com.nikolabojanic.converter.TraineeConverter;
import com.nikolabojanic.converter.TrainerConverter;
import com.nikolabojanic.dto.*;
import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.UserEntity;
import com.nikolabojanic.service.TraineeService;
import com.nikolabojanic.validation.TraineeValidation;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeControllerTest {
    @Mock
    private TraineeValidation traineeValidation;
    @Mock
    private TraineeConverter traineeConverter;
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerConverter trainerConverter;
    @InjectMocks
    private TraineeController traineeController;

    @Test
    void getTraineeTest() {
        //given
        doNothing().when(traineeValidation).validateUsernameNotNull(any(String.class));
        when(traineeService.findByUsername(any(AuthDTO.class), any(String.class))).thenReturn(new TraineeEntity());
        when(traineeConverter.convertModelToResponse(any(TraineeEntity.class))).thenReturn(new TraineeResponseDTO());
        //when
        ResponseEntity<TraineeResponseDTO> response = traineeController.getTrainee(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10));
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void updateTraineeTest() {
        //given
        doNothing().when(traineeValidation).validateUsernameNotNull(any(String.class));
        doNothing().when(traineeValidation).validateUpdateTraineeRequest(any(TraineeUpdateRequestDTO.class));
        when(traineeConverter.convertUpdateRequestToModel(any(TraineeUpdateRequestDTO.class)))
                .thenReturn(new TraineeEntity());
        when(traineeService.updateTraineeProfile(any(AuthDTO.class), any(String.class),
                any(TraineeEntity.class))).thenReturn(new TraineeEntity());
        when(traineeConverter.convertModelToUpdateResponse(any(TraineeEntity.class)))
                .thenReturn(new TraineeUpdateResponseDTO());
        //when
        ResponseEntity<TraineeUpdateResponseDTO> response = traineeController.updateTrainee(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                new TraineeUpdateRequestDTO());
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void registerTraineeTest() {
        //given
        UserEntity user = new UserEntity();
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        TraineeEntity registered = new TraineeEntity();
        registered.setUser(user);
        doNothing().when(traineeValidation).validateRegisterRequest(any(TraineeRegistrationRequestDTO.class));
        when(traineeConverter.convertRegistrationRequestToModel(any(TraineeRegistrationRequestDTO.class)))
                .thenReturn(new TraineeEntity());
        when(traineeService.createTraineeProfile(any(TraineeEntity.class))).thenReturn(registered);
        when(traineeConverter.convertModelToRegistrationResponse(any(TraineeEntity.class)))
                .thenReturn(new RegistrationResponseDTO());
        //when
        ResponseEntity<RegistrationResponseDTO> response = traineeController
                .registerTrainee(new TraineeRegistrationRequestDTO());
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void deleteTraineeTest() {
        //given
        doNothing().when(traineeValidation).validateUsernameNotNull(any(String.class));
        when(traineeService.deleteByUsername(any(AuthDTO.class), any(String.class))).thenReturn(new TraineeEntity());
        //when
        ResponseEntity<Void> response = traineeController.deleteTrainee(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10));
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void updateTraineesTrainersTest() {
        //given
        TrainerEntity trainer = new TrainerEntity();
        List<TrainerEntity> trainers = List.of(trainer);
        doNothing().when(traineeValidation).validateUsernameNotNull(any(String.class));
        doNothing().when(traineeValidation).validateUpdateTrainersRequest(any(List.class));
        when(traineeService.updateTraineeTrainers(any(AuthDTO.class), any(String.class), any(List.class)))
                .thenReturn(trainers);
        when(trainerConverter.convertModelToTraineeTrainer(trainer)).thenReturn(new TraineeTrainerResponseDTO());
        //when
        ResponseEntity<List<TraineeTrainerResponseDTO>> response = traineeController.updateTraineesTrainers(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
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
        doNothing().when(traineeValidation).validateActiveStatusRequest(any(String.class), any(Boolean.class));
        doNothing().when(traineeService).changeActiveStatus(any(AuthDTO.class), any(String.class), any(Boolean.class));
        //when
        ResponseEntity<Void> response = traineeController.changeActiveStatus(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                true
        );
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}