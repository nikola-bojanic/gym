package com.nikolabojanic.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class TrainingTypeTest {

    @Test
    void setAndGetTrainingTypeTest() {
        TrainingTypeEntity type = new TrainingTypeEntity();
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        List<TrainerEntity> trainers = new ArrayList<>();
        List<TrainingEntity> trainings = new ArrayList<>();
        String name = RandomStringUtils.randomAlphabetic(3, 6);

        type.setId(id);
        type.setName(name);
        type.setTrainings(trainings);
        type.setTrainers(trainers);

        assertEquals(id, type.getId());
        assertEquals(name, type.getName());
        assertEquals(trainings, type.getTrainings());
        assertEquals(trainers, type.getTrainers());
    }

    @Test
    void allArgsConstructorTest() {
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        List<TrainerEntity> trainers = new ArrayList<>();
        List<TrainingEntity> trainings = new ArrayList<>();
        String name = RandomStringUtils.randomAlphabetic(3, 6);
        TrainingTypeEntity type = new TrainingTypeEntity(id, name, trainers, trainings);

        assertEquals(id, type.getId());
        assertEquals(name, type.getName());
        assertEquals(trainers, type.getTrainers());
        assertEquals(trainings, type.getTrainings());
    }

    @Test
    void noArgsConstructorTest() {
        TrainingTypeEntity type = new TrainingTypeEntity();

        assertNull(type.getId());
        assertNull(type.getName());
        assertNull(type.getTrainers());
        assertNull(type.getTrainings());
    }
}