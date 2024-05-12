package com.nikolabojanic.dao;

import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;

import java.util.List;
import java.util.Optional;

public interface TraineeDAO {

    Optional<TraineeEntity> deleteByUsername(String username);

    TraineeEntity save(TraineeEntity trainee);

    Optional<TraineeEntity> findByUsername(String username);

    List<TrainerEntity> saveTrainers(TraineeEntity trainee, List<TrainerEntity> trainers);

}
