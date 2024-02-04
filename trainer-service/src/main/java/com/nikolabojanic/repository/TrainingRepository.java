package com.nikolabojanic.repository;

import com.nikolabojanic.entity.TrainingEntity;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingRepository extends JpaRepository<TrainingEntity, Long> {
    @Query("select t from TrainingEntity t where t.trainer.username = :username")
    List<TrainingEntity> findByTrainerUsername(String username);

    @Query("select t from TrainingEntity t where t.trainer.username = :username"
        + " and t.date = :date")
    List<TrainingEntity> findByUsernameAndDate(String username, LocalDate date);

    @Modifying
    @Query("delete from TrainingEntity t where t.trainer.username = :username and date = :date")
    void deleteTrainingForTrainerAndDate(String username, LocalDate date);
}
