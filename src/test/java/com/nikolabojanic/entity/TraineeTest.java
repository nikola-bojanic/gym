package com.nikolabojanic.entity;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class TraineeTest {

    @Test
    void setAndGetTraineeTest() {
        TraineeEntity trainee = new TraineeEntity();
        UserEntity user = new UserEntity();
        List<TrainerEntity> trainers = new ArrayList<>();
        List<TrainingEntity> trainings = new ArrayList<>();
        LocalDate date = LocalDate.of(2023, 11, 22);
        String address = RandomStringUtils.randomAlphabetic(3, 6);
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));

        trainee.setId(id);
        trainee.setUser(user);
        trainee.setAddress(address);
        trainee.setDateOfBirth(date);
        trainee.setTrainers(trainers);
        trainee.setTrainings(trainings);

        assertEquals(id, trainee.getId());
        assertEquals(address, trainee.getAddress());
        assertEquals(user, trainee.getUser());
        assertEquals(date, trainee.getDateOfBirth());
        assertEquals(trainers, trainee.getTrainers());
        assertEquals(trainings, trainee.getTrainings());
    }

    @Test
    void allArgsConstructorTest() {
        UserEntity user = new UserEntity();
        List<TrainerEntity> trainers = new ArrayList<>();
        List<TrainingEntity> trainings = new ArrayList<>();
        LocalDate date = LocalDate.of(2023, 11, 22);
        String address = RandomStringUtils.randomAlphabetic(3, 6);
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));

        TraineeEntity trainee = new TraineeEntity(id, date, address, user, trainers, trainings);

        assertEquals(id, trainee.getId());
        assertEquals(address, trainee.getAddress());
        assertEquals(user, trainee.getUser());
        assertEquals(date, trainee.getDateOfBirth());
        assertEquals(trainers, trainee.getTrainers());
        assertEquals(trainings, trainee.getTrainings());
    }

    @Test
    void noArgsConstructorTest() {
        TraineeEntity trainee = new TraineeEntity();

        assertNull(trainee.getId());
        assertNull(trainee.getAddress());
        assertNull(trainee.getUser());
        assertNull(trainee.getDateOfBirth());
        assertNull(trainee.getTrainings());
        assertNull(trainee.getTrainers());
    }
}