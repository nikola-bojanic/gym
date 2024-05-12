package com.nikolabojanic.entity;

import org.apache.commons.lang3.RandomStringUtils;
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
        TrainerEntity trainer = new TrainerEntity();
        TraineeEntity trainee = new TraineeEntity();
        LocalDate date = LocalDate.of(2023, 11, 22);
        TrainingTypeEntity type = new TrainingTypeEntity();
        TrainingEntity training = new TrainingEntity();
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        String name = RandomStringUtils.randomAlphabetic(3, 6);
        Double duration = Double.parseDouble(RandomStringUtils.randomNumeric(3, 6));

        training.setId(id);
        training.setDate(date);
        training.setDuration(duration);
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setName(name);
        training.setType(type);

        assertEquals(id, training.getId());
        assertEquals(date, training.getDate());
        assertEquals(duration, training.getDuration());
        assertEquals(name, training.getName());
        assertEquals(trainee, training.getTrainee());
        assertEquals(trainer, training.getTrainer());
        assertEquals(type, training.getType());
    }

    @Test
    void allArgsConstructorTest() {
        TrainerEntity trainer = new TrainerEntity();
        TraineeEntity trainee = new TraineeEntity();
        LocalDate date = LocalDate.of(2023, 11, 22);
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        String name = RandomStringUtils.randomAlphabetic(3, 6);
        Double duration = Double.parseDouble(RandomStringUtils.randomNumeric(3, 6));
        TrainingTypeEntity type = new TrainingTypeEntity();

        TrainingEntity training = new TrainingEntity(id, trainee, trainer, name, type, date, duration);

        assertEquals(id, training.getId());
        assertEquals(date, training.getDate());
        assertEquals(duration, training.getDuration());
        assertEquals(name, training.getName());
        assertEquals(trainee, training.getTrainee());
        assertEquals(trainer, training.getTrainer());
        assertEquals(type, training.getType());

    }

    @Test
    void noArgsConstructorTest() {
        TrainingEntity training = new TrainingEntity();

        assertNull(training.getDate());
        assertNull(training.getId());
        assertNull(training.getTrainer());
        assertNull(training.getName());
        assertNull(training.getType());
        assertNull(training.getDuration());
        assertNull(training.getTrainee());

    }
}