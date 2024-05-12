package com.nikolabojanic.dao;

import com.nikolabojanic.model.TrainingEntity;

import java.time.LocalDate;
import java.util.List;

public interface TrainingDAO {

    TrainingEntity save(TrainingEntity training);

    List<TrainingEntity> findByTraineeAndFilter(
            String username,
            LocalDate beginDate,
            LocalDate endDate,
            String trainerName,
            Long typeId);

    List<TrainingEntity> findByTrainerAndFilter(
            String username,
            LocalDate beginDate,
            LocalDate endDate,
            String traineeName);
}
