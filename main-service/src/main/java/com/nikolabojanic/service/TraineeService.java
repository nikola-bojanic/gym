package com.nikolabojanic.service;

import com.nikolabojanic.converter.TraineeConverter;
import com.nikolabojanic.domain.TraineeDomain;
import com.nikolabojanic.dto.TraineeTrainerUpdateRequestDto;
import com.nikolabojanic.entity.TraineeEntity;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.entity.UserEntity;
import com.nikolabojanic.enumeration.UserRole;
import com.nikolabojanic.exception.ScEntityNotFoundException;
import com.nikolabojanic.repository.TraineeRepository;
import io.micrometer.core.instrument.Counter;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Transactional
@Service
public class TraineeService {
    private final TraineeRepository traineeRepository;
    private final UserService userService;
    private final TrainerService trainerService;
    private final PasswordEncoder passwordEncoder;
    private final TraineeConverter traineeConverter;
    private final Counter totalTransactionsCounter;

    /**
     * Constructs a new TraineeService with the specified dependencies.
     *
     * @param traineeRepository        The repository for TraineeEntity.
     * @param userService              The service for user-related operations.
     * @param trainerService           The service for trainer-related operations.
     * @param passwordEncoder          The password encoder for securing passwords.
     * @param traineeConverter         The converter for transforming TraineeEntity to domain model.
     * @param totalTransactionsCounter The counter for tracking total service transactions.
     */
    public TraineeService(
        TraineeRepository traineeRepository,
        UserService userService,
        @Lazy TrainerService trainerService,
        PasswordEncoder passwordEncoder,
        TraineeConverter traineeConverter,
        Counter totalTransactionsCounter) {
        this.traineeRepository = traineeRepository;
        this.userService = userService;
        this.trainerService = trainerService;
        this.passwordEncoder = passwordEncoder;
        this.traineeConverter = traineeConverter;
        this.totalTransactionsCounter = totalTransactionsCounter;
    }

    /**
     * Creates a new trainee profile with the provided TraineeEntity.
     *
     * @param trainee The TraineeEntity representing the new trainee profile.
     * @return The TraineeDomain model representing the created trainee profile.
     */
    @SuppressWarnings("VariableDeclarationUsageDistance")
    public TraineeDomain createTraineeProfile(TraineeEntity trainee) {
        UserEntity user = userService.generateUsernameAndPassword(trainee.getUser());
        TraineeDomain traineeDomain = traineeConverter.convertEntityToDomainModel(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(UserRole.TRAINEE);
        trainee.setUser(user);
        log.info("Created trainee profile");
        totalTransactionsCounter.increment();
        traineeRepository.save(trainee);
        return traineeDomain;
    }

    /**
     * Updates an existing trainee profile with the provided TraineeEntity.
     *
     * @param username The username of the trainee to be updated.
     * @param trainee  The TraineeEntity representing the updated trainee profile.
     * @return The updated TraineeEntity.
     */
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

    /**
     * Finds a trainee by username.
     *
     * @param username The username of the trainee to be retrieved.
     * @return The TraineeEntity representing the retrieved trainee.
     * @throws ScEntityNotFoundException If the trainee with the specified username is not found.
     */
    public TraineeEntity findByUsername(String username) {
        totalTransactionsCounter.increment();
        Optional<TraineeEntity> exists = traineeRepository.findByUsername(username);
        if (exists.isPresent()) {
            log.info("Successfully retrieved trainee with username {}.", username);
            return exists.get();
        } else {
            log.error("Attempted to fetch trainee with non-existent username {}. "
                + "Status: {}", username, HttpStatus.NOT_FOUND.value());
            throw new ScEntityNotFoundException("Trainee with username " + username + " doesn't exist");
        }
    }

    /**
     * Deletes a trainee by username.
     *
     * @param username The username of the trainee to be deleted.
     * @return The deleted TraineeEntity.
     * @throws ScEntityNotFoundException If the trainee with the specified username is not found.
     */
    public TraineeEntity deleteByUsername(String username) {
        totalTransactionsCounter.increment();
        Optional<TraineeEntity> deleted = traineeRepository.findByUsername(username);
        if (deleted.isEmpty()) {
            log.error("Attempted to delete trainee with non-existent username {}. "
                + "Status: {}", username, HttpStatus.NOT_FOUND.value());
            throw new ScEntityNotFoundException("Trainee with username " + username + " doesn't exist");
        } else {
            totalTransactionsCounter.increment();
            traineeRepository.deleteByUsername(username);
            log.warn("Deleted trainee with username: {}", username);
            return deleted.get();
        }
    }

    /**
     * Updates the trainers associated with a trainee.
     *
     * @param username      The username of the trainee.
     * @param inputTrainers The list of TraineeTrainerUpdateRequestDto representing trainers to be associated.
     * @return The updated TraineeEntity.
     */
    public TraineeEntity updateTraineeTrainers(
        String username,
        List<TraineeTrainerUpdateRequestDto> inputTrainers) {
        TraineeEntity trainee = findByUsername(username);
        List<TrainerEntity> trainers = new ArrayList<>();
        inputTrainers.forEach(trainer -> trainers.add(trainerService.findByUsername(trainer.getUsername())));
        trainee.setTrainers(trainers);
        log.info("Updated trainee trainer list");
        totalTransactionsCounter.increment();
        return traineeRepository.save(trainee);
    }

    /**
     * Changes the active status of a trainee.
     *
     * @param username The username of the trainee whose active status is to be changed.
     * @param isActive The new active status.
     */
    public void changeActiveStatus(String username, Boolean isActive) {
        TraineeEntity trainee = findByUsername(username);
        UserEntity managedUser = trainee.getUser();
        managedUser.setIsActive(isActive);
        log.info("Changed trainee active status");
        totalTransactionsCounter.increment();
        traineeRepository.save(trainee);
    }
}
