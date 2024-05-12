package com.nikolabojanic.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class TrainingTest {

    @Test
    void setAndGetTrainingTest() {
        Trainer trainer = new Trainer();
        Trainee trainee = new Trainee();
        LocalDate date = LocalDate.of(2023, 11, 22);
        TrainingType type = new TrainingType();
        Training training = new Training();

        training.setId(1L);
        training.setDate(date);
        training.setDuration(10.0);
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setName("name");
        training.setType(type);

        assertEquals(1L, training.getId());
        assertEquals(date, training.getDate());
        assertEquals(10.0, training.getDuration());
        assertEquals("name", training.getName());
        assertEquals(trainee, training.getTrainee());
        assertEquals(trainer, training.getTrainer());
        assertEquals(type, training.getType());
    }

    @Test
    void allArgsConstructorTest() {
        Trainer trainer = new Trainer();
        Trainee trainee = new Trainee();
        LocalDate date = LocalDate.of(2023, 11, 22);
        TrainingType type = new TrainingType();

        Training training = new Training(1L, trainee, trainer, "name", type, date, 10.0);

        assertEquals(1L, training.getId());
        assertEquals(date, training.getDate());
        assertEquals(10.0, training.getDuration());
        assertEquals("name", training.getName());
        assertEquals(trainee, training.getTrainee());
        assertEquals(trainer, training.getTrainer());
        assertEquals(type, training.getType());

    }

    @Test
    void noArgsConstructorTest() {

        Training training = new Training();

        assertNull(training.getDate());
        assertNull(training.getId());
        assertNull(training.getTrainer());
        assertNull(training.getName());
        assertNull(training.getType());
        assertNull(training.getDuration());
        assertNull(training.getTrainee());

    }
}