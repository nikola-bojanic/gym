package com.nikolabojanic.service;

import com.nikolabojanic.dto.TraineeTrainerUpdateRequestDTO;
import com.nikolabojanic.exception.SCEntityNotFoundException;
import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.UserEntity;
import com.nikolabojanic.repository.TraineeRepository;
import io.micrometer.core.instrument.Counter;
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
    private final TraineeRepository traineeRepository;
    private final UserService userService;
    private final TrainerService trainerService;
    private final Counter totalTransactionsCounter;

    @Autowired
    public TraineeService(
            TraineeRepository traineeRepository,
            UserService userService,
            @Lazy TrainerService trainerService,
            Counter totalTransactionsCounter) {
        this.traineeRepository = traineeRepository;
        this.userService = userService;
        this.trainerService = trainerService;
        this.totalTransactionsCounter = totalTransactionsCounter;
    }

    public TraineeEntity createTraineeProfile(TraineeEntity trainee) {
        UserEntity user = userService.generateUsernameAndPassword(trainee.getUser());
        trainee.setUser(user);
        log.info("Created trainee profile");
        totalTransactionsCounter.increment();
        return traineeRepository.save(trainee);
    }

    public TraineeEntity updateTraineeProfile(String username, TraineeEntity trainee) {
        TraineeEntity managedTrainee = findByUsername(username);
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
        totalTransactionsCounter.increment();
        return traineeRepository.save(managedTrainee);
    }

    public TraineeEntity findByUsername(String username) {
        totalTransactionsCounter.increment();
        Optional<TraineeEntity> exists = traineeRepository.findByUsername(username);
        if (exists.isPresent()) {
            log.info("Successfully retrieved trainee with username {}.", username);
            return exists.get();
        } else {
            log.error("Attempted to fetch trainee with non-existent username {}. " +
                    "Status: {}", username, HttpStatus.NOT_FOUND.value());
            throw new SCEntityNotFoundException("Trainee with username " + username + " doesn't exist");
        }
    }


    public TraineeEntity deleteByUsername(String username) {
        totalTransactionsCounter.increment();
        Optional<TraineeEntity> deleted = traineeRepository.findByUsername(username);
        if (deleted.isEmpty()) {
            log.error("Attempted to delete trainee with non-existent username {}. " +
                    "Status: {}", username, HttpStatus.NOT_FOUND.value());
            throw new SCEntityNotFoundException("Trainee with username " + username + " doesn't exist");
        } else {
            totalTransactionsCounter.increment();
            traineeRepository.deleteByUsername(username);
            log.warn("Deleted trainee with username: {}", username);
            return deleted.get();
        }
    }

    public TraineeEntity updateTraineeTrainers(
            String username,
            List<TraineeTrainerUpdateRequestDTO> inputTrainers) {
        TraineeEntity trainee = findByUsername(username);
        List<TrainerEntity> trainers = new ArrayList<>();
        inputTrainers.forEach(trainer -> trainers.add(trainerService.findByUsername(trainer.getUsername())));
        trainee.setTrainers(trainers);
        log.info("Updated trainee trainer list");
        totalTransactionsCounter.increment();
        return traineeRepository.save(trainee);
    }

    public void changeActiveStatus(String username, Boolean isActive) {
        TraineeEntity trainee = findByUsername(username);
        UserEntity managedUser = trainee.getUser();
        managedUser.setIsActive(isActive);
        log.info("Changed trainee active status");
        totalTransactionsCounter.increment();
        traineeRepository.save(trainee);
    }
}
