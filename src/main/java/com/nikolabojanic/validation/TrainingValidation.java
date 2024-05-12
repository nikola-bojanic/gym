package com.nikolabojanic.validation;

import com.nikolabojanic.dto.TrainingRequestDTO;
import com.nikolabojanic.exception.SCValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrainingValidation {

    Integer badRequest = HttpStatus.BAD_REQUEST.value();

    public void validateUsernameNotNull(String username) {
        if (username == null || username.isBlank() || username.length() < 4) {
            log.error("Attempted to fetch user with bad username. Status: {}", badRequest);
            throw new SCValidationException("Username must be at least 4 characters long");
        }
    }

    public void validateCreateTrainingRequest(TrainingRequestDTO training) {
        if (training == null) {
            log.error("Attempted to create training with null. Status: {}", badRequest);
            throw new SCValidationException("Cannot create training with null");
        } else if (training.getName() == null || training.getName().isBlank()) {
            log.error("Attempted to create training with missing name. Status: {}", badRequest);
            throw new SCValidationException("Cannot create training without a name");
        } else if (training.getDate() == null) {
            log.error("Attempted to create training with missing date. Status: {}", badRequest);
            throw new SCValidationException("Cannot create training without a date");
        } else if (training.getDuration() == null) {
            log.error("Attempted to create training with missing duration. Status: {}", badRequest);
            throw new SCValidationException("Cannot create training without a duration");
        } else if (training.getTraineeUsername() == null) {
            log.error("Attempted to create training with missing trainee username. Status: {}", badRequest);
            throw new SCValidationException("Cannot create training without a trainee username");
        } else if (training.getTrainerUsername() == null) {
            log.error("Attempted to create training with missing trainer username. Status: {}", badRequest);
            throw new SCValidationException("Cannot create training without a trainer username");
        }
    }
}
