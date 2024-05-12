package com.nikolabojanic.service;


import com.nikolabojanic.dao.TrainerDAO;
import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.exception.SCEntityNotFoundException;
import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.UserEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class TrainerService {
    private final TrainerDAO trainerDAO;
    private final UserService userService;
    private final TrainingTypeService trainingTypeService;
    private final TraineeService traineeService;

    public TrainerEntity createTrainerProfile(TrainerEntity trainer) {
        UserEntity user = userService.generateUsernameAndPassword(trainer.getUser());
        trainer.setUser(user);
        if (trainer.getSpecialization() != null && trainer.getSpecialization().getId() != null) {
            trainer.setSpecialization(trainingTypeService.findById(trainer.getSpecialization().getId()));
        }
        log.info("Created trainer profile");
        return trainerDAO.save(trainer);
    }

    public TrainerEntity updateTrainerProfile(AuthDTO authDTO, String username, TrainerEntity trainer) {
        TrainerEntity managedTrainer = findByUsername(authDTO, username);
        UserEntity inputUser = trainer.getUser();
        UserEntity managedUser = managedTrainer.getUser();
        managedUser.setIsActive(inputUser.getIsActive());
        managedUser.setFirstName(inputUser.getFirstName());
        managedUser.setLastName(inputUser.getLastName());
        log.info("Updated trainer profile");
        return trainerDAO.save(managedTrainer);
    }

    public TrainerEntity findByUsername(AuthDTO authDTO, String username) {
        userService.authentication(authDTO);
        Optional<TrainerEntity> exists = trainerDAO.findByUsername(username);
        if (exists.isPresent()) {
            log.info("Successfully retrieved trainer with username {}.", username);
            return exists.get();
        } else {
            log.error("Attempted to fetch trainer with non-existent username {}. " +
                    "Status: {}", username, HttpStatus.NOT_FOUND.value());
            throw new SCEntityNotFoundException("Trainer with username " + username + " doesn't exist");
        }
    }

    public void changeActiveStatus(AuthDTO authDTO, String username, Boolean isActive) {
        TrainerEntity trainer = findByUsername(authDTO, username);
        UserEntity managedUser = trainer.getUser();
        managedUser.setIsActive(isActive);
        log.info("Changed active status");
        trainerDAO.save(trainer);
    }

    public List<TrainerEntity> findActiveForOtherTrainees(AuthDTO authDTO, String username) {
        TraineeEntity trainee = traineeService.findByUsername(authDTO, username);
        log.info("Successfully retrieved active trainers for other trainees");
        return trainerDAO.findActiveForOtherTrainees(trainee.getId());
    }
}
