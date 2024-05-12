package com.nikolabojanic.service;

import com.nikolabojanic.entity.TrainingEntity;
import com.nikolabojanic.exception.TsEntityNotFoundException;
import com.nikolabojanic.repository.TrainingRepository;
import java.time.LocalDate;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional("transactionManager")
@Service
@Slf4j
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainerService trainerService;

    public TrainingService(TrainingRepository trainingRepository, @Lazy TrainerService trainerService) {
        this.trainingRepository = trainingRepository;
        this.trainerService = trainerService;
    }

    /**
     * Saves a training to a database.
     *
     * @param training {@link TrainingEntity} Training to be saved.
     * @return {@link TrainingEntity} Saved training entity.
     */
    public TrainingEntity addTraining(TrainingEntity training) {
        training.setTrainer(trainerService.addTrainer(training.getTrainer()));
        log.info("Saving new training for trainer: {}", training.getTrainer().getUsername());
        return trainingRepository.save(training);
    }

    /**
     * Deletes all trainings that match the trainer username and a date.
     *
     * @param username {@link String} Trainer's username for a given training.
     * @param date     {@link LocalDate} Date on which the training was scheduled.
     * @return {@link List} of deleted {@link TrainingEntity}
     */
    public List<TrainingEntity> deleteTrainings(String username, LocalDate date) {
        List<TrainingEntity> deletedTrainings = trainingRepository.findByUsernameAndDate(username, date);
        if (deletedTrainings.isEmpty()) {
            log.error("Attempted to delete non-existent trainings."
                + " Trainer username: {}, Date: {}", username, date);
            throw new TsEntityNotFoundException(
                "Trainings for trainer: " + username + " and date: " + date + " do not exist");
        }
        log.warn("Deleting training for trainee: {} on date: {}", username, date);
        trainingRepository.deleteTrainingForTrainerAndDate(username, date);
        return deletedTrainings;
    }

    /**
     * Finds all trainings for a given trainer username.
     *
     * @param username {@link String} Trainer username by which trainings are retrieved.
     * @return {@link List} of {@link TrainingEntity} that are assigned to a trainer.
     */
    public List<TrainingEntity> findByTrainer(String username) {
        List<TrainingEntity> trainings = trainingRepository.findByTrainerUsername(username);
        if (trainings.isEmpty()) {
            log.error("Attempted to fetch non-existent trainings."
                + " Trainer username: {}", username);
            throw new TsEntityNotFoundException(
                "Trainings for trainer: " + username + " do not exist");
        }
        log.info("Fetched trainings for trainer: {}", username);
        return trainings;
    }
}
