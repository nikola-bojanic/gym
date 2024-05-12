package com.nikolabojanic.validation;

import com.nikolabojanic.model.TraineeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TraineeValidation {
    public void validateCreateTraineeRequest(TraineeEntity trainee) {
        if (trainee == null) {
            log.error("Attempted to create a trainee profile with null");
            throw new IllegalArgumentException("Cannot create trainee profile with null value");
        } else if (trainee.getId() != null) {
            log.error("Attempted to create trainee profile with an existing ID");
            throw new IllegalArgumentException("Cannot create trainee profile with an ID");
        } else if (trainee.getUser() == null) {
            log.error("Attempted to create trainee profile with null user");
            throw new IllegalArgumentException("Cannot create trainee profile with null user");
        } else if (trainee.getUser().getId() != null) {
            log.error("Attempted to create trainee profile with an user ID");
            throw new IllegalArgumentException("Cannot create trainee profile with an user ID");
        }
    }

    public void validateUpdateTraineeRequest(TraineeEntity trainee) {
        if (trainee == null) {
            log.error("Attempted to update a trainee with null");
            throw new IllegalArgumentException("Cannot update a trainee with null value");
        } else if (trainee.getUser() == null) {
            log.error("Attempted to update trainee profile with null user");
            throw new IllegalArgumentException("Cannot update trainee profile with null user");
        } else if (trainee.getId() == null) {
            log.error("Attempted to update trainee with null id");
            throw new IllegalArgumentException("Cannot update trainee with null id");
        } else if (trainee.getUser().getId() == null) {
            log.error("Attempted to update user with null ID");
            throw new IllegalArgumentException("Cannot update user without an ID");
        }
    }

    public void validateIdNotNull(Long id) {
        if (id == null) {
            log.error("Attempted to fetch trainee with null id");
            throw new IllegalArgumentException("ID cannot be null");
        }
    }

    public void validateTraineeNotNull(TraineeEntity trainee) {
        if (trainee == null) {
            log.error("Attempted to change null trainee's trainer list");
            throw new IllegalArgumentException("Trainee must not be null");
        }
    }

    public void validateTraineePasswordChange(TraineeEntity trainee, String password) {
        if (trainee == null) {
            log.error("Attempted to change null trainee's password");
            throw new IllegalArgumentException("Trainee cannot be null");
        } else if (trainee.getId() == null) {
            log.error("Attempted to change null trainee's password without a trainee id");
            throw new IllegalArgumentException("Trainee id cannot be null");
        } else if (trainee.getUser() == null) {
            log.error("Attempted to change trainee's password with null user");
            throw new IllegalArgumentException("User cannot be null");
        } else if (trainee.getUser().getPassword().equals(password)) {
            log.error("Attempted to change trainee's password with the same password");
            throw new IllegalArgumentException("New password is the same as the old one");
        }
    }


}
