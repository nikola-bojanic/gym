package com.nikolabojanic.service;

import com.nikolabojanic.dao.TrainerDAO;
import com.nikolabojanic.model.Trainer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerServiceTest {
    @Mock
    private TrainerDAO trainerDAO;

    @InjectMocks
    private TrainerService trainerService;

    @Test
    void createTest() {
        //Arrange
        Trainer trainer = new Trainer();
        when(trainerDAO.save(trainer)).thenReturn(new Trainer());
        //Act
        Trainer createdTrainer = trainerService.create(trainer);
        //Assert
        assertNotNull(createdTrainer);
    }

    @Test
    void updateTest() {
        //Arrange
        Trainer trainer = new Trainer();
        trainer.setId(1L);
        when(trainerDAO.findById(trainer.getId())).thenReturn(Optional.of(new Trainer()));
        when(trainerDAO.save(trainer)).thenReturn(new Trainer());
        //Act
        Trainer updatedTrainer = trainerService.update(trainer);
        //Assert
        assertNotNull(updatedTrainer);

    }

    @Test
    void findByIdTest() {
        //Arrange
        Long id = 1L;
        when(trainerDAO.findById(id)).thenReturn(Optional.of(new Trainer()));
        //Act
        Trainer fetchedTrainer = trainerService.findById(id);
        //Assert
        assertNotNull(fetchedTrainer);
    }

    @Test
    void createTrainerWithIdTest() {
        Trainer trainer = new Trainer();
        trainer.setId(1L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerService.create(trainer);
        });

        assertEquals("Cannot create a trainer with an ID", exception.getMessage());
    }

    @Test
    void updateTrainerWithNonExistentId() {
        Trainer trainer = new Trainer();
        trainer.setId(999L);
        when(trainerDAO.findById(trainer.getId())).thenReturn(Optional.empty());


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerService.update(trainer);
        });

        assertEquals("Trainer with ID: " + trainer.getId() + " doesn't exist", exception.getMessage());

    }

    @Test
    void updateTrainerWithNullId() {
        Trainer trainer = new Trainer();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerService.update(trainer);
        });

        assertEquals("ID cannot be null", exception.getMessage());

    }

    @Test
    void findByNullIdTest() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerService.findById(null);
        });

        assertEquals("ID cannot be null", exception.getMessage());
    }

    @Test
    void findByNonExistingIdTest() {
        //Arrange
        Long id = 999L;
        when(trainerDAO.findById(id)).thenReturn(Optional.empty());
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerService.findById(id);
        });
        //Assert
        assertEquals("Trainer with ID: " + id + " doesn't exist", exception.getMessage());
    }
}
