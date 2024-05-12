package com.nikolabojanic.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class TraineeTest {

    @Test
    void setAndGetTraineeTest() {
        //Arrange
        Trainee trainee = new Trainee();
        User user = new User();
        Set<Trainer> trainers = new HashSet<>();
        Set<Training> trainings = new HashSet<>();
        LocalDate date = LocalDate.of(2023, 11, 22);
        //Act
        trainee.setId(1L);
        trainee.setUser(user);
        trainee.setAddress("address");
        trainee.setTrainings(trainings);
        trainee.setTrainers(trainers);
        trainee.setDateOfBirth(date);
        //Assert
        assertEquals(1L, trainee.getId());
        assertEquals("address", trainee.getAddress());
        assertEquals(trainings, trainee.getTrainings());
        assertEquals(trainers, trainee.getTrainers());
        assertEquals(user, trainee.getUser());
        assertEquals(date, trainee.getDateOfBirth());
    }

    @Test
    void allArgsConstructorTest() {
        //Arrange and act
        User user = new User();
        Set<Trainer> trainers = new HashSet<>();
        Set<Training> trainings = new HashSet<>();
        LocalDate date = LocalDate.of(2023, 11, 22);
        Trainee trainee = new Trainee(1L, date, "address", user, trainers, trainings);
        //Assert
        assertEquals(1L, trainee.getId());
        assertEquals("address", trainee.getAddress());
        assertEquals(trainings, trainee.getTrainings());
        assertEquals(trainers, trainee.getTrainers());
        assertEquals(user, trainee.getUser());
        assertEquals(date, trainee.getDateOfBirth());
    }

    @Test
    void noArgsConstructorTest() {
        //Arrange and act
        Trainee trainee = new Trainee();
        //Assert
        assertNull(trainee.getId());
        assertNull(trainee.getAddress());
        assertEquals(new HashSet<Training>(), trainee.getTrainings());
        assertEquals(new HashSet<Trainer>(), trainee.getTrainers());
        assertNull(trainee.getUser());
        assertNull(trainee.getDateOfBirth());
    }
}