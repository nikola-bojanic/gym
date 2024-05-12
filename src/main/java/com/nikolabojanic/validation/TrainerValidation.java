package com.nikolabojanic.validation;

import com.nikolabojanic.model.TrainerEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainerValidation {

    public void validateCreateTrainerRequest(TrainerEntity trainer) {
        if (trainer == null) {
            log.error("Attempted to create a trainer profile with null");
            throw new IllegalArgumentException("Cannot create trainer profile with null value");
        } else if (trainer.getUser() == null) {
            log.error("Attempted to create trainer profile with null user");
            throw new IllegalArgumentException("Cannot create trainer profile with null user");
        } else if (trainer.getId() != null) {
            log.error("Attempted to create trainer profile with an ID");
            throw new IllegalArgumentException("Cannot create trainer profile with an ID");
        } else if (trainer.getUser().getId() != null) {
            log.error("Attempted to create trainer profile with an user ID");
            throw new IllegalArgumentException("Cannot create trainer profile with an user ID");
        }
    }

    public void validateUpdateTrainerRequest(TrainerEntity trainer) {
        if (trainer == null) {
            log.error("Attempted to update trainer profile with null");
            throw new IllegalArgumentException("Cannot update trainer profile with null value");
        } else if (trainer.getId() == null) {
            log.error("Attempted to update trainer profile with null id");
            throw new IllegalArgumentException("Cannot update trainer profile with null id");
        } else if (trainer.getUser() == null) {
            log.error("Attempted to update trainer profile with null user ");
            throw new IllegalArgumentException("Cannot update trainer profile with null user");
        } else if (trainer.getUser().getId() == null) {
            log.error("Attempted to update trainer profile with null user ID");
            throw new IllegalArgumentException("Cannot update trainer profile without user id");
        }
    }

    public void validateIdNotNull(Long id) {
        if (id == null) {
            log.error("Attempted to fetch trainer with null ID");
            throw new IllegalArgumentException("ID cannot be null");
        }
    }

    public void validateTrainerPasswordChange(TrainerEntity trainer, String password) {
        if (trainer == null) {
            log.error("Attempted to change null trainer's password");
            throw new IllegalArgumentException("Trainer cannot be null");
        } else if (trainer.getUser() == null) {
            log.error("Attempted to change trainer's password with null user");
            throw new IllegalArgumentException("User cannot be null");
        } else if (trainer.getUser().getPassword().equals(password)) {
            log.error("Attempted to change trainer's password with the same password");
            throw new IllegalArgumentException("New password is the same as the old one");
        }
    }

    public void validateTraineeId(Long id) {
        if (id == null) {
            log.error("Attempted to fetch trainers with null trainee id");
            throw new IllegalArgumentException("Trainee id cannot be null");
        }
    }
}
