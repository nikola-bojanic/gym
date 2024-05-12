package com.nikolabojanic.service;

import com.nikolabojanic.entity.TrainingEntity;
import com.nikolabojanic.repository.TrainingRepository;
import io.micrometer.core.instrument.Counter;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingTypeService trainingTypeService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final Counter totalTransactionsCounter;

    public TrainingEntity create(TrainingEntity training) {
        if (training.getTrainer() != null && training.getTrainer().getUser() != null
                && training.getTrainer().getUser().getUsername() != null) {
            training.setTrainer(trainerService.findByUsername(training.getTrainer().getUser().getUsername()));
        }
        if (training.getTrainee() != null && training.getTrainee().getUser() != null
                && training.getTrainee().getUser().getUsername() != null) {
            training.setTrainee(traineeService.findByUsername(training.getTrainee().getUser().getUsername()));
        }
        if (training.getType() != null && training.getType().getId() != null) {
            training.setType(trainingTypeService.findById(training.getType().getId()));
        }
        log.info("Created a new training");
        totalTransactionsCounter.increment();
        return trainingRepository.save(training);
    }

    public List<TrainingEntity> findByTraineeAndFilter(
            String username,
            LocalDate begin,
            LocalDate end,
            String trainerName,
            Long typeId) {
        log.info("Retrieved trainings by trainee username and filter.");
        totalTransactionsCounter.increment();
        if (typeId != null) {
            return trainingRepository
                    .findByTraineeUserUsernameAndDateBetweenAndTrainerUserFirstNameContainingIgnoreCaseAndTypeId(
                            username, begin, end, trainerName, typeId);
        } else {
            return trainingRepository.
                    findByTraineeUserUsernameAndDateBetweenAndTrainerUserFirstNameContainingIgnoreCase(
                            username, begin, end, trainerName);
        }

    }

    public List<TrainingEntity> findByTrainerAndFilter(
            String username,
            LocalDate begin,
            LocalDate end,
            String traineeName) {
        log.info("Retrieved trainings by trainer username and filter.");
        totalTransactionsCounter.increment();
        return trainingRepository
                .findByTrainerUserUsernameAndDateBetweenAndTraineeUserFirstNameContainingIgnoreCase(
                        username, begin, end, traineeName);
    }
}
