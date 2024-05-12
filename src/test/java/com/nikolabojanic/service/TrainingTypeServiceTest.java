package com.nikolabojanic.service;

import com.nikolabojanic.dao.TrainingTypeDAO;
import com.nikolabojanic.model.TrainingTypeEntity;
import com.nikolabojanic.validation.TrainingTypeValidation;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TrainingTypeServiceTest {
    @Mock
    private TrainingTypeDAO trainingTypeDAO;
    @Mock
    private TrainingTypeValidation trainingTypeValidation;
    @InjectMocks
    private TrainingTypeService trainingTypeService;

    @Test
    void findByIdTest() {
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(trainingTypeDAO.findById(id)).thenReturn(Optional.of(new TrainingTypeEntity()));

        TrainingTypeEntity fetchedType = trainingTypeService.findById(id);

        assertNotNull(fetchedType);
        verify(trainingTypeValidation).validateIdNotNull(id);
    }

//    @Test
//    void findByNullId() {
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> trainingTypeService.findById(null));
//
//        assertEquals("ID cannot be null", exception.getMessage());
//    }

    @Test
    void findByNonExistingIdTest() {
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(trainingTypeDAO.findById(id)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            trainingTypeService.findById(id);
        });

        assertEquals("Training type with ID " + id + " doesn't exist", exception.getMessage());
        verify(trainingTypeValidation).validateIdNotNull(id);
    }
}
