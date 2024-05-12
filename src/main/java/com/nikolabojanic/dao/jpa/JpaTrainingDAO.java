package com.nikolabojanic.dao.jpa;

import com.nikolabojanic.dao.TrainingDAO;
import com.nikolabojanic.model.TrainingEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JpaTrainingDAO implements TrainingDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public TrainingEntity save(TrainingEntity training) {
        entityManager.merge(training);
        return training;
    }

    @Override
    public List<TrainingEntity> findByTraineeIdAndDate(Long traineeId, LocalDate beginDate, LocalDate endDate) {
        try {
            return entityManager.
                    createQuery("from TrainingEntity where trainee.id = :traineeId and date between :beginDate and :endDate", TrainingEntity.class).
                    setParameter("traineeId", traineeId).
                    setParameter("beginDate", beginDate).
                    setParameter("endDate", endDate).
                    getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }

    }

    @Override
    public List<TrainingEntity> findByTrainerIdAndDate(Long trainerId, LocalDate beginDate, LocalDate endDate) {
        try {
            return entityManager.
                    createQuery("from TrainingEntity where trainer.id = :trainerId and date between :beginDate and :endDate", TrainingEntity.class).
                    setParameter("trainerId", trainerId).
                    setParameter("beginDate", beginDate).
                    setParameter("endDate", endDate).
                    getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

}
