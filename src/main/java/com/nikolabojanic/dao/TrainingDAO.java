package com.nikolabojanic.dao;

import com.nikolabojanic.model.TrainingEntity;

import java.time.LocalDate;
import java.util.List;

public interface TrainingDAO {

    TrainingEntity save(TrainingEntity training);

    List<TrainingEntity> findByTraineeIdAndDate(Long id, LocalDate beginDate, LocalDate endDate);

    List<TrainingEntity> findByTrainerIdAndDate(Long id, LocalDate beginDate, LocalDate endDate);
}
