package com.nikolabojanic.service;

import com.nikolabojanic.dao.TraineeDAO;
import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.dto.TraineeTrainerUpdateRequestDTO;
import com.nikolabojanic.exception.SCEntityNotFoundException;
import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.UserEntity;
import com.nikolabojanic.validation.TraineeValidation;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional
@Service
public class TraineeService {
    private final TraineeDAO traineeDAO;
    private final UserService userService;
    private final TrainerService trainerService;

    @Autowired
    public TraineeService(
            TraineeDAO traineeDAO,
            UserService userService,
            @Lazy TrainerService trainerService,
            TraineeValidation traineeValidation) {
        this.traineeDAO = traineeDAO;
        this.userService = userService;
        this.trainerService = trainerService;
    }

    public TraineeEntity createTraineeProfile(TraineeEntity trainee) {
        UserEntity user = userService.generateUsernameAndPassword(trainee.getUser());
        trainee.setUser(user);
        log.info("Created trainee profile");
        return traineeDAO.save(trainee);
    }

    public TraineeEntity updateTraineeProfile(AuthDTO authDTO, String username, TraineeEntity trainee) {
        TraineeEntity managedTrainee = findByUsername(authDTO, username);
        UserEntity inputUser = trainee.getUser();
        UserEntity managedUser = managedTrainee.getUser();
        managedUser.setIsActive(inputUser.getIsActive());
        managedUser.setFirstName(inputUser.getFirstName());
        managedUser.setLastName(inputUser.getLastName());
        if (trainee.getAddress() != null) {
            managedTrainee.setAddress(trainee.getAddress());
        }
        if (trainee.getDateOfBirth() != null) {
            managedTrainee.setDateOfBirth(trainee.getDateOfBirth());
        }
        log.info("Updated trainee profile");
        return traineeDAO.save(managedTrainee);
    }

    public TraineeEntity findByUsername(AuthDTO authDTO, String username) {
        userService.authentication(authDTO);
        Optional<TraineeEntity> exists = traineeDAO.findByUsername(username);
        if (exists.isPresent()) {
            log.info("Successfully retrieved trainee with username {}.", username);
            return exists.get();
        } else {
            log.error("Attempted to fetch trainee with non-existent username {}. Status: {}", username, HttpStatus.NOT_FOUND.value());
            throw new SCEntityNotFoundException("Trainee with username " + username + " doesn't exist");
        }
    }


    public TraineeEntity deleteByUsername(AuthDTO authDTO, String username) {
        userService.authentication(authDTO);
        Optional<TraineeEntity> deleted = traineeDAO.deleteByUsername(username);
        if (deleted.isEmpty()) {
            log.error("Attempted to delete trainee with non-existent username {}. " +
                    "Status: {}", username, HttpStatus.NOT_FOUND.value());
            throw new SCEntityNotFoundException("Trainee with username " + username + " doesn't exist");
        } else {
            log.warn("Deleted trainee with username: {}", username);
            return deleted.get();
        }
    }

    public List<TrainerEntity> updateTraineeTrainers(
            AuthDTO authDTO,
            String username,
            List<TraineeTrainerUpdateRequestDTO> inputTrainers) {
        TraineeEntity trainee = findByUsername(authDTO, username);
        List<TrainerEntity> trainers = new ArrayList<>();
        inputTrainers.forEach(trainer -> trainers.add(trainerService.findByUsername(authDTO, trainer.getUsername())));
        log.info("Updated trainee trainer list");
        return traineeDAO.saveTrainers(trainee, trainers);
    }

    public void changeActiveStatus(AuthDTO authDTO, String username, Boolean isActive) {
        TraineeEntity trainee = findByUsername(authDTO, username);
        UserEntity managedUser = trainee.getUser();
        managedUser.setIsActive(isActive);
        log.info("Changed trainee active status");
        traineeDAO.save(trainee);
    }
}
