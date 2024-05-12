package com.nikolabojanic.dao.impl.memory;

import com.nikolabojanic.config.TraineeFileStorage;
import com.nikolabojanic.config.TrainerFileStorage;
import com.nikolabojanic.config.TrainingFileStorage;
import com.nikolabojanic.dao.TrainingDAO;
import com.nikolabojanic.model.Trainee;
import com.nikolabojanic.model.Trainer;
import com.nikolabojanic.model.Training;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Repository
public class InMemoryTrainingDAO implements TrainingDAO {
    private TrainingFileStorage trainingFileStorage;
    private TraineeFileStorage traineeFileStorage;
    private TrainerFileStorage trainerFileStorage;

    @Override
    public Optional<Training> findById(Long id) {
        return Optional.ofNullable(trainingFileStorage.getTrainings().get(id));
    }

    @Override
    public Training save(Training training) {
        Long mapId = trainingFileStorage.getMaxId();
        if (training.getId() == null) {
            training.setId(mapId++);
            log.info("Creating training with ID: {}", training.getId());
        } else {
            log.info("Updating training with ID: {}", training.getId());
        }
        Map<Long, Trainee> trainees = traineeFileStorage.getTrainees();
        Map<Long, Trainer> trainers = trainerFileStorage.getTrainers();
        Map<Long, Training> trainings = trainingFileStorage.getTrainings();
        trainers.get(training.getTrainer().getId()).getTrainings().add(training);
        trainees.get(training.getTrainee().getId()).getTrainings().add(training);
        trainings.put(mapId, training);
        trainerFileStorage.setTrainers(trainers);
        traineeFileStorage.setTrainees(trainees);
        trainingFileStorage.setTrainings(trainings);
        trainingFileStorage.setMaxId(mapId);
        updateStorageFile();
        return training;
    }

    private void updateStorageFile() {
        try {
            trainingFileStorage.writeTrainings();
        } catch (IOException e) {
            log.error("Error saving trainings to file");
        }
    }

}
