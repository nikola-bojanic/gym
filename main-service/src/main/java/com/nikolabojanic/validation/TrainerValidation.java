package com.nikolabojanic.validation;

import com.nikolabojanic.dto.TrainerRegistrationRequestDto;
import com.nikolabojanic.dto.TrainerUpdateRequestDto;
import com.nikolabojanic.exception.ScValidationException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainerValidation {
    private static final int BAD_REQUEST = HttpStatus.BAD_REQUEST.value();

    /**
     * Validates the registration request for creating a trainer profile, ensuring that the provided data is valid.
     *
     * @param requestDto The TrainerRegistrationRequestDto containing information about the trainer.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validateRegisterRequest(TrainerRegistrationRequestDto requestDto) {
        if (requestDto == null) {
            log.error("Attempted to create a trainer profile with null. Status: {}", BAD_REQUEST);
            throw new ScValidationException("Cannot create trainer profile with null value");
        }
        List<String> errors = new ArrayList<>();
        if (requestDto.getFirstName() == null || requestDto.getFirstName().isBlank()
            || requestDto.getFirstName().length() < 2) {
            log.error("Attempted to create trainer profile with bad first name. Status: {}", BAD_REQUEST);
            errors.add("First name must be at least two characters long");
        }
        if (requestDto.getLastName() == null || requestDto.getLastName().isBlank()
            || requestDto.getLastName().length() < 2) {
            log.error("Attempted to create trainer profile with bad last name. Status: {}", BAD_REQUEST);
            errors.add("Last name must be at least two characters long");
        }
        if (requestDto.getSpecializationId() == null) {
            log.error("Attempted to create trainer profile with no specialization id. Status: {}", BAD_REQUEST);
            errors.add("Cannot create trainer profile with null specialization id");
        }
        if (!errors.isEmpty()) {
            throw new ScValidationException(errors.toString());
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
            log.error("Attempted to update trainer profile with null. Status: {}", BAD_REQUEST);
            throw new ScValidationException("Cannot update trainer profile with null request");
        }
        List<String> errors = new ArrayList<>();
        if (requestDto.getUsername() == null || requestDto.getUsername().isBlank()
            || requestDto.getUsername().length() < 4) {
            log.error("Attempted to update trainer profile with bad username. Status: {}", BAD_REQUEST);
            errors.add("Username must be at least 4 characters long");
        }
        if (requestDto.getFirstName() == null || requestDto.getFirstName().isBlank()
            || requestDto.getFirstName().length() < 2) {
            log.error("Attempted to update trainer profile with bad first name. Status: {}", BAD_REQUEST);
            errors.add("First name must be at least two characters long");
        }
        if (requestDto.getLastName() == null || requestDto.getLastName().isBlank()
            || requestDto.getLastName().length() < 2) {
            log.error("Attempted to update trainer profile with bad last name. Status: {}", BAD_REQUEST);
            errors.add("Last name must be at least two characters long");
        }
        if (requestDto.getIsActive() == null) {
            log.error("Attempted to update trainer profile with null active status. Status: {}", BAD_REQUEST);
            errors.add("Active status must not be null");
        }
        if (!errors.isEmpty()) {
            throw new ScValidationException(errors.toString());
        }
    }

    /**
     * Validates the provided username, ensuring that it is not null, blank, and has a minimum length of 4 characters.
     *
     * @param username The username to validate.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validateUsername(String username) {
        if (username == null || username.isBlank() || username.length() < 4) {
            log.error("Attempted to fetch trainee with bad username. Status: {}", BAD_REQUEST);
            throw new ScValidationException("Username must be at least 4 characters long");
        }
    }

}
