package com.nikolabojanic.dao;

import com.nikolabojanic.model.TrainerEntity;

import java.util.List;
import java.util.Optional;

public interface TrainerDAO {
    Optional<TrainerEntity> findById(Long id);

    TrainerEntity save(TrainerEntity trainer);

    Optional<TrainerEntity> findByUserId(Long id);

    List<TrainerEntity> findActiveForOtherTrainees(Long traineeId);

    void changeActiveStatus(TrainerEntity trainer);
}
