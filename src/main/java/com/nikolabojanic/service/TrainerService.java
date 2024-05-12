package com.nikolabojanic.service;


import com.nikolabojanic.converter.TrainerConverter;
import com.nikolabojanic.domain.TrainerDomain;
import com.nikolabojanic.entity.TraineeEntity;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.entity.UserEntity;
import com.nikolabojanic.exception.ScEntityNotFoundException;
import com.nikolabojanic.repository.TrainerRepository;
import io.micrometer.core.instrument.Counter;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class TrainerService {
    private final TrainerRepository trainerRepository;
    private final UserService userService;
    private final TrainingTypeService trainingTypeService;
    private final TraineeService traineeService;
    private final Counter totalTransactionsCounter;
    private final PasswordEncoder passwordEncoder;
    private final TrainerConverter trainerConverter;

    public TrainerDomain createTrainerProfile(TrainerEntity trainer) {
        UserEntity user = userService.generateUsernameAndPassword(trainer.getUser());
        TrainerDomain domainModel = trainerConverter.convertEntityToDomainModel(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        trainer.setUser(user);
        if (trainer.getSpecialization() != null && trainer.getSpecialization().getId() != null) {
            trainer.setSpecialization(trainingTypeService.findById(trainer.getSpecialization().getId()));
        }
        log.info("Created trainer profile");
        totalTransactionsCounter.increment();
        trainerRepository.save(trainer);
        return domainModel;
    }

    public TrainerEntity updateTrainerProfile(String username, TrainerEntity trainer) {
        TrainerEntity managedTrainer = findByUsername(username);
        UserEntity inputUser = trainer.getUser();
        UserEntity managedUser = managedTrainer.getUser();
        managedUser.setIsActive(inputUser.getIsActive());
        managedUser.setFirstName(inputUser.getFirstName());
        managedUser.setLastName(inputUser.getLastName());
        log.info("Updated trainer profile");
        totalTransactionsCounter.increment();
        return trainerRepository.save(managedTrainer);
    }

    public TrainerEntity findByUsername(String username) {
        totalTransactionsCounter.increment();
        Optional<TrainerEntity> exists = trainerRepository.findByUsername(username);
        if (exists.isPresent()) {
            log.info("Successfully retrieved trainer with username {}.", username);
            return exists.get();
        } else {
            log.error("Attempted to fetch trainer with non-existent username {}. " +
                    "Status: {}", username, HttpStatus.NOT_FOUND.value());
            throw new ScEntityNotFoundException("Trainer with username " + username + " doesn't exist");
        }
    }

    public void changeActiveStatus(String username, Boolean isActive) {
        TrainerEntity trainer = findByUsername(username);
        UserEntity managedUser = trainer.getUser();
        managedUser.setIsActive(isActive);
        log.info("Changed active status");
        totalTransactionsCounter.increment();
        trainerRepository.save(trainer);
    }

    public List<TrainerEntity> findActiveForOtherTrainees(String username) {
        TraineeEntity trainee = traineeService.findByUsername(username);
        log.info("Successfully retrieved active trainers for other trainees");
        totalTransactionsCounter.increment();
        return trainerRepository.findActiveForOtherTrainees(trainee.getId());
    }
}
