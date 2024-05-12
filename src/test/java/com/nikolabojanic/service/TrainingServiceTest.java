package com.nikolabojanic.service;

import com.nikolabojanic.dao.TrainingDAO;
import com.nikolabojanic.model.Training;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {

    @Mock
    private TrainingDAO trainingDAO;

    @InjectMocks
    private TrainingService trainingService;


    @Test
    void createTest() {
        //Arrange
        Training training = new Training();
        when(trainingDAO.save(training)).thenReturn(new Training());
        //Assert
        Training saved = trainingService.create(training);
        //Act
        assertNotNull(saved);
    }

    @Test
    void createWithTrainingIdTest() {
        Training training = new Training();
        training.setId(1L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainingService.create(training);
        });
        assertEquals("Cannot create a training with an ID", exception.getMessage());
    }

    @Test
    void findByNullIdTest() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainingService.findById(null);
        });

        assertEquals("ID cannot be null", exception.getMessage());
    }

    @Test
    void findByIdTest() {
        //Arrange
        Long id = 1L;
        when(trainingDAO.findById(id)).thenReturn(Optional.ofNullable(new Training()));
        //Assert
        Training fetchedTraining = trainingService.findById(id);
        //Act
        assertNotNull(fetchedTraining);
    }

    @Test
    void findByNonExistingId() {
        Long id = 999L;
        when(trainingDAO.findById(id)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainingService.findById(id);
        });

        assertEquals("Training with ID:" + id + " doesn't exist", exception.getMessage());
    }
}