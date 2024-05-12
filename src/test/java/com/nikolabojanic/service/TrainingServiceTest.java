package com.nikolabojanic.service;

import com.nikolabojanic.entity.*;
import com.nikolabojanic.repository.TrainingRepository;
import io.micrometer.core.instrument.Counter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {
    @Mock
    private TrainingRepository trainingRepository;
    @Mock
    private TrainingTypeService trainingTypeService;
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private Counter totalTransactionsCounter;
    @InjectMocks
    private TrainingService trainingService;

    @Test
    void createWithNoTypeNoTraineeNoTrainerTest() {
        //arrange
        when(trainingRepository.save(any(TrainingEntity.class))).thenReturn(new TrainingEntity());
        //act
        TrainingEntity training = trainingService.create(new TrainingEntity());
        //assert
        assertThat(training).isNotNull();
    }

    @Test
    void createWithNoTraineeNoTrainerTest() {
        //arrange
        TrainingTypeEntity type = new TrainingTypeEntity();
        type.setId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        type.setId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        TrainingEntity training = new TrainingEntity();
        training.setType(type);
        when(trainingTypeService.findById(anyLong())).thenReturn(type);
        when(trainingRepository.save(training)).thenReturn(new TrainingEntity());
        //act
        TrainingEntity savedTraining = trainingService.create(training);
        //assert
        assertThat(savedTraining).isNotNull();
    }

    @Test
    void createWithNoTraineeTest() {
        //arrange
        UserEntity user = new UserEntity();
        user.setUsername(RandomStringUtils.randomAlphabetic(5));
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        TrainingTypeEntity type = new TrainingTypeEntity();
        type.setId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        type.setId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        TrainingEntity training = new TrainingEntity();
        training.setType(type);
        training.setTrainer(trainer);
        when(trainingTypeService.findById(anyLong())).thenReturn(type);
        when(trainerService.findByUsername(anyString())).thenReturn(trainer);
        when(trainingRepository.save(training)).thenReturn(new TrainingEntity());
        //act
        TrainingEntity savedTraining = trainingService.create(training);
        //assert
        assertThat(savedTraining).isNotNull();
    }

    @Test
    void createTest() {
        //arrange
        UserEntity user = new UserEntity();
        user.setUsername(RandomStringUtils.randomAlphabetic(5));
        TrainerEntity trainer = new TrainerEntity();
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        trainer.setUser(user);
        TrainingTypeEntity type = new TrainingTypeEntity();
        type.setId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        type.setId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        TrainingEntity training = new TrainingEntity();
        training.setType(type);
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        when(traineeService.findByUsername(anyString())).thenReturn(trainee);
        when(trainingTypeService.findById(anyLong())).thenReturn(type);
        when(trainerService.findByUsername(anyString())).thenReturn(trainer);
        when(trainingRepository.save(training)).thenReturn(new TrainingEntity());
        //act
        TrainingEntity savedTraining = trainingService.create(training);
        //assert
        assertThat(savedTraining).isNotNull();
    }

    @Test
    void findByTraineeAndFilterWithTypeIdTest() {
        //arrange
        LocalDate begin = LocalDate.of(2022, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 1);
        when(trainingRepository.
                findByTraineeUserUsernameAndDateBetweenAndTrainerUserFirstNameContainingIgnoreCaseAndTypeId(
                        anyString(),
                        any(LocalDate.class),
                        any(LocalDate.class),
                        anyString(),
                        anyLong()
                )).thenReturn(new ArrayList<>());
        //act
        List<TrainingEntity> trainings = trainingService.findByTraineeAndFilter(
                RandomStringUtils.randomAlphabetic(10),
                begin,
                end,
                RandomStringUtils.randomAlphabetic(10),
                Long.parseLong(RandomStringUtils.randomNumeric(5))

        );
        //assert
        assertThat(trainings).isNotNull();
    }

    @Test
    void findByTraineeAndFilterWithoutTypeIdTest() {
        //arrange
        LocalDate begin = LocalDate.of(2022, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 1);
        when(trainingRepository.
                findByTraineeUserUsernameAndDateBetweenAndTrainerUserFirstNameContainingIgnoreCase(
                        anyString(),
                        any(LocalDate.class),
                        any(LocalDate.class),
                        anyString()
                )).thenReturn(new ArrayList<>());
        //act
        List<TrainingEntity> trainings = trainingService.findByTraineeAndFilter(
                RandomStringUtils.randomAlphabetic(10),
                begin,
                end,
                RandomStringUtils.randomAlphabetic(10),
                null
        );
        //assert
        assertThat(trainings).isNotNull();
    }

    @Test
    void findByTrainerAndFilterTest() {
        //arrange
        LocalDate begin = LocalDate.of(2022, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 1);
        when(trainingRepository
                .findByTrainerUserUsernameAndDateBetweenAndTraineeUserFirstNameContainingIgnoreCase(
                        anyString(),
                        any(LocalDate.class),
                        any(LocalDate.class),
                        anyString()
                )).thenReturn(new ArrayList<>());
        //act
        List<TrainingEntity> trainings = trainingService.findByTrainerAndFilter(
                RandomStringUtils.randomAlphabetic(10),
                begin,
                end,
                RandomStringUtils.randomAlphabetic(10)
        );
        //assert
        assertThat(trainings).isNotNull();
    }
}