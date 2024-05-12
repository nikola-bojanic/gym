package com.nikolabojanic.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerTest {

    @Test
    void setAndGetTrainer() {
        UserEntity user = new UserEntity();
        TrainingTypeEntity specialization = new TrainingTypeEntity();
        TrainerEntity trainer = new TrainerEntity();
        List<TrainingEntity> trainings = new ArrayList<>();
        List<TraineeEntity> trainees = new ArrayList<>();
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));

        trainer.setId(id);
        trainer.setUser(user);
        trainer.setSpecialization(specialization);
        trainer.setTrainees(trainees);
        trainer.setTrainings(trainings);

        assertEquals(id, trainer.getId());
        assertEquals(user, trainer.getUser());
        assertEquals(specialization, trainer.getSpecialization());
        assertEquals(trainings, trainer.getTrainings());
        assertEquals(trainees, trainer.getTrainees());
    }

    @Test
    void allArgsConstructorTest() {
        UserEntity user = new UserEntity();
        TrainingTypeEntity specialization = new TrainingTypeEntity();
        List<TrainingEntity> trainings = new ArrayList<>();
        List<TraineeEntity> trainees = new ArrayList<>();
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));

        TrainerEntity trainer = new TrainerEntity(id, user, specialization, trainees, trainings);

        assertEquals(id, trainer.getId());
        assertEquals(user, trainer.getUser());
        assertEquals(specialization, trainer.getSpecialization());
        assertEquals(trainings, trainer.getTrainings());
        assertEquals(trainees, trainer.getTrainees());
    }

    @Test
    void noArgsConstructorTest() {
        TrainerEntity trainer = new TrainerEntity();

        assertNull(trainer.getSpecialization());
        assertNull(trainer.getId());
        assertNull(trainer.getUser());
        assertNull(trainer.getTrainees());
        assertNull(trainer.getTrainings());
    }

}