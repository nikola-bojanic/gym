package com.nikolabojanic.dao;

import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;

import java.util.List;
import java.util.Optional;

public interface TraineeDAO {

    Optional<TraineeEntity> findById(Long id);

    void delete(Long id);

    TraineeEntity save(TraineeEntity trainee);

    Optional<TraineeEntity> findByUserId(Long id);

    TraineeEntity saveTrainers(TraineeEntity trainee, List<TrainerEntity> trainers);

    void changeActiveStatus(TraineeEntity trainee);
}
