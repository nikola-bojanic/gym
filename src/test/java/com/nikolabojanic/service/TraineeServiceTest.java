package com.nikolabojanic.service;

import com.nikolabojanic.dto.TraineeTrainerUpdateRequestDTO;
import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.UserEntity;
import com.nikolabojanic.repository.TraineeRepository;
import io.micrometer.core.instrument.Counter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {
    @Mock
    private TraineeRepository traineeRepository;
    @Mock
    private UserService userService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private Counter totalTransactionsCounter;
    @InjectMocks
    private TraineeService traineeService;

    @Test
    void createTraineeProfileTest() {
        //given
        UserEntity user = new UserEntity();
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        when(userService.generateUsernameAndPassword(any(UserEntity.class))).thenReturn(new UserEntity());
        when(traineeRepository.save(any(TraineeEntity.class))).thenReturn(new TraineeEntity());
        //when
        TraineeEntity createdTrainee = traineeService.createTraineeProfile(trainee);
        //then
        assertThat(createdTrainee).isNotNull();
    }

    @Test
    void updateTraineeProfileTest() {
        //given
        UserEntity user = new UserEntity();
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        when(traineeRepository.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(TraineeEntity.class))).thenReturn(new TraineeEntity());
        //when
        TraineeEntity updatedTrainee = traineeService.updateTraineeProfile(
                RandomStringUtils.randomAlphabetic(10),
                trainee);
        //then
        assertThat(updatedTrainee).isNotNull();
    }

    @Test
    void findByUsernameTest() {
        //given
        when(traineeRepository.findByUsername(any(String.class))).thenReturn(Optional.of(new TraineeEntity()));
        //when
        TraineeEntity trainee = traineeService.findByUsername(
                RandomStringUtils.randomAlphabetic(10));
        //then
        assertThat(trainee).isNotNull();
    }

    @Test
    void deleteByUsernameTest() {
        //given
        when(traineeRepository.findByUsername(any(String.class))).thenReturn(Optional.of(new TraineeEntity()));
        doNothing().when(traineeRepository).deleteByUsername(any(String.class));
        //when
        TraineeEntity deleted = traineeService.deleteByUsername(
                RandomStringUtils.randomAlphabetic(10));
        //then
        assertThat(deleted).isNotNull();
    }

    @Test
    void updateTraineeTrainersTest() {
        //given
        TraineeTrainerUpdateRequestDTO requestDTO = new TraineeTrainerUpdateRequestDTO();
        requestDTO.setUsername(RandomStringUtils.randomAlphabetic(10));
        UserEntity user = new UserEntity();
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        when(traineeRepository.findByUsername(any(String.class))).thenReturn(Optional.of(new TraineeEntity()));
        when(trainerService.findByUsername(any(String.class))).thenReturn(trainer);
        when(traineeRepository.save(any(TraineeEntity.class))).thenReturn(new TraineeEntity());
        //when
        TraineeEntity updated = traineeService.updateTraineeTrainers(
                RandomStringUtils.randomAlphabetic(10),
                List.of(requestDTO));
        //then
        assertThat(updated).isNotNull();
    }

    @Test
    void changeActiveStatusTest() {
        //given
        UserEntity user = new UserEntity();
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        when(traineeRepository.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));
        when(traineeRepository.save(any(TraineeEntity.class))).thenReturn(new TraineeEntity());
        //then
        assertDoesNotThrow(() -> traineeService.changeActiveStatus(
                RandomStringUtils.randomAlphabetic(10), true));
    }

}