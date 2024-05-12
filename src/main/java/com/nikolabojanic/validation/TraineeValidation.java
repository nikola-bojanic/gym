package com.nikolabojanic.validation;

import com.nikolabojanic.dto.TraineeRegistrationRequestDTO;
import com.nikolabojanic.dto.TraineeTrainerUpdateRequestDTO;
import com.nikolabojanic.dto.TraineeUpdateRequestDTO;
import com.nikolabojanic.exception.ScValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TraineeValidation {
    Integer badRequest = HttpStatus.BAD_REQUEST.value();
    String usernameMessage = "Username must be at least 4 characters long";

    public void validateRegisterRequest(TraineeRegistrationRequestDTO requestDTO) {
        if (requestDTO == null) {
            log.error("Attempted to create a trainee profile with null. Status: {}", badRequest);
            throw new ScValidationException("Cannot create trainee profile with null value");
        } else if (requestDTO.getFirstName() == null || requestDTO.getFirstName().isBlank() ||
                requestDTO.getFirstName().length() < 2) {
            log.error("Attempted to create trainee profile with bad first name. Status: {}", badRequest);
            throw new ScValidationException("First name must be at least two characters long");
        } else if (requestDTO.getLastName() == null || requestDTO.getLastName().isBlank() ||
                requestDTO.getLastName().length() < 2) {
            log.error("Attempted to create trainee profile with bad last name. Status: {}", badRequest);
            throw new ScValidationException("Last name must be at least two characters long");
        }
    }

    public void validateUpdateTraineeRequest(TraineeUpdateRequestDTO requestDTO) {
        if (requestDTO == null) {
            log.error("Attempted to update a trainee with null. Status: {}", badRequest);
            throw new ScValidationException("Cannot update a trainee with null value.");
        } else if (requestDTO.getUsername() == null || requestDTO.getUsername().isBlank() ||
                requestDTO.getUsername().length() < 4) {
            log.error("Attempted to update trainee profile with bad username. Status: {}", badRequest);
            throw new ScValidationException(usernameMessage);
        } else if (requestDTO.getFirstName() == null || requestDTO.getFirstName().isBlank() ||
                requestDTO.getFirstName().length() < 2) {
            log.error("Attempted to update trainee profile with bad first name. Status: {}", badRequest);
            throw new ScValidationException("First name must be at least two characters long");
        } else if (requestDTO.getLastName() == null || requestDTO.getLastName().isBlank() ||
                requestDTO.getLastName().length() < 2) {
            log.error("Attempted to update trainee profile with bad last name. Status: {}", badRequest);
            throw new ScValidationException("Last name must be at least two characters long");
        } else if (requestDTO.getIsActive() == null) {
            log.error("Attempted to update trainee profile with null active status. Status: {}", badRequest);
            throw new ScValidationException("Active status must not be null");
        }
    }

    public void validateUpdateTrainersRequest(List<TraineeTrainerUpdateRequestDTO> requestDTO) {
        if (requestDTO == null) {
            log.error("Attempted to update trainee's trainers with null request. Status: {}", badRequest);
            throw new ScValidationException("Request must not be null");
        }
        requestDTO.forEach(trainer -> {
            if (trainer.getUsername() == null || trainer.getUsername().isBlank()
                    || trainer.getUsername().length() < 4) {
                log.error("Attempted to update trainee's trainers with bad trainer username. Status: {}", badRequest);
                throw new ScValidationException(usernameMessage);
            }
        });
    }

    public void validateUsernameNotNull(String username) {
        if (username == null || username.isBlank() || username.length() < 4) {
            log.error("Attempted to fetch trainee with bad path variable username. Status: {}", badRequest);
            throw new ScValidationException(usernameMessage);
        }
    }

    public void validateActiveStatusRequest(String username, Boolean isActive) {
        validateUsernameNotNull(username);
        if (isActive == null) {
            log.error("Attempted to change trainee's active status with null value. Status: {}", badRequest);
            throw new ScValidationException("Active status must not be null");
        }
    }

}
