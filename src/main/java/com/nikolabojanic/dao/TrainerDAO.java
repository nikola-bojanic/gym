package com.nikolabojanic.dao;

import com.nikolabojanic.model.TrainerEntity;

import java.util.List;
import java.util.Optional;

public interface TrainerDAO {
    TrainerEntity save(TrainerEntity trainer);

    Optional<TrainerEntity> findByUsername(String username);

    List<TrainerEntity> findActiveForOtherTrainees(Long traineeId);
}
