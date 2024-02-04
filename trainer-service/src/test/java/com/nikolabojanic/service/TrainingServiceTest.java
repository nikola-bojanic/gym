package com.nikolabojanic.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.entity.TrainingEntity;
import com.nikolabojanic.exception.TsEntityNotFoundException;
import com.nikolabojanic.repository.TrainingRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    private TrainerService trainerService;
    @InjectMocks
    private TrainingService trainingService;

    @Test
    void addTrainingTest() {
        //arrange
        TrainingEntity training = new TrainingEntity();
        training.setTrainer(new TrainerEntity());
        when(trainerService.addTrainer(any(TrainerEntity.class))).thenReturn(new TrainerEntity());
        when(trainingRepository.save(any(TrainingEntity.class))).thenReturn(new TrainingEntity());
        //act
        TrainingEntity savedTraining = trainingService.addTraining(training);
        //assert
        assertThat(savedTraining).isNotNull();
    }

    @Test
    void deleteTrainingsTest() {
        //arrange
        doNothing().when(trainingRepository).deleteTrainingForTrainerAndDate(anyString(), any(LocalDate.class));
        when(trainingRepository.findByUsernameAndDate(anyString(), any(LocalDate.class)))
            .thenReturn(List.of(new TrainingEntity()));
        //act
        List<TrainingEntity> deletedTrainings = trainingService
            .deleteTrainings(RandomStringUtils.randomAlphabetic(5), LocalDate.now());
        //assert
        assertThat(deletedTrainings).isNotEmpty();
    }

    @Test
    void deleteNonExistentTrainingsTest() {
        //arrange
        String username = RandomStringUtils.randomAlphabetic(5);
        LocalDate date = LocalDate.now();
        when(trainingRepository.findByUsernameAndDate(anyString(), any(LocalDate.class)))
            .thenReturn(new ArrayList<>());
        //act
        assertThatThrownBy(() ->
            trainingService.deleteTrainings(username, date))
            .isInstanceOf(TsEntityNotFoundException.class)
            .hasMessage("Trainings for trainer: " + username + " and date: " + date + " do not exist");
    }

    @Test
    void findByTrainerTest() {
        //arrange
        when(trainingRepository.findByTrainerUsername(anyString())).thenReturn(List.of(new TrainingEntity()));
        //act
        List<TrainingEntity> trainings = trainingService.findByTrainer(RandomStringUtils.randomAlphabetic(5));
        //assert
        assertThat(trainings).isNotNull();
    }

    @Test
    void findByNonExistentTrainerTest() {
        //arrange
        String username = RandomStringUtils.randomAlphabetic(5);

        when(trainingRepository.findByTrainerUsername(anyString())).thenReturn(new ArrayList<>());
        //act
        assertThatThrownBy(() -> trainingService.findByTrainer(username))
            .isInstanceOf(TsEntityNotFoundException.class)
            .hasMessage("Trainings for trainer: " + username + " do not exist");
    }
}