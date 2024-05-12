package com.nikolabojanic.validation;

import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.UserEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TrainerValidationTest {
    @InjectMocks
    private TrainerValidation trainerValidation;

    @Test
    void validateCreateTrainerRequestTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerValidation.validateCreateTrainerRequest(null);
        });

        assertEquals("Cannot create trainer profile with null value", exception.getMessage());
    }

    @Test
    void validateCreateTrainerRequestWithIdTest() {
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(new UserEntity());
        trainer.setId(Long.parseLong(RandomStringUtils.randomNumeric(4, 7)));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerValidation.validateCreateTrainerRequest(trainer);
        });

        assertEquals("Cannot create trainer profile with an ID", exception.getMessage());
    }

    @Test
    void validateCreateTrainerRequestWithNoUserTest() {
        TrainerEntity trainer = new TrainerEntity();
        trainer.setId(Long.parseLong(RandomStringUtils.randomNumeric(4, 7)));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerValidation.validateCreateTrainerRequest(trainer);
        });

        assertEquals("Cannot create trainer profile with null user", exception.getMessage());
    }

    @Test
    void validateCreateTrainerRequestWithUserIdTest() {
        TrainerEntity trainer = new TrainerEntity();
        UserEntity user = new UserEntity();
        user.setId(Long.parseLong(RandomStringUtils.randomNumeric(4, 7)));
        trainer.setUser(user);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerValidation.validateCreateTrainerRequest(trainer);
        });

        assertEquals("Cannot create trainer profile with an user ID", exception.getMessage());
    }

    @Test
    void validateUpdateTrainerRequestTest() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerValidation.validateUpdateTrainerRequest(null);
        });

        assertEquals("Cannot update trainer profile with null value", exception.getMessage());
    }

    @Test
    void validateUpdateTrainerRequestWithoutIdTest() {
        TrainerEntity trainer = new TrainerEntity();
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerValidation.validateUpdateTrainerRequest(trainer);
        });

        assertEquals("Cannot update trainer profile with null id", exception.getMessage());
    }

    @Test
    void validateUpdateTrainerRequestWithoutUserTest() {
        TrainerEntity trainer = new TrainerEntity();

        trainer.setId(Long.parseLong(RandomStringUtils.randomNumeric(4, 7)));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerValidation.validateUpdateTrainerRequest(trainer);
        });

        assertEquals("Cannot update trainer profile with null user", exception.getMessage());
    }

    @Test
    void validateUpdateTrainerRequestWithoutUserIdTest() {
        TrainerEntity trainer = new TrainerEntity();
        UserEntity user = new UserEntity();
        trainer.setId(Long.parseLong(RandomStringUtils.randomNumeric(4, 7)));
        trainer.setUser(user);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerValidation.validateUpdateTrainerRequest(trainer);
        });

        assertEquals("Cannot update trainer profile without user id", exception.getMessage());
    }

    @Test
    void validateIdNotNullTest() {
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerValidation.validateIdNotNull(null);
        });
        //then
        assertEquals("ID cannot be null", exception.getMessage());
    }

    @Test
    void validateTraineeIdNotNullTest() {
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerValidation.validateTraineeId(null);
        });
        //then
        assertEquals("Trainee id cannot be null", exception.getMessage());
    }
}