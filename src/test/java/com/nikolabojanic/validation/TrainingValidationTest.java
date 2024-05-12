package com.nikolabojanic.validation;

import com.nikolabojanic.model.TrainingEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TrainingValidationTest {
    @InjectMocks
    private TrainingValidation trainingValidation;

    @Test
    void validateCreateTrainingRequestWithNullTest() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainingValidation.validateCreateTrainingRequest(null);
        });

        assertEquals("Cannot create training with null", exception.getMessage());
    }

    @Test
    void validateCreateTrainingRequestWithIdTest() {
        TrainingEntity training = new TrainingEntity();
        training.setId(Long.parseLong(RandomStringUtils.randomNumeric(4, 6)));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainingValidation.validateCreateTrainingRequest(training);
        });

        assertEquals("Cannot create training with an id", exception.getMessage());
    }

    @Test
    void validateCreateTrainingRequestWithNullNameTest() {
        TrainingEntity training = new TrainingEntity();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainingValidation.validateCreateTrainingRequest(training);
        });

        assertEquals("Cannot create training without a name", exception.getMessage());
    }

    @Test
    void validateCreateTrainingRequestWithNullDateTest() {
        TrainingEntity training = new TrainingEntity();
        training.setName(RandomStringUtils.randomAlphabetic(4, 7));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainingValidation.validateCreateTrainingRequest(training);
        });

        assertEquals("Cannot create training without a date", exception.getMessage());
    }

    @Test
    void validateCreateTrainingRequestWithNullDurationTest() {
        TrainingEntity training = new TrainingEntity();
        training.setName(RandomStringUtils.randomAlphabetic(4, 7));
        training.setDate(LocalDate.now());
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainingValidation.validateCreateTrainingRequest(training);
        });

        assertEquals("Cannot create training without a duration", exception.getMessage());
    }

    @Test
    void validateDateBeginNotNull() {
        //given
        LocalDate end = LocalDate.now();

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainingValidation.validateDate(null, end);
        });

        //then
        assertEquals("Date values must not be null", exception.getMessage());
    }

    @Test
    void validateDateEndNotNull() {
        //given
        LocalDate begin = LocalDate.now();

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainingValidation.validateDate(begin, null);
        });

        //then
        assertEquals("Date values must not be null", exception.getMessage());
    }
}