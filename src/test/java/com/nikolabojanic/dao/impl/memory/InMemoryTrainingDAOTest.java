package com.nikolabojanic.dao.impl.memory;

import com.nikolabojanic.config.TraineeFileStorage;
import com.nikolabojanic.config.TrainerFileStorage;
import com.nikolabojanic.config.TrainingFileStorage;
import com.nikolabojanic.model.Trainee;
import com.nikolabojanic.model.Trainer;
import com.nikolabojanic.model.Training;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InMemoryTrainingDAOTest {
    @Mock
    private TrainingFileStorage trainingFileStorage;
    @Mock
    private TraineeFileStorage traineeFileStorage;
    @Mock
    private TrainerFileStorage trainerFileStorage;
    @InjectMocks
    private InMemoryTrainingDAO inMemoryTrainingDAO;


    @Test
    void getByIdTest() {
        Training training = new Training(Long.parseLong(RandomStringUtils.randomNumeric(2, 4)));
        Map<Long, Training> trainings = new HashMap<>();
        trainings.put(training.getId(), training);
        when(trainingFileStorage.getTrainings()).thenReturn(trainings);

        Training retrieved = inMemoryTrainingDAO.findById(training.getId()).get();

        assertEquals(training.getId(), retrieved.getId());
    }

    @Test
    void saveNewTest() {
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(2, 4));
        Long trainerId = Long.parseLong(RandomStringUtils.randomNumeric(2, 4));
        Long traineeId = Long.parseLong(RandomStringUtils.randomNumeric(2, 4));
        Trainer trainer = new Trainer(trainerId);
        Trainee trainee = new Trainee(traineeId);
        Training training = new Training(trainee, trainer);
        Map<Long, Trainer> trainers = new HashMap<>();
        Map<Long, Trainee> trainees = new HashMap<>();
        trainers.put(trainerId, trainer);
        trainees.put(traineeId, trainee);
        Set<Training> trainings = new HashSet<>();
        trainings.add(training);
        when(traineeFileStorage.getTrainees()).thenReturn(trainees);
        when(trainerFileStorage.getTrainers()).thenReturn(trainers);
        when(trainingFileStorage.getMaxId()).thenReturn(id);

        Training saved = inMemoryTrainingDAO.save(training);
        assertEquals(trainings, traineeFileStorage.getTrainees().get(traineeId).getTrainings());
        assertEquals(trainings, trainerFileStorage.getTrainers().get(trainerId).getTrainings());
        assertEquals(id, saved.getId());
    }

    @Test
    void saveExistingTest() {
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(2, 4));
        Long trainerId = Long.parseLong(RandomStringUtils.randomNumeric(2, 4));
        Long traineeId = Long.parseLong(RandomStringUtils.randomNumeric(2, 4));
        Trainer trainer = new Trainer(trainerId);
        Trainee trainee = new Trainee(traineeId);
        Training training = new Training(id, trainee, trainer);
        Map<Long, Trainer> trainers = new HashMap<>();
        Map<Long, Trainee> trainees = new HashMap<>();
        trainers.put(trainerId, trainer);
        trainees.put(traineeId, trainee);
        Set<Training> trainings = new HashSet<>();
        trainings.add(training);
        when(traineeFileStorage.getTrainees()).thenReturn(trainees);
        when(trainerFileStorage.getTrainers()).thenReturn(trainers);

        Training saved = inMemoryTrainingDAO.save(training);
        assertEquals(trainings, traineeFileStorage.getTrainees().get(traineeId).getTrainings());
        assertEquals(trainings, trainerFileStorage.getTrainers().get(trainerId).getTrainings());
        assertEquals(training, saved);
    }
}