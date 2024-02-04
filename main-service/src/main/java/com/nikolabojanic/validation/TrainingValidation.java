package com.nikolabojanic.validation;

import com.nikolabojanic.dto.TrainingRequestDto;
import com.nikolabojanic.exception.ScValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrainingValidation {

    Integer badRequest = HttpStatus.BAD_REQUEST.value();

    /**
     * Validates the provided username, ensuring that it is not null, blank, and has a minimum length of 4 characters.
     *
     * @param username The username to validate.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validateUsernameNotNull(String username) {
        if (username == null || username.isBlank() || username.length() < 4) {
            log.error("Attempted to fetch user with bad username. Status: {}", badRequest);
            throw new ScValidationException("Username must be at least 4 characters long");
        }
    }

    /**
     * Validates the provided training ID, ensuring that it is not null.
     *
     * @param id The ID to validate.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validateIdNotNull(Long id) {
        if (id == null) {
            log.error("Attempted to fetch training with null id. Status: {}", badRequest);
            throw new ScValidationException("Id must not be null");
        }
    }

    /**
     * Validates the request for creating a training, ensuring that the provided data is valid.
     *
     * @param training The TrainingRequestDto containing information about the training to be created.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validateCreateTrainingRequest(TrainingRequestDto training) {
        if (training == null) {
            log.error("Attempted to create training with null. Status: {}", badRequest);
            throw new ScValidationException("Cannot create training with null");
        } else if (training.getName() == null || training.getName().isBlank()) {
            log.error("Attempted to create training with missing name. Status: {}", badRequest);
            throw new ScValidationException("Cannot create training without a name");
        } else if (training.getDate() == null) {
            log.error("Attempted to create training with missing date. Status: {}", badRequest);
            throw new ScValidationException("Cannot create training without a date");
        } else if (training.getDuration() == null) {
            log.error("Attempted to create training with missing duration. Status: {}", badRequest);
            throw new ScValidationException("Cannot create training without a duration");
        } else if (training.getTraineeUsername() == null) {
            log.error("Attempted to create training with missing trainee username. Status: {}", badRequest);
            throw new ScValidationException("Cannot create training without a trainee username");
        } else if (training.getTrainerUsername() == null) {
            log.error("Attempted to create training with missing trainer username. Status: {}", badRequest);
            throw new ScValidationException("Cannot create training without a trainer username");
        }
    }
}
