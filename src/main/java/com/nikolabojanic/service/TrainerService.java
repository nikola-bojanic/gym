package com.nikolabojanic.service;


import com.nikolabojanic.dao.TrainerDAO;
import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.UserEntity;
import com.nikolabojanic.validation.TrainerValidation;
import com.nikolabojanic.validation.UserValidation;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class TrainerService {
    private final TrainerDAO trainerDAO;
    private final UserService userService;
    private final TrainingTypeService trainingTypeService;
    private final UserValidation userValidation;
    private final TrainerValidation trainerValidation;

    public TrainerEntity createTrainerProfile(TrainerEntity trainer) {
        trainerValidation.validateCreateTrainerRequest(trainer);
        UserEntity user = userService.generateUsernameAndPassword(trainer.getUser());
        trainer.setUser(user);
        if (trainer.getSpecialization() != null && trainer.getSpecialization().getId() != null) {
            trainer.setSpecialization(trainingTypeService.findById(trainer.getSpecialization().getId()));
        }
        log.info("Created trainer profile");
        return trainerDAO.save(trainer);
    }

    public TrainerEntity updateTrainerProfile(AuthDTO authDTO, TrainerEntity trainer) {
        userService.authentication(authDTO);
        trainerValidation.validateUpdateTrainerRequest(trainer);
        TrainerEntity managedTrainer = findById(trainer.getId());
        UserEntity inputUser = trainer.getUser();
        userValidation.validateUserFields(inputUser);
        UserEntity user = userService.findByUsername(inputUser.getUsername());
        userValidation.validateUserPermissionToEdit(user.getUsername(), inputUser.getUsername());
        user.setIsActive(inputUser.getIsActive());
        user.setFirstName(inputUser.getFirstName());
        user.setLastName(inputUser.getLastName());
        managedTrainer.setUser(user);
        if (trainer.getSpecialization() != null && trainer.getSpecialization().getId() != null) {
            trainer.setSpecialization(trainingTypeService.findById(trainer.getSpecialization().getId()));
        }
        log.info("Updating trainer profile");
        return trainerDAO.save(managedTrainer);

    }

    public TrainerEntity findById(Long id) {
        trainerValidation.validateIdNotNull(id);
        return trainerDAO.findById(id)
                .orElseThrow(() -> {
                    log.error("Attempted to fetch trainer with non-existent ID {}", id);
                    return new NoSuchElementException("Trainer with ID: " + id + " doesn't exist");
                });
    }

    public TrainerEntity findByUsername(AuthDTO authDTO, String username) {
        userService.authentication(authDTO);
        UserEntity user = userService.findByUsername(username);
        Long userId = user.getId();
        return trainerDAO.findByUserId(userId).orElseThrow(() -> {
            log.error("Attempted to fetch trainer with non-existent associated user ID {}", userId);
            return new NoSuchElementException("Trainer with user ID " + userId + " doesn't exist");
        });
    }

    public void changeTrainerPassword(AuthDTO authDTO, TrainerEntity trainer, String password) {
        userService.authentication(authDTO);
        trainerValidation.validateTrainerPasswordChange(trainer, password);
        userValidation.validateUsernameAndPassword(trainer.getUser().getUsername(), password);
        UserEntity user = userService.findByUsername(trainer.getUser().getUsername());
        TrainerEntity check = findById(trainer.getId());
        userValidation.validateUserPermissionToEdit(trainer.getUser().getUsername(), check.getUser().getUsername());
        user.setPassword(password);
        check.setUser(user);
        log.warn("Changing password for trainer.");
        trainerDAO.save(check);
    }

    public List<TrainerEntity> findActiveForOtherTrainees(AuthDTO authDTO, Long traineeId) {
        userService.authentication(authDTO);
        trainerValidation.validateTraineeId(traineeId);
        return trainerDAO.findActiveForOtherTrainees(traineeId);
    }

    public void changeActiveStatus(AuthDTO authDTO, Long trainerId) {
        userService.authentication(authDTO);
        TrainerEntity trainer = findById(trainerId);
        log.info("Changed active status");
        trainerDAO.changeActiveStatus(trainer);
    }
}
