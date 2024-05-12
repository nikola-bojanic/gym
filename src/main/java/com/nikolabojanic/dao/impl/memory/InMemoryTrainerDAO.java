package com.nikolabojanic.dao.impl.memory;

import com.nikolabojanic.config.TraineeFileStorage;
import com.nikolabojanic.config.TrainerFileStorage;
import com.nikolabojanic.dao.TrainerDAO;
import com.nikolabojanic.model.Trainee;
import com.nikolabojanic.model.Trainer;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@AllArgsConstructor
@Repository
public class InMemoryTrainerDAO implements TrainerDAO {
    private TraineeFileStorage traineeFileStorage;
    private TrainerFileStorage trainerFileStorage;

    @Override
    public List<Trainer> getAll() {
        return new ArrayList<>(trainerFileStorage.getTrainers().values());
    }

    @Override
    public Optional<Trainer> findById(Long id) {
        return Optional.ofNullable(trainerFileStorage.getTrainers().get(id));
    }

    @Override
    public Trainer save(Trainer trainer) {
        Long mapId = trainerFileStorage.getMaxId(); // get max key value from storage
        if (trainer.getId() == null) {
            trainer.setId(mapId++); //increment it and assign it to new object
            log.info("Creating trainer with ID: {}", trainer.getId());
        } else {
            log.info("Updated trainer with ID: {}", trainer.getId());
        }
        Map<Long, Trainee> trainees = traineeFileStorage.getTrainees();
        Map<Long, Trainer> trainers = trainerFileStorage.getTrainers();
        for (Trainee trainee : trainer.getTrainees()) {
            trainee.getTrainers().add(trainer);
            trainees.put(trainee.getId(), trainee);
        }
        trainers.put(mapId, trainer);
        traineeFileStorage.setTrainees(trainees);
        trainerFileStorage.setTrainers(trainers);
        trainerFileStorage.setMaxId(mapId);
        updateFileStorage();
        return trainer;
    }

    private void updateFileStorage() {
        try {
            trainerFileStorage.writeTrainers();
            traineeFileStorage.writeTraineesTrainers();
        } catch (IOException e) {
            log.error("Error saving to file", e);
        }
    }
}
