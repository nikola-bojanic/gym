package com.nikolabojanic.repository;

import com.nikolabojanic.entity.TrainingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainingRepository extends JpaRepository<TrainingEntity, Long> {
    List<TrainingEntity> findByTraineeUserUsernameAndDateBetweenAndTrainerUserFirstNameContainingIgnoreCase(
            String traineeUsername,
            LocalDate begin,
            LocalDate end,
            String trainerName
    );

    List<TrainingEntity> findByTraineeUserUsernameAndDateBetweenAndTrainerUserFirstNameContainingIgnoreCaseAndTypeId(
            String traineeUsername,
            LocalDate begin,
            LocalDate end,
            String trainerName,
            Long typeId
    );

    List<TrainingEntity> findByTrainerUserUsernameAndDateBetweenAndTraineeUserFirstNameContainingIgnoreCase(
            String trainerUsername,
            LocalDate begin,
            LocalDate end,
            String traineeName
    );
}
