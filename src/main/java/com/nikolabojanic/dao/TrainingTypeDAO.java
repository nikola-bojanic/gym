package com.nikolabojanic.dao;

import com.nikolabojanic.model.TrainingTypeEntity;

import java.util.Optional;

public interface TrainingTypeDAO {
    Optional<TrainingTypeEntity> findById(Long id);
}
