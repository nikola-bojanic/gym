package com.nikolabojanic.service;

import com.nikolabojanic.dao.TraineeDAO;
import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.dto.TraineeTrainerUpdateRequestDTO;
import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.UserEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
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
    private TraineeDAO traineeDAO;
    @Mock
    private UserService userService;
    @Mock
    private TrainerService trainerService;
    @InjectMocks
    private TraineeService traineeService;

    @Test
    void createTraineeProfileTest() {
        //given
        UserEntity user = new UserEntity();
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        when(userService.generateUsernameAndPassword(any(UserEntity.class))).thenReturn(new UserEntity());
        when(traineeDAO.save(any(TraineeEntity.class))).thenReturn(new TraineeEntity());
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
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));
        doNothing().when(userService).authentication(any(AuthDTO.class));
        when(traineeDAO.save(any(TraineeEntity.class))).thenReturn(new TraineeEntity());
        //when
        TraineeEntity updatedTrainee = traineeService.updateTraineeProfile(
                new AuthDTO(),
                RandomStringUtils.randomAlphabetic(10),
                trainee);
        //then
        assertThat(updatedTrainee).isNotNull();
    }

    @Test
    void findByUsernameTest() {
        //given
        doNothing().when(userService).authentication(any(AuthDTO.class));
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(new TraineeEntity()));
        //when
        TraineeEntity trainee = traineeService.findByUsername(
                new AuthDTO(),
                RandomStringUtils.randomAlphabetic(10));
        //then
        assertThat(trainee).isNotNull();
    }

    @Test
    void deleteByUsernameTest() {
        //given
        doNothing().when(userService).authentication(any(AuthDTO.class));
        when(traineeDAO.deleteByUsername(any(String.class))).thenReturn(Optional.of(new TraineeEntity()));
        //when
        TraineeEntity deleted = traineeService.deleteByUsername(
                new AuthDTO(),
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
        doNothing().when(userService).authentication(any(AuthDTO.class));
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(new TraineeEntity()));
        when(trainerService.findByUsername(any(AuthDTO.class), any(String.class))).thenReturn(trainer);
        when(traineeDAO.saveTrainers(any(TraineeEntity.class), any(List.class))).thenReturn(new ArrayList<>());
        //when
        List<TrainerEntity> trainers = traineeService.updateTraineeTrainers(
                new AuthDTO(),
                RandomStringUtils.randomAlphabetic(10),
                List.of(requestDTO));
        //then
        assertThat(trainers).isNotNull();
    }

    @Test
    void changeActiveStatusTest() {
        //given
        UserEntity user = new UserEntity();
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        doNothing().when(userService).authentication(any(AuthDTO.class));
        when(traineeDAO.findByUsername(any(String.class))).thenReturn(Optional.of(trainee));
        when(traineeDAO.save(any(TraineeEntity.class))).thenReturn(new TraineeEntity());
        //then
        assertDoesNotThrow(() -> traineeService.changeActiveStatus(
                new AuthDTO(),
                RandomStringUtils.randomAlphabetic(10), true));
    }

}