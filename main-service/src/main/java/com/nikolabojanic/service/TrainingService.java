package com.nikolabojanic.service;

import com.nikolabojanic.client.trainer.TrainerServiceClient;
import com.nikolabojanic.client.trainer.TrainerWorkloadRequestDto;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.entity.TrainingEntity;
import com.nikolabojanic.entity.UserEntity;
import com.nikolabojanic.enumeration.Action;
import com.nikolabojanic.exception.ScEntityNotFoundException;
import com.nikolabojanic.logging.MdcFilter;
import com.nikolabojanic.repository.TrainingRepository;
import io.micrometer.core.instrument.Counter;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class TrainingService {
    private final TrainingRepository trainingRepository;
    private final TrainingTypeService trainingTypeService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final TrainerServiceClient trainerServiceClient;
    private final MdcFilter mdcFilter;
    private final Counter totalTransactionsCounter;

    /**
     * Creates a new training with the provided information.
     *
     * @param training The TrainingEntity object representing the new training.
     * @param jwt      The JSON Web Token for authentication.
     * @return The created TrainingEntity.
     */
    public TrainingEntity create(TrainingEntity training, String jwt) {
        TrainerEntity trainer = new TrainerEntity();
        if (training.getTrainer() != null && training.getTrainer().getUser() != null
            && training.getTrainer().getUser().getUsername() != null) {
            trainer = trainerService.findByUsername(training.getTrainer().getUser().getUsername());
            training.setTrainer(trainer);
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
        UserEntity user = trainer.getUser();
        TrainerWorkloadRequestDto requestDto = new TrainerWorkloadRequestDto(
            user.getUsername(),
            user.getFirstName(),
            user.getLastName(),
            user.getIsActive(),
            training.getDate(),
            training.getDuration(),
            Action.ADD);
        log.info("{} {} {} {} {} {} {}", requestDto.getUsername(),
            requestDto.getFirstName(),
            requestDto.getLastName(),
            requestDto.getIsActive(),
            requestDto.getDate(),
            requestDto.getDuration(),
            requestDto.getAction());
        trainerServiceClient.workloadUpdate(requestDto, mdcFilter.getTraceId(), jwt);
        return trainingRepository.save(training);
    }

    /**
     * Deletes a training with the specified ID.
     *
     * @param id  The ID of the training to be deleted.
     * @param jwt The JSON Web Token for authentication.
     * @return The deleted TrainingEntity.
     * @throws ScEntityNotFoundException If the training with the specified ID is not found.
     */
    public TrainingEntity deleteTraining(Long id, String jwt) {
        totalTransactionsCounter.increment();
        Optional<TrainingEntity> deleted = trainingRepository.findById(id);
        if (deleted.isEmpty()) {
            log.error("Attempted to delete non existing training. {} ", HttpStatus.NOT_FOUND.value());
            throw new ScEntityNotFoundException("Training with id " + id + " doesn't exist");
        } else {
            totalTransactionsCounter.increment();
            trainingRepository.deleteById(id);
            log.warn("Deleted training with id: {}", id);
            TrainingEntity deletedTraining = deleted.get();
            UserEntity user = deletedTraining.getTrainer().getUser();
            TrainerWorkloadRequestDto requestDto = new TrainerWorkloadRequestDto(
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getIsActive(),
                deletedTraining.getDate(),
                deletedTraining.getDuration(),
                Action.DELETE
            );
            trainerServiceClient.workloadUpdate(requestDto, mdcFilter.getTraceId(), jwt);
            return deleted.get();
        }
    }

    /**
     * Finds trainings for a trainee based on the provided parameters.
     *
     * @param username    The username of the trainee.
     * @param begin       The start date for filtering.
     * @param end         The end date for filtering.
     * @param trainerName The name of the trainer for additional filtering.
     * @param typeId      The ID of the training type for additional filtering.
     * @return List of TrainingEntity representing the filtered trainings.
     */
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
            return trainingRepository
                .findByTraineeUserUsernameAndDateBetweenAndTrainerUserFirstNameContainingIgnoreCase(
                    username, begin, end, trainerName);
        }

    }

    /**
     * Finds trainings for a trainer based on the provided parameters.
     *
     * @param username    The username of the trainer.
     * @param begin       The start date for filtering.
     * @param end         The end date for filtering.
     * @param traineeName The name of the trainee for additional filtering.
     * @return List of TrainingEntity representing the filtered trainings.
     */
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
