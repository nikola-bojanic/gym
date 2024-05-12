package com.nikolabojanic.dao;

import com.nikolabojanic.model.Trainee;

import java.util.List;
import java.util.Optional;

public interface TraineeDAO {
    List<Trainee> getAll();

    Optional<Trainee> findById(Long id);

    Void delete(Long id);

    Trainee save(Trainee trainee);
}
