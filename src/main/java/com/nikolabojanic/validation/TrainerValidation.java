package com.nikolabojanic.validation;

import com.nikolabojanic.dto.TrainerRegistrationRequestDTO;
import com.nikolabojanic.dto.TrainerUpdateRequestDTO;
import com.nikolabojanic.exception.SCValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainerValidation {
    Integer badRequest = HttpStatus.BAD_REQUEST.value();

    public void validateRegisterRequest(TrainerRegistrationRequestDTO requestDTO) {
        if (requestDTO == null) {
            log.error("Attempted to create a trainer profile with null. Status: {}", badRequest);
            throw new SCValidationException("Cannot create trainer profile with null value");
        } else if (requestDTO.getFirstName() == null || requestDTO.getFirstName().isBlank()
                || requestDTO.getFirstName().length() < 2) {
            log.error("Attempted to create trainer profile with bad first name. Status: {}", badRequest);
            throw new SCValidationException("First name must be at least two characters long");
        } else if (requestDTO.getLastName() == null || requestDTO.getLastName().isBlank()
                || requestDTO.getLastName().length() < 2) {
            log.error("Attempted to create trainer profile with bad last name. Status: {}", badRequest);
            throw new SCValidationException("Last name must be at least two characters long");
        } else if (requestDTO.getSpecializationId() == null) {
            log.error("Attempted to create trainer profile with no specialization id. Status: {}", badRequest);
            throw new SCValidationException("Cannot create trainer profile with null specialization id");
        }
    }

    public void validateUpdateTrainerRequest(TrainerUpdateRequestDTO requestDTO) {
        if (requestDTO == null) {
            log.error("Attempted to update trainer profile with null. Status: {}", badRequest);
            throw new SCValidationException("Cannot update trainer profile with null request");
        } else if (requestDTO.getUsername() == null || requestDTO.getUsername().isBlank()
                || requestDTO.getUsername().length() < 4) {
            log.error("Attempted to update trainer profile with bad username. Status: {}", badRequest);
            throw new SCValidationException("Username must be at least 4 characters long");
        } else if (requestDTO.getFirstName() == null || requestDTO.getFirstName().isBlank()
                || requestDTO.getFirstName().length() < 2) {
            log.error("Attempted to update trainer profile with bad first name. Status: {}", badRequest);
            throw new SCValidationException("First name must be at least two characters long");
        } else if (requestDTO.getLastName() == null || requestDTO.getLastName().isBlank()
                || requestDTO.getLastName().length() < 2) {
            log.error("Attempted to update trainer profile with bad last name. Status: {}", badRequest);
            throw new SCValidationException("Last name must be at least two characters long");
        } else if (requestDTO.getIsActive() == null) {
            log.error("Attempted to update trainer profile with null active status. Status: {}", badRequest);
            throw new SCValidationException("Active status must not be null");
        }
    }

    public void validateUsernameNotNull(String username) {
        if (username == null || username.isBlank() || username.length() < 4) {
            log.error("Attempted to fetch trainee with bad username. Status: {}", badRequest);
            throw new SCValidationException("Username must be at least 4 characters long");
        }
    }

    public void validateActiveStatusRequest(String username, Boolean isActive) {
        validateUsernameNotNull(username);
        if (isActive == null) {
            log.error("Attempted to change trainer's active status with null value. Status: {}", badRequest);
            throw new SCValidationException("Active status must not be null");
        }
    }
}
