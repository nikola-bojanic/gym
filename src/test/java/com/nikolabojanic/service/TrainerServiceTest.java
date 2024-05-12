package com.nikolabojanic.service;

import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.UserEntity;
import com.nikolabojanic.repository.TrainerRepository;
import io.micrometer.core.instrument.Counter;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TraineeService traineeService;
    @Mock
    private UserService userService;
    @Mock
    private TrainingTypeService trainingTypeService;
    @Mock
    private Counter totalTransactionsCounter;
    @InjectMocks
    private TrainerService trainerService;

    @Test
    void createTrainerProfileTest() {
        //given
        UserEntity user = new UserEntity();
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        when(userService.generateUsernameAndPassword(any(UserEntity.class))).thenReturn(new UserEntity());
        when(trainerRepository.save(any(TrainerEntity.class))).thenReturn(new TrainerEntity());
        //when
        TrainerEntity createdTrainer = trainerService.createTrainerProfile(trainer);
        //then
        assertThat(createdTrainer).isNotNull();
    }


    @Test
    void updateTrainerProfileTest() {
        //given
        UserEntity user = new UserEntity();
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        when(trainerRepository.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(TrainerEntity.class))).thenReturn(new TrainerEntity());
        //when
        TrainerEntity updatedTrainer = trainerService.updateTrainerProfile(
                RandomStringUtils.randomAlphabetic(10),
                trainer);
        //then
        assertThat(updatedTrainer).isNotNull();
    }

    @Test
    void findByUsernameTest() {
        //given
        when(trainerRepository.findByUsername(any(String.class))).thenReturn(Optional.of(new TrainerEntity()));
        //when
        TrainerEntity trainer = trainerService.findByUsername(
                RandomStringUtils.randomAlphabetic(10));
        //then
        assertThat(trainer).isNotNull();
    }

    @Test
    void changeActiveStatusTest() {
        //given
        UserEntity user = new UserEntity();
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        when(trainerRepository.findByUsername(any(String.class))).thenReturn(Optional.of(trainer));
        when(trainerRepository.save(any(TrainerEntity.class))).thenReturn(new TrainerEntity());
        //then
        assertDoesNotThrow(() -> trainerService.changeActiveStatus(
                RandomStringUtils.randomAlphabetic(10), true));
    }

    @Test
    void findActiveForOtherTraineesTest() {
        //given
        TraineeEntity trainee = new TraineeEntity();
        trainee.setId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        when(traineeService.findByUsername(any(String.class))).thenReturn(trainee);
        when(trainerRepository.findActiveForOtherTrainees(any(Long.class))).thenReturn(new ArrayList<>());
        //when
        List<TrainerEntity> activeTrainers = trainerService.findActiveForOtherTrainees(
                RandomStringUtils.randomNumeric(10));

        //then
        assertThat(activeTrainers).isNotNull();
    }

}
