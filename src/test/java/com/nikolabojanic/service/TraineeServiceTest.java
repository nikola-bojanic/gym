package com.nikolabojanic.service;

import com.nikolabojanic.dao.TraineeDAO;
import com.nikolabojanic.model.Trainee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {
    @Mock
    private TraineeDAO traineeDAO;

    @InjectMocks
    private TraineeService traineeService;


    @Test
    void createTest() {
        //Arrange
        Trainee trainee = new Trainee();
        when(traineeDAO.save(trainee)).thenReturn(new Trainee());
        //Act
        Trainee createdTrainee = traineeService.create(trainee);
        //Assert
        assertNotNull(createdTrainee);

    }

    @Test
    void updateTest() {
        //Arrange
        Trainee trainee = new Trainee();
        trainee.setId(1L);
        when(traineeDAO.findById(trainee.getId())).thenReturn(Optional.of(new Trainee()));
        when(traineeDAO.save(trainee)).thenReturn(new Trainee());
        //Act
        Trainee updatedTrainee = traineeService.update(trainee);
        //Assert
        assertNotNull(updatedTrainee);
    }

    @Test
    void deleteTest() {
        //Arrange
        Long id = 1L;
        when(traineeDAO.findById(id)).thenReturn(Optional.of(new Trainee()));
        //Act
        traineeService.delete(id);
    }

    @Test
    void deleteWithNonExistentIdTest() {
        //Arrange
        Long id = 1L;
        when(traineeDAO.findById(id)).thenReturn(Optional.empty());
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            traineeService.delete(id);
        });
        //Assert
        assertEquals("Trainee with ID " + id + " doesn't exist", exception.getMessage());

    }

    @Test
    void deleteWithNullIdTest() {
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            traineeService.delete(null);
        });
        //Assert
        assertEquals("ID cannot be null", exception.getMessage());

    }

    @Test
    void findByIdTest() {
        //Arrange
        Long id = 1L;
        when(traineeDAO.findById(id)).thenReturn(Optional.of(new Trainee()));
        //Act
        Trainee fetchedTrainee = traineeService.findById(id);
        //Assert
        assertNotNull(fetchedTrainee);
    }

    @Test
    void findByNonExistingIdTest() {
        //Arrange
        Long id = 999L;
        when(traineeDAO.findById(id)).thenReturn(Optional.empty());
        //Act
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            traineeService.findById(id);
        });
        //Assert
        assertEquals("Trainee with ID " + id + " doesn't exist", exception.getMessage());
    }

    @Test
    void createTraineeWithIdTest() {
        Trainee trainee = new Trainee();
        trainee.setId(1L);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            this.traineeService.create(trainee);
        });

        assertEquals("Cannot create a training with an ID", exception.getMessage());
    }

    @Test
    void updateTraineeWithNonExistingIdTest() {
        Trainee trainee = new Trainee();
        trainee.setId(999L);
        when(traineeDAO.findById(trainee.getId())).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            traineeService.update(trainee);
        });

        assertEquals("Trainee with ID " + trainee.getId() + " doesn't exist", exception.getMessage());
    }

    @Test
    void updateTraineeWithNullIdTest() {
        Trainee trainee = new Trainee();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            traineeService.update(trainee);
        });

        assertEquals("ID cannot be null", exception.getMessage());
    }


    @Test
    void findByNullId() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> traineeService.findById(null));

        assertEquals("ID cannot be null", exception.getMessage());
    }

}