package com.nikolabojanic.service;

import com.nikolabojanic.dao.TraineeDAO;
import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.UserEntity;
import com.nikolabojanic.validation.TraineeValidation;
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
public class TraineeService {

    private final TraineeDAO traineeDAO;
    private final UserService userService;
    private final TrainerService trainerService;
    private final UserValidation userValidation;
    private final TraineeValidation traineeValidation;

    public TraineeEntity createTraineeProfile(TraineeEntity trainee) {
        traineeValidation.validateCreateTraineeRequest(trainee);
        UserEntity user = userService.generateUsernameAndPassword(trainee.getUser());
        trainee.setUser(user);
        log.info("Created trainee profile");
        return traineeDAO.save(trainee);
    }

    public TraineeEntity updateTraineeProfile(AuthDTO authDTO, TraineeEntity trainee) {
        userService.authentication(authDTO);
        traineeValidation.validateUpdateTraineeRequest(trainee);
        TraineeEntity managedTrainee = findById(trainee.getId());
        UserEntity inputUser = trainee.getUser();
        userValidation.validateUserFields(inputUser);
        UserEntity user = userService.findByUsername(inputUser.getUsername());
        userValidation.validateUserPermissionToEdit(user.getUsername(), inputUser.getUsername());
        user.setIsActive(inputUser.getIsActive());
        user.setFirstName(inputUser.getFirstName());
        user.setLastName(inputUser.getLastName());
        managedTrainee.setUser(user);
        if (trainee.getAddress() != null) {
            managedTrainee.setAddress(trainee.getAddress());
        }
        if (trainee.getDateOfBirth() != null) {
            managedTrainee.setDateOfBirth(trainee.getDateOfBirth());
        }
        log.info("Updating trainee profile");
        return traineeDAO.save(managedTrainee);
    }


    public TraineeEntity findById(Long id) {
        traineeValidation.validateIdNotNull(id);
        return traineeDAO.findById(id)
                .orElseThrow(() -> {
                    log.error("Attempted to fetch trainee with non-existent ID {}", id);
                    return new NoSuchElementException("Trainee with ID " + id + " doesn't exist");
                });
    }

    public TraineeEntity findByUsername(AuthDTO authDTO, String username) {
        userService.authentication(authDTO);
        UserEntity user = userService.findByUsername(username);
        Long userId = user.getId();
        return traineeDAO.findByUserId(userId).orElseThrow(() -> {
            log.error("Attempted to fetch trainee with non-existent user ID {}", userId);
            return new NoSuchElementException("Trainee with user ID " + userId + " doesn't exist");
        });
    }

    public void changeTraineePassword(AuthDTO authDTO, TraineeEntity trainee, String password) {
        userService.authentication(authDTO);
        traineeValidation.validateTraineePasswordChange(trainee, password);
        userValidation.validateUsernameAndPassword(trainee.getUser().getUsername(), password);
        UserEntity user = userService.findByUsername(trainee.getUser().getUsername());
        TraineeEntity check = findById(trainee.getId());
        userValidation.validateUserPermissionToEdit(check.getUser().getUsername(), trainee.getUser().getUsername());
        user.setPassword(password);
        check.setUser(user);
        log.warn("Changing password for trainee.");
        traineeDAO.save(check);
    }

    public TraineeEntity deleteByUsername(AuthDTO authDTO, String username) {
        userService.authentication(authDTO);
        TraineeEntity trainee = findByUsername(authDTO, username);
        log.warn("Deleting trainee with id: {}", trainee.getId());
        traineeDAO.delete(trainee.getId());
        return trainee;
    }

    public TraineeEntity updateTraineeTrainers(AuthDTO authDTO, TraineeEntity trainee, List<TrainerEntity> inputTrainers) {
        userService.authentication(authDTO);
        traineeValidation.validateTraineeNotNull(trainee);
        findById(trainee.getId());
        inputTrainers.forEach(trainer -> trainerService.findById(trainer.getId()));
        return traineeDAO.saveTrainers(trainee, inputTrainers);
    }

    public void changeActiveStatus(AuthDTO authDTO, Long traineeId) {
        userService.authentication(authDTO);
        TraineeEntity trainee = findById(traineeId);
        log.info("Changed active status");
        traineeDAO.changeActiveStatus(trainee);
    }
}
