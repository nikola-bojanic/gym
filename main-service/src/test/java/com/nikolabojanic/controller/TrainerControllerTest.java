package com.nikolabojanic.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.nikolabojanic.converter.TrainerConverter;
import com.nikolabojanic.domain.TrainerDomain;
import com.nikolabojanic.dto.ActiveTrainerResponseDto;
import com.nikolabojanic.dto.RegistrationResponseDto;
import com.nikolabojanic.dto.TrainerRegistrationRequestDto;
import com.nikolabojanic.dto.TrainerResponseDto;
import com.nikolabojanic.dto.TrainerUpdateRequestDto;
import com.nikolabojanic.dto.TrainerUpdateResponseDto;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.service.TrainerService;
import com.nikolabojanic.validation.TrainerValidation;
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
class TrainerControllerTest {
    @Mock
    private TrainerService trainerService;
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
        doNothing().when(trainerValidation).validateUsername(any(String.class));
        when(trainerService.findByUsername(any(String.class))).thenReturn(new TrainerEntity());
        when(trainerConverter.convertModelToResponseDto(any(TrainerEntity.class))).thenReturn(new TrainerResponseDto());
        //when
        ResponseEntity<TrainerResponseDto> response = trainerController.getTrainer(
            RandomStringUtils.randomAlphabetic(10));
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void updateTrainerTest() {
        //given
        doNothing().when(trainerValidation).validateUsername(any(String.class));
        doNothing().when(trainerValidation).validateUpdateTrainerRequest(any(TrainerUpdateRequestDto.class));
        when(trainerConverter.convertUpdateRequestToModel(any(TrainerUpdateRequestDto.class)))
            .thenReturn(new TrainerEntity());
        when(trainerService.updateTrainerProfile(any(String.class), any(TrainerEntity.class)))
            .thenReturn(new TrainerEntity());
        when(trainerConverter.convertModelToUpdateResponse(any(TrainerEntity.class)))
            .thenReturn(new TrainerUpdateResponseDto());
        //when
        ResponseEntity<TrainerUpdateResponseDto> response = trainerController.updateTrainer(
            RandomStringUtils.randomAlphabetic(10),
            new TrainerUpdateRequestDto());
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void registerTrainerTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(10);
        TrainerDomain registered = new TrainerDomain();
        registered.setUsername(username);
        doNothing().when(trainerValidation).validateRegisterRequest(any(TrainerRegistrationRequestDto.class));
        when(trainerConverter.convertRegistrationRequestToModel(any(TrainerRegistrationRequestDto.class)))
            .thenReturn(new TrainerEntity());
        when(trainerService.createTrainerProfile(any(TrainerEntity.class))).thenReturn(registered);
        when(trainerConverter.convertModelToRegistrationResponse(any(TrainerDomain.class)))
            .thenReturn(new RegistrationResponseDto());
        //when
        ResponseEntity<RegistrationResponseDto> response = trainerController
            .registerTrainer(new TrainerRegistrationRequestDto());
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void getActiveTrainersTest() {
        //given
        TrainerEntity trainer = new TrainerEntity();
        List<TrainerEntity> trainers = List.of(trainer);
        doNothing().when(trainerValidation).validateUsername(any(String.class));
        when(trainerService.findActiveForOtherTrainees(any(String.class)))
            .thenReturn(trainers);
        when(trainerConverter.convertModelToActiveTrainerResponse(any(TrainerEntity.class)))
            .thenReturn(new ActiveTrainerResponseDto());
        //when
        ResponseEntity<List<ActiveTrainerResponseDto>> response = trainerController.getActiveTrainers(
            RandomStringUtils.randomAlphabetic(10)
        );
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void changeActiveStatusTest() {
        //given
        doNothing().when(trainerValidation).validateUsername(any(String.class));
        doNothing().when(trainerService).changeActiveStatus(any(String.class), any(Boolean.class));
        //when
        ResponseEntity<Void> response = trainerController.changeActiveStatus(
            RandomStringUtils.randomAlphabetic(10),
            true
        );
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
