package com.nikolabojanic.validation;

import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.UserEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TraineeValidationTest {
    @InjectMocks
    private TraineeValidation traineeValidation;

    @Test
    void validateCreateTraineeRequestWithNullTest() {

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            traineeValidation.validateCreateTraineeRequest(null);
        });

        assertEquals("Cannot create trainee profile with null value", exception.getMessage());
    }

    @Test
    void validateCreateTraineeRequestWithIdTest() {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setId(Long.parseLong(RandomStringUtils.randomNumeric(4, 5)));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            traineeValidation.validateCreateTraineeRequest(trainee);
        });

        assertEquals("Cannot create trainee profile with an ID", exception.getMessage());
    }

    @Test
    void validateCreateTraineeRequestWithNullUserTest() {
        TraineeEntity trainee = new TraineeEntity();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            traineeValidation.validateCreateTraineeRequest(trainee);
        });

        assertEquals("Cannot create trainee profile with null user", exception.getMessage());
    }

    @Test
    void validateCreateTraineeRequestWithUserIdTest() {
        TraineeEntity trainee = new TraineeEntity();
        UserEntity user = new UserEntity();
        user.setId(Long.parseLong(RandomStringUtils.randomNumeric(4, 7)));
        trainee.setUser(user);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            traineeValidation.validateCreateTraineeRequest(trainee);
        });

        assertEquals("Cannot create trainee profile with an user ID", exception.getMessage());
    }

    @Test
    void validateUpdateTraineeRequestWithNullTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            traineeValidation.validateUpdateTraineeRequest(null);
        });

        assertEquals("Cannot update a trainee with null value", exception.getMessage());
    }

    @Test
    void validateUpdateTraineeRequestWithNullIdTest() {
        TraineeEntity trainee = new TraineeEntity();
        UserEntity user = new UserEntity();
        trainee.setUser(user);


        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            traineeValidation.validateUpdateTraineeRequest(trainee);
        });

        assertEquals("Cannot update trainee with null id", exception.getMessage());
    }

    @Test
    void validateUpdateTraineeRequestWithNullUserTest() {
        TraineeEntity trainee = new TraineeEntity();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            traineeValidation.validateUpdateTraineeRequest(trainee);
        });

        assertEquals("Cannot update trainee profile with null user", exception.getMessage());
    }

    @Test
    void validateUpdateTraineeRequestWithNullUserIdTest() {
        //given
        TraineeEntity trainee = new TraineeEntity();
        trainee.setId(Long.parseLong(RandomStringUtils.randomNumeric(4, 5)));
        UserEntity user = new UserEntity();
        trainee.setUser(user);
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            traineeValidation.validateUpdateTraineeRequest(trainee);
        });
        //then
        assertEquals("Cannot update user without an ID", exception.getMessage());
    }

    @Test
    void validateIdNotNullTest() {
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            traineeValidation.validateIdNotNull(null);
        });
        //then
        assertEquals("ID cannot be null", exception.getMessage());
    }

    @Test
    void validateTraineeNotNullTest() {
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            traineeValidation.validateTraineeNotNull(null);
        });
        //then
        assertEquals("Trainee must not be null", exception.getMessage());
    }
}