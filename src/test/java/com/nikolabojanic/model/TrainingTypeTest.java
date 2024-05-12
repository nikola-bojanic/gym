package com.nikolabojanic.model;

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
        List<Training> trainings = new ArrayList<>();
        List<Trainer> trainers = new ArrayList<>();
        TrainingType type = new TrainingType();

        type.setId(1L);
        type.setTrainings(trainings);
        type.setTrainers(trainers);
        type.setName("name");

        assertEquals(1L, type.getId());
        assertEquals(trainers, type.getTrainers());
        assertEquals(trainings, type.getTrainings());
        assertEquals("name", type.getName());
    }

    @Test
    void allArgsConstructorTest() {
        List<Training> trainings = new ArrayList<>();
        List<Trainer> trainers = new ArrayList<>();
        TrainingType type = new TrainingType(1L, "name", trainings, trainers);

        assertEquals(1L, type.getId());
        assertEquals(trainers, type.getTrainers());
        assertEquals(trainings, type.getTrainings());
        assertEquals("name", type.getName());
    }

    @Test
    void noArgsConstructorTest() {
        TrainingType type = new TrainingType();

        assertNull(type.getTrainings());
        assertNull(type.getId());
        assertNull(type.getTrainers());
        assertNull(type.getName());
    }

}