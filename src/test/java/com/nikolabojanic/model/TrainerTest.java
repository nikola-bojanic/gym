package com.nikolabojanic.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class TrainerTest {

    @Test
    void setAndGetTrainer() {
        User user = new User();
        TrainingType specialization = new TrainingType();
        Set<Training> trainings = new HashSet<>();
        Set<Trainee> trainees = new HashSet<>();
        Trainer trainer = new Trainer();

        trainer.setId(1L);
        trainer.setTrainees(trainees);
        trainer.setUser(user);
        trainer.setTrainings(trainings);
        trainer.setSpecialization(specialization);

        assertEquals(1L, trainer.getId());
        assertEquals(trainees, trainer.getTrainees());
        assertEquals(trainings, trainer.getTrainings());
        assertEquals(user, trainer.getUser());
        assertEquals(specialization, trainer.getSpecialization());
    }

    @Test
    void allArgsConstructorTest() {
        User user = new User();
        TrainingType specialization = new TrainingType();
        Set<Training> trainings = new HashSet<>();
        Set<Trainee> trainees = new HashSet<>();
        Trainer trainer = new Trainer(1L, user, trainees, trainings, specialization);

        assertEquals(1L, trainer.getId());
        assertEquals(trainees, trainer.getTrainees());
        assertEquals(trainings, trainer.getTrainings());
        assertEquals(user, trainer.getUser());
        assertEquals(specialization, trainer.getSpecialization());
    }

    @Test
    void noArgsConstructorTest() {
        Trainer trainer = new Trainer();

        assertNull(trainer.getSpecialization());
        assertNull(trainer.getId());
        assertEquals(new HashSet<Trainee>(), trainer.getTrainees());
        assertEquals(new HashSet<Training>(), trainer.getTrainings());
        assertNull(trainer.getUser());
    }

}