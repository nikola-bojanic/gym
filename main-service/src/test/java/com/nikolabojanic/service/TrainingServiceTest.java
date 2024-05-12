package com.nikolabojanic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.nikolabojanic.entity.TraineeEntity;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.entity.TrainingEntity;
import com.nikolabojanic.entity.TrainingTypeEntity;
import com.nikolabojanic.entity.UserEntity;
import com.nikolabojanic.exception.ScEntityNotFoundException;
import com.nikolabojanic.repository.TrainingRepository;
import com.nikolabojanic.service.jms.MessageSender;
import io.micrometer.core.instrument.Counter;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
    private MessageSender messageSender;
    @Mock
    private Counter totalTransactionsCounter;
    @InjectMocks
    private TrainingService trainingService;

    @Test
    void createWithNoTypeNoTraineeNoTrainerTest() {
        //arrange
        TrainingEntity training = new TrainingEntity();
        TrainerEntity trainer = new TrainerEntity();
        UserEntity user = new UserEntity();
        user.setUsername(RandomStringUtils.randomAlphabetic(5));
        trainer.setUser(user);
        training.setTrainer(trainer);
        when(trainingRepository.save(any(TrainingEntity.class))).thenReturn(new TrainingEntity());
        when(trainerService.findByUsername(anyString())).thenReturn(trainer);
        //act
        TrainingEntity addedTraining = trainingService.create(training, RandomStringUtils.randomAlphabetic(5));
        //assert
        assertThat(addedTraining).isNotNull();
    }

    @Test
    void createWithNoTraineeNoTrainerTest() {
        //arrange
        TrainingTypeEntity type = new TrainingTypeEntity();
        type.setId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        type.setId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        TrainingEntity training = new TrainingEntity();
        TrainerEntity trainer = new TrainerEntity();
        UserEntity user = new UserEntity();
        user.setUsername(RandomStringUtils.randomAlphabetic(5));
        trainer.setUser(user);
        training.setTrainer(trainer);
        training.setType(type);
        when(trainerService.findByUsername(anyString())).thenReturn(trainer);
        when(trainingTypeService.findById(anyLong())).thenReturn(type);
        when(trainingRepository.save(training)).thenReturn(new TrainingEntity());
        //act
        TrainingEntity savedTraining = trainingService.create(training, RandomStringUtils.randomAlphabetic(5));
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
        TrainingEntity savedTraining = trainingService.create(training, RandomStringUtils.randomAlphabetic(5));
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
        TrainingEntity savedTraining = trainingService.create(training, RandomStringUtils.randomAlphabetic(5));
        //assert
        assertThat(savedTraining).isNotNull();
    }

    @Test
    void findByTraineeAndFilterWithTypeIdTest() {
        //arrange
        LocalDate begin = LocalDate.of(2022, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 1);
        when(
            trainingRepository
                .findByTraineeUserUsernameAndDateBetweenAndTrainerUserFirstNameContainingIgnoreCaseAndTypeId(
                    anyString(), any(LocalDate.class), any(LocalDate.class), anyString(), anyLong()))
            .thenReturn(new ArrayList<>());
        //act
        List<TrainingEntity> trainings =
            trainingService.findByTraineeAndFilter(RandomStringUtils.randomAlphabetic(10), begin, end,
                RandomStringUtils.randomAlphabetic(10), Long.parseLong(RandomStringUtils.randomNumeric(5))

            );
        //assert
        assertThat(trainings).isNotNull();
    }

    @Test
    void deleteTrainingTest() {
        //arrange
        UserEntity user = new UserEntity();
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        TrainingEntity training = new TrainingEntity();
        training.setTrainer(trainer);
        doNothing().when(totalTransactionsCounter).increment();
        when(trainingRepository.findById(anyLong())).thenReturn(Optional.of(training));
        doNothing().when(trainingRepository).deleteById(anyLong());
        //act
        TrainingEntity deletedTraining =
            trainingService.deleteTraining(Long.parseLong(RandomStringUtils.randomNumeric(5)),
                RandomStringUtils.randomAlphabetic(5));
        //assert
        assertThat(deletedTraining).isNotNull();
    }

    @Test
    void deleteNonExistentTrainingTest() {
        //arrange
        doNothing().when(totalTransactionsCounter).increment();
        when(trainingRepository.findById(anyLong())).thenReturn(Optional.empty());
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(5));
        //act
        assertThatThrownBy(() -> trainingService.deleteTraining(id, RandomStringUtils.randomAlphabetic(5)))
            //assert
            .isInstanceOf(ScEntityNotFoundException.class).hasMessage("Training with id " + id + " doesn't exist");

    }

    @Test
    void findByTraineeAndFilterWithoutTypeIdTest() {
        //arrange
        LocalDate begin = LocalDate.of(2022, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 1);
        when(trainingRepository.findByTraineeUserUsernameAndDateBetweenAndTrainerUserFirstNameContainingIgnoreCase(
            anyString(), any(LocalDate.class), any(LocalDate.class), anyString())).thenReturn(new ArrayList<>());
        //act
        List<TrainingEntity> trainings =
            trainingService.findByTraineeAndFilter(RandomStringUtils.randomAlphabetic(10), begin, end,
                RandomStringUtils.randomAlphabetic(10), null);
        //assert
        assertThat(trainings).isNotNull();
    }

    @Test
    void findByTrainerAndFilterTest() {
        //arrange
        LocalDate begin = LocalDate.of(2022, 1, 1);
        LocalDate end = LocalDate.of(2024, 1, 1);
        when(trainingRepository.findByTrainerUserUsernameAndDateBetweenAndTraineeUserFirstNameContainingIgnoreCase(
            anyString(), any(LocalDate.class), any(LocalDate.class), anyString())).thenReturn(new ArrayList<>());
        //act
        List<TrainingEntity> trainings =
            trainingService.findByTrainerAndFilter(RandomStringUtils.randomAlphabetic(10), begin, end,
                RandomStringUtils.randomAlphabetic(10));
        //assert
        assertThat(trainings).isNotNull();
    }
}