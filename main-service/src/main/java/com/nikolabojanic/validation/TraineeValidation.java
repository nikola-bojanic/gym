package com.nikolabojanic.validation;

import com.nikolabojanic.dto.TraineeRegistrationRequestDto;
import com.nikolabojanic.dto.TraineeTrainerUpdateRequestDto;
import com.nikolabojanic.dto.TraineeUpdateRequestDto;
import com.nikolabojanic.exception.ScValidationException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TraineeValidation {
    Integer badRequest = HttpStatus.BAD_REQUEST.value();
    String usernameMessage = "Username must be at least 4 characters long";

    /**
     * Validates the registration request for creating a trainee profile, ensuring that the provided data is valid.
     *
     * @param requestDto The TraineeRegistrationRequestDto containing information about the trainee.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validateRegisterRequest(TraineeRegistrationRequestDto requestDto) {
        if (requestDto == null) {
            log.error("Attempted to create a trainee profile with null. Status: {}", badRequest);
            throw new ScValidationException("Cannot create trainee profile with null value");
        } else if (requestDto.getFirstName() == null || requestDto.getFirstName().isBlank()
            || requestDto.getFirstName().length() < 2) {
            log.error("Attempted to create trainee profile with bad first name. Status: {}", badRequest);
            throw new ScValidationException("First name must be at least two characters long");
        } else if (requestDto.getLastName() == null || requestDto.getLastName().isBlank()
            || requestDto.getLastName().length() < 2) {
            log.error("Attempted to create trainee profile with bad last name. Status: {}", badRequest);
            throw new ScValidationException("Last name must be at least two characters long");
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
            log.error("Attempted to update a trainee with null. Status: {}", badRequest);
            throw new ScValidationException("Cannot update a trainee with null value.");
        } else if (requestDto.getUsername() == null || requestDto.getUsername().isBlank()
            || requestDto.getUsername().length() < 4) {
            log.error("Attempted to update trainee profile with bad username. Status: {}", badRequest);
            throw new ScValidationException(usernameMessage);
        } else if (requestDto.getFirstName() == null || requestDto.getFirstName().isBlank()
            || requestDto.getFirstName().length() < 2) {
            log.error("Attempted to update trainee profile with bad first name. Status: {}", badRequest);
            throw new ScValidationException("First name must be at least two characters long");
        } else if (requestDto.getLastName() == null || requestDto.getLastName().isBlank()
            || requestDto.getLastName().length() < 2) {
            log.error("Attempted to update trainee profile with bad last name. Status: {}", badRequest);
            throw new ScValidationException("Last name must be at least two characters long");
        } else if (requestDto.getIsActive() == null) {
            log.error("Attempted to update trainee profile with null active status. Status: {}", badRequest);
            throw new ScValidationException("Active status must not be null");
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
            log.error("Attempted to update trainee's trainers with null request. Status: {}", badRequest);
            throw new ScValidationException("Request must not be null");
        }
        requestDto.forEach(trainer -> {
            if (trainer.getUsername() == null || trainer.getUsername().isBlank()
                || trainer.getUsername().length() < 4) {
                log.error("Attempted to update trainee's trainers with bad trainer username. Status: {}", badRequest);
                throw new ScValidationException(usernameMessage);
            }
        });
    }

    /**
     * Validates the provided username, ensuring that it is not null, blank, and has a minimum length of 4 characters.
     *
     * @param username The username to validate.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validateUsernameNotNull(String username) {
        if (username == null || username.isBlank() || username.length() < 4) {
            log.error("Attempted to fetch trainee with bad path variable username. Status: {}", badRequest);
            throw new ScValidationException(usernameMessage);
        }
    }

    /**
     * Validates the request for changing a trainee's active status, ensuring that the provided data is valid.
     *
     * @param username The username of the trainee.
     * @param isActive The new active status for the trainee.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validateActiveStatusRequest(String username, Boolean isActive) {
        validateUsernameNotNull(username);
        if (isActive == null) {
            log.error("Attempted to change trainee's active status with null value. Status: {}", badRequest);
            throw new ScValidationException("Active status must not be null");
        }
    }

}
