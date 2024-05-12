package com.nikolabojanic.dao.impl.memory;

import com.nikolabojanic.config.TraineeFileStorage;
import com.nikolabojanic.config.TrainerFileStorage;
import com.nikolabojanic.dao.TraineeDAO;
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
public class InMemoryTraineeDAO implements TraineeDAO {
    private TraineeFileStorage traineeFileStorage;
    private TrainerFileStorage trainerFileStorage;

    @Override
    public List<Trainee> getAll() {
        return new ArrayList<>(traineeFileStorage.getTrainees().values());
    }

    @Override
    public Optional<Trainee> findById(Long id) {
        return Optional.ofNullable(traineeFileStorage.getTrainees().get(id));
    }

    @Override
    public Void delete(Long id) {
        log.info("Removing trainee with ID {}", id);
        Map<Long, Trainee> trainees = traineeFileStorage.getTrainees();
        Trainee trainee = trainees.get(id);
        Map<Long, Trainer> trainers = trainerFileStorage.getTrainers();
        for (Trainer trainer : trainee.getTrainers()) {
            trainer.getTrainees().remove(trainee); //remove trainee from trainer's set of trainees
            trainers.put(trainer.getId(), trainer); // update DAO trainer map
        }
        trainees.remove(id);
        traineeFileStorage.setTrainees(trainees);
        trainerFileStorage.setTrainers(trainers); // update storage trainer map
        updateFileStorage(); // update file
        return null;
    }

    @Override
    public Trainee save(Trainee trainee) {
        Long mapId = traineeFileStorage.getMaxId();
        if (trainee.getId() == null) {
            trainee.setId(mapId++);
            log.info("Creating trainee with ID: {}", trainee.getId());
        } else {
            log.info("Updating trainee with ID: {}", trainee.getId());
        }
        Map<Long, Trainee> trainees = traineeFileStorage.getTrainees();
        Map<Long, Trainer> trainers = trainerFileStorage.getTrainers();
        for (Trainer trainer : trainee.getTrainers()) {
            trainer.getTrainees().add(trainee);
            trainers.put(trainer.getId(), trainer);
        }
        trainees.put(trainee.getId(), trainee);
        trainerFileStorage.setTrainers(trainers);
        traineeFileStorage.setTrainees(trainees);
        traineeFileStorage.setMaxId(mapId);
        updateFileStorage();
        return trainee;
    }

    private void updateFileStorage() {
        try {
            traineeFileStorage.writeTrainees();
            traineeFileStorage.writeTraineesTrainers();

        } catch (IOException e) {
            log.error("Error saving trainees to file", e);
        }
    }
}
