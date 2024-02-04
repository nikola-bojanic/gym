package com.nikolabojanic.validation;

import com.nikolabojanic.dto.TrainerRegistrationRequestDto;
import com.nikolabojanic.dto.TrainerUpdateRequestDto;
import com.nikolabojanic.exception.ScValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainerValidation {
    Integer badRequest = HttpStatus.BAD_REQUEST.value();

    /**
     * Validates the registration request for creating a trainer profile, ensuring that the provided data is valid.
     *
     * @param requestDto The TrainerRegistrationRequestDto containing information about the trainer.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validateRegisterRequest(TrainerRegistrationRequestDto requestDto) {
        if (requestDto == null) {
            log.error("Attempted to create a trainer profile with null. Status: {}", badRequest);
            throw new ScValidationException("Cannot create trainer profile with null value");
        } else if (requestDto.getFirstName() == null || requestDto.getFirstName().isBlank()
            || requestDto.getFirstName().length() < 2) {
            log.error("Attempted to create trainer profile with bad first name. Status: {}", badRequest);
            throw new ScValidationException("First name must be at least two characters long");
        } else if (requestDto.getLastName() == null || requestDto.getLastName().isBlank()
            || requestDto.getLastName().length() < 2) {
            log.error("Attempted to create trainer profile with bad last name. Status: {}", badRequest);
            throw new ScValidationException("Last name must be at least two characters long");
        } else if (requestDto.getSpecializationId() == null) {
            log.error("Attempted to create trainer profile with no specialization id. Status: {}", badRequest);
            throw new ScValidationException("Cannot create trainer profile with null specialization id");
        }
    }

    /**
     * Validates the update request for modifying a trainer profile, ensuring that the provided data is valid.
     *
     * @param requestDto The TrainerUpdateRequestDto containing information about the updates to the trainer profile.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validateUpdateTrainerRequest(TrainerUpdateRequestDto requestDto) {
        if (requestDto == null) {
            log.error("Attempted to update trainer profile with null. Status: {}", badRequest);
            throw new ScValidationException("Cannot update trainer profile with null request");
        } else if (requestDto.getUsername() == null || requestDto.getUsername().isBlank()
            || requestDto.getUsername().length() < 4) {
            log.error("Attempted to update trainer profile with bad username. Status: {}", badRequest);
            throw new ScValidationException("Username must be at least 4 characters long");
        } else if (requestDto.getFirstName() == null || requestDto.getFirstName().isBlank()
            || requestDto.getFirstName().length() < 2) {
            log.error("Attempted to update trainer profile with bad first name. Status: {}", badRequest);
            throw new ScValidationException("First name must be at least two characters long");
        } else if (requestDto.getLastName() == null || requestDto.getLastName().isBlank()
            || requestDto.getLastName().length() < 2) {
            log.error("Attempted to update trainer profile with bad last name. Status: {}", badRequest);
            throw new ScValidationException("Last name must be at least two characters long");
        } else if (requestDto.getIsActive() == null) {
            log.error("Attempted to update trainer profile with null active status. Status: {}", badRequest);
            throw new ScValidationException("Active status must not be null");
        }
    }

    /**
     * Validates the provided username, ensuring that it is not null, blank, and has a minimum length of 4 characters.
     *
     * @param username The username to validate.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validateUsernameNotNull(String username) {
        if (username == null || username.isBlank() || username.length() < 4) {
            log.error("Attempted to fetch trainee with bad username. Status: {}", badRequest);
            throw new ScValidationException("Username must be at least 4 characters long");
        }
    }

    /**
     * Validates the request for changing a trainer's active status, ensuring that the provided data is valid.
     *
     * @param username The username of the trainer.
     * @param isActive The new active status for the trainer.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validateActiveStatusRequest(String username, Boolean isActive) {
        validateUsernameNotNull(username);
        if (isActive == null) {
            log.error("Attempted to change trainer's active status with null value. Status: {}", badRequest);
            throw new ScValidationException("Active status must not be null");
        }
    }
}
