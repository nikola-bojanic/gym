package com.nikolabojanic.controller;

import com.nikolabojanic.converter.TrainerConverter;
import com.nikolabojanic.dto.*;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.UserEntity;
import com.nikolabojanic.service.TrainerService;
import com.nikolabojanic.service.UserService;
import com.nikolabojanic.validation.TrainerValidation;
import io.micrometer.core.instrument.Counter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerControllerTest {
    @Mock
    private TrainerService trainerService;
    @Mock
    private UserService userService;
    @Mock
    private TrainerConverter trainerConverter;
    @Mock
    private TrainerValidation trainerValidation;
    @Mock
    private Counter trainerEndpointsHitCounter;
    @InjectMocks
    private TrainerController trainerController;

    @Test
    void getTrainerTest() {
        //given
        doNothing().when(userService).authentication(any(AuthDTO.class));
        doNothing().when(trainerValidation).validateUsernameNotNull(any(String.class));
        when(trainerService.findByUsername(any(String.class))).thenReturn(new TrainerEntity());
        when(trainerConverter.convertModelToResponseDTO(any(TrainerEntity.class))).thenReturn(new TrainerResponseDTO());
        //when
        ResponseEntity<TrainerResponseDTO> response = trainerController.getTrainer(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10));
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void updateTrainerTest() {
        //given
        doNothing().when(userService).authentication(any(AuthDTO.class));
        doNothing().when(trainerValidation).validateUsernameNotNull(any(String.class));
        doNothing().when(trainerValidation).validateUpdateTrainerRequest(any(TrainerUpdateRequestDTO.class));
        when(trainerConverter.convertUpdateRequestToModel(any(TrainerUpdateRequestDTO.class)))
                .thenReturn(new TrainerEntity());
        when(trainerService.updateTrainerProfile(any(String.class), any(TrainerEntity.class)))
                .thenReturn(new TrainerEntity());
        when(trainerConverter.convertModelToUpdateResponse(any(TrainerEntity.class)))
                .thenReturn(new TrainerUpdateResponseDTO());
        //when
        ResponseEntity<TrainerUpdateResponseDTO> response = trainerController.updateTrainer(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                new TrainerUpdateRequestDTO());
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void registerTrainerTest() {
        //given
        UserEntity user = new UserEntity();
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        TrainerEntity registered = new TrainerEntity();
        registered.setUser(user);
        doNothing().when(trainerValidation).validateRegisterRequest(any(TrainerRegistrationRequestDTO.class));
        when(trainerConverter.convertRegistrationRequestToModel(any(TrainerRegistrationRequestDTO.class)))
                .thenReturn(new TrainerEntity());
        when(trainerService.createTrainerProfile(any(TrainerEntity.class))).thenReturn(registered);
        when(trainerConverter.convertModelToRegistrationResponse(any(TrainerEntity.class)))
                .thenReturn(new RegistrationResponseDTO());
        //when
        ResponseEntity<RegistrationResponseDTO> response = trainerController
                .registerTrainer(new TrainerRegistrationRequestDTO());
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void getActiveTrainersTest() {
        //given
        TrainerEntity trainer = new TrainerEntity();
        List<TrainerEntity> trainers = List.of(trainer);
        doNothing().when(userService).authentication(any(AuthDTO.class));
        doNothing().when(trainerValidation).validateUsernameNotNull(any(String.class));
        when(trainerService.findActiveForOtherTrainees(any(String.class)))
                .thenReturn(trainers);
        when(trainerConverter.convertModelToActiveTrainerResponse(any(TrainerEntity.class)))
                .thenReturn(new ActiveTrainerResponseDTO());
        //when
        ResponseEntity<List<ActiveTrainerResponseDTO>> response = trainerController.getActiveTrainers(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10)
        );
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void changeActiveStatusTest() {
        //given
        doNothing().when(userService).authentication(any(AuthDTO.class));
        doNothing().when(trainerValidation).validateActiveStatusRequest(any(String.class), any(Boolean.class));
        doNothing().when(trainerService).changeActiveStatus(any(String.class), any(Boolean.class));
        //when
        ResponseEntity<Void> response = trainerController.changeActiveStatus(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                true
        );
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
