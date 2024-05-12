package com.nikolabojanic.dao.impl.memory;

import com.nikolabojanic.config.TraineeFileStorage;
import com.nikolabojanic.config.TrainerFileStorage;
import com.nikolabojanic.model.Trainee;
import com.nikolabojanic.model.Trainer;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InMemoryTrainerDAOTest {
    @Mock
    private TraineeFileStorage traineeFileStorage;
    @Mock
    private TrainerFileStorage trainerFileStorage;
    @InjectMocks
    private InMemoryTrainerDAO inMemoryTrainerDAO;


    @Test
    void getAllTest() {
        Map<Long, Trainer> trainers = new HashMap<>();
        Trainer trainer1 = new Trainer(Long.parseLong(RandomStringUtils.randomNumeric(2, 4)));
        Trainer trainer2 = new Trainer(Long.parseLong(RandomStringUtils.randomNumeric(2, 4)));
        trainers.put(1L, trainer1);
        trainers.put(2L, trainer2);
        when(trainerFileStorage.getTrainers()).thenReturn(trainers);

        List<Trainer> retrieved = inMemoryTrainerDAO.getAll();

        assertEquals(trainers.size(), retrieved.size());
        assertEquals(trainers.get(1L), retrieved.get(0));
        assertEquals(trainers.get(2L), retrieved.get(1));
    }

    @Test
    void findByIdTest() {
        Map<Long, Trainer> trainers = new HashMap<>();
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(2, 4));
        Trainer trainer = new Trainer(id);
        trainers.put(trainer.getId(), trainer);
        when(trainerFileStorage.getTrainers()).thenReturn(trainers);

        Trainer retrieved = inMemoryTrainerDAO.findById(id).get();

        assertEquals(trainer, retrieved);
    }

    @Test
    void saveNewTest() {
        Long trainerId = Long.parseLong(RandomStringUtils.randomNumeric(2, 4));
        Long traineeId = Long.parseLong(RandomStringUtils.randomNumeric(2, 4));
        Trainer trainer = new Trainer();
        Trainee trainee = new Trainee(traineeId);
        trainer.getTrainees().add(trainee);
        Map<Long, Trainee> trainees = new HashMap<>();
        trainees.put(traineeId, trainee);
        Set<Trainer> newTrainers = new HashSet<>();
        newTrainers.add(trainer);
        when(trainerFileStorage.getMaxId()).thenReturn(trainerId);
        when(traineeFileStorage.getTrainees()).thenReturn(trainees);

        Trainer saved = inMemoryTrainerDAO.save(trainer);

        assertEquals(trainerId, saved.getId());
        assertEquals(newTrainers, traineeFileStorage.getTrainees().get(traineeId).getTrainers());
        assertEquals(newTrainers.size(), traineeFileStorage.getTrainees().get(traineeId).getTrainers().size());
    }

    @Test
    void saveExistingTest() {
        Long trainerId = Long.parseLong(RandomStringUtils.randomNumeric(2, 4));
        Long traineeId = Long.parseLong(RandomStringUtils.randomNumeric(2, 4));
        Trainer trainer = new Trainer(trainerId);
        Trainee trainee = new Trainee(traineeId);
        trainer.getTrainees().add(trainee);
        Map<Long, Trainee> trainees = new HashMap<>();
        trainees.put(traineeId, trainee);
        Set<Trainer> newTrainers = new HashSet<>();
        newTrainers.add(trainer);
        when(traineeFileStorage.getTrainees()).thenReturn(trainees);

        Trainer saved = inMemoryTrainerDAO.save(trainer);

        assertEquals(trainer, saved);
        assertEquals(newTrainers, traineeFileStorage.getTrainees().get(traineeId).getTrainers());
        assertEquals(newTrainers.size(), traineeFileStorage.getTrainees().get(traineeId).getTrainers().size());
    }
}