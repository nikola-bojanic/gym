package com.nikolabojanic.validation;

import com.nikolabojanic.dto.TraineeRegistrationRequestDto;
import com.nikolabojanic.dto.TraineeTrainerUpdateRequestDto;
import com.nikolabojanic.dto.TraineeUpdateRequestDto;
import com.nikolabojanic.exception.ScValidationException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TraineeValidation {
    private static final int BAD_REQUEST = HttpStatus.BAD_REQUEST.value();

    /**
     * Validates the registration request for creating a trainee profile, ensuring that the provided data is valid.
     *
     * @param requestDto The TraineeRegistrationRequestDto containing information about the trainee.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validateRegisterRequest(TraineeRegistrationRequestDto requestDto) {
        if (requestDto == null) {
            log.error("Attempted to create a trainee profile with null. Status: {}", BAD_REQUEST);
            throw new ScValidationException("Cannot create trainee profile with null value");
        }
        List<String> errors = new ArrayList<>();
        if (requestDto.getFirstName() == null || requestDto.getFirstName().isBlank()
            || requestDto.getFirstName().length() < 2) {
            log.error("Attempted to create trainee profile with bad first name. Status: {}", BAD_REQUEST);
            errors.add("First name must be at least two characters long");
        }
        if (requestDto.getLastName() == null || requestDto.getLastName().isBlank()
            || requestDto.getLastName().length() < 2) {
            log.error("Attempted to create trainee profile with bad last name. Status: {}", BAD_REQUEST);
            errors.add("Last name must be at least two characters long");
        }
        if (!errors.isEmpty()) {
            throw new ScValidationException(errors.toString());
        }
    }

    /**
     * Validates the update request for modifying a trainee profile, ensuring that the provided data is valid.
     *
     * @param requestDto The TraineeUpdateRequestDto containing information about the updates to the trainee profile.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validateUpdateTraineeRequest(TraineeUpdateRequestDto requestDto) {
        if (requestDto == null) {
            log.error("Attempted to update a trainee with null. Status: {}", BAD_REQUEST);
            throw new ScValidationException("Cannot update a trainee with null value.");
        }
        List<String> errors = new ArrayList<>();
        if (requestDto.getUsername() == null || requestDto.getUsername().isBlank()
            || requestDto.getUsername().length() < 4) {
            log.error("Attempted to update trainee profile with bad username. Status: {}", BAD_REQUEST);
            errors.add("Username must be at least 4 characters long");
        }
        if (requestDto.getFirstName() == null || requestDto.getFirstName().isBlank()
            || requestDto.getFirstName().length() < 2) {
            log.error("Attempted to update trainee profile with bad first name. Status: {}", BAD_REQUEST);
            errors.add("First name must be at least two characters long");
        }
        if (requestDto.getLastName() == null || requestDto.getLastName().isBlank()
            || requestDto.getLastName().length() < 2) {
            log.error("Attempted to update trainee profile with bad last name. Status: {}", BAD_REQUEST);
            errors.add("Last name must be at least two characters long");
        }
        if (requestDto.getIsActive() == null) {
            log.error("Attempted to update trainee profile with null active status. Status: {}", BAD_REQUEST);
            errors.add("Active status must not be null");
        }
        if (!errors.isEmpty()) {
            throw new ScValidationException(errors.toString());
        }
    }

    /**
     * Validates the update request for modifying a trainee's trainers, ensuring that the provided data is valid.
     *
     * @param requestDto The list of TraineeTrainerUpdateRequestDto containing information about the trainers to update.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validateUpdateTrainersRequest(List<TraineeTrainerUpdateRequestDto> requestDto) {
        if (requestDto == null) {
            log.error("Attempted to update trainee's trainers with null request. Status: {}", BAD_REQUEST);
            throw new ScValidationException("Request must not be null");
        }
        List<String> errors = new ArrayList<>();
        requestDto.forEach(trainer -> {
            if (trainer.getUsername() == null || trainer.getUsername().isBlank()
                || trainer.getUsername().length() < 4) {
                log.error("Attempted to update trainee's trainers with bad trainer username. Status: {}", BAD_REQUEST);
                errors.add(
                    "Username " + trainer.getUsername() + " invalid. Username must be at least 4 characters long");
            }
        });
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
            log.error("Attempted to fetch trainee with bad path variable username. Status: {}", BAD_REQUEST);
            throw new ScValidationException("Username must be at least 4 characters long");
        }
    }

}
