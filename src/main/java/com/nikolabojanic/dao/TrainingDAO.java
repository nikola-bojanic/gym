package com.nikolabojanic.dao;

import com.nikolabojanic.model.Training;

import java.util.Optional;

public interface TrainingDAO {
    Optional<Training> findById(Long id);

    Training save(Training training);
}
