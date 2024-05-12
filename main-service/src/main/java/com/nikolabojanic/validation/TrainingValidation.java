package com.nikolabojanic.validation;

import com.nikolabojanic.dto.TrainingRequestDto;
import com.nikolabojanic.exception.ScValidationException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrainingValidation {

    private static final int BAD_REQUEST = HttpStatus.BAD_REQUEST.value();

    /**
     * Validates the provided username, ensuring that it is not null, blank, and has a minimum length of 4 characters.
     *
     * @param username The username to validate.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validateUsername(String username) {
        if (username == null || username.isBlank() || username.length() < 4) {
            log.error("Attempted to fetch user with bad username. Status: {}", BAD_REQUEST);
            throw new ScValidationException("Username must be at least 4 characters long");
        }
    }

    /**
     * Validates the provided training ID, ensuring that it is not null.
     *
     * @param id The ID to validate.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validateId(Long id) {
        if (id == null) {
            log.error("Attempted to fetch training with null id. Status: {}", BAD_REQUEST);
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
            log.error("Attempted to create training with null. Status: {}", BAD_REQUEST);
            throw new ScValidationException("Cannot create training with null");
        }
        List<String> errors = new ArrayList<>();
        if (training.getName() == null || training.getName().isBlank()) {
            log.error("Attempted to create training with missing name. Status: {}", BAD_REQUEST);
            errors.add("Cannot create training without a name");
        }
        if (training.getDate() == null) {
            log.error("Attempted to create training with missing date. Status: {}", BAD_REQUEST);
            errors.add("Cannot create training without a date");
        }
        if (training.getDuration() == null) {
            log.error("Attempted to create training with missing duration. Status: {}", BAD_REQUEST);
            errors.add("Cannot create training without a duration");
        }
        if (training.getTraineeUsername() == null) {
            log.error("Attempted to create training with missing trainee username. Status: {}", BAD_REQUEST);
            errors.add("Cannot create training without a trainee username");
        }
        if (training.getTrainerUsername() == null) {
            log.error("Attempted to create training with missing trainer username. Status: {}", BAD_REQUEST);
            errors.add("Cannot create training without a trainer username");
        }
        if (!errors.isEmpty()) {
            throw new ScValidationException(errors.toString());
        }
    }
}
