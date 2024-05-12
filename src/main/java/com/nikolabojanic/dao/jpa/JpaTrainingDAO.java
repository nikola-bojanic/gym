package com.nikolabojanic.dao.jpa;

import com.nikolabojanic.dao.TrainingDAO;
import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.TrainingEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class JpaTrainingDAO implements TrainingDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public TrainingEntity save(TrainingEntity training) {
        entityManager.merge(training);
        log.info("Wrote changes to training with id {} to database", training.getId());
        return training;
    }

    @Override
    public List<TrainingEntity> findByTraineeAndFilter(
            String traineeUsername,
            LocalDate beginDate,
            LocalDate endDate,
            String trainerName,
            Long trainingTypeId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrainingEntity> criteriaQuery = criteriaBuilder.createQuery(TrainingEntity.class);
        Root<TrainingEntity> root = criteriaQuery.from(TrainingEntity.class);
        List<Predicate> predicates = new ArrayList<>();
        if (traineeUsername != null) {
            Join<TrainingEntity, TraineeEntity> traineeJoin = root.join("trainee", JoinType.LEFT);
            predicates.add(criteriaBuilder.equal(traineeJoin.get("user").get("username"), traineeUsername));
        }
        if (beginDate != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), beginDate));
        }
        if (endDate != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), endDate));
        }
        if (trainerName != null) {
            Join<TrainingEntity, TrainerEntity> trainerJoin = root.join("trainer", JoinType.LEFT);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(trainerJoin.get("user").get("firstName")),
                    "%" + trainerName.toLowerCase() + "%"));
        }
        if (trainingTypeId != null) {
            predicates.add(criteriaBuilder.equal(root.get("type").get("id"), trainingTypeId));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        log.info("Successfully executed the filter search for trainee's trainings in the database");
        return entityManager.createQuery(criteriaQuery).getResultList();
    }

    @Override
    public List<TrainingEntity> findByTrainerAndFilter(
            String trainerUsername,
            LocalDate beginDate,
            LocalDate endDate,
            String traineeName) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TrainingEntity> criteriaQuery = criteriaBuilder.createQuery(TrainingEntity.class);
        Root<TrainingEntity> root = criteriaQuery.from(TrainingEntity.class);
        List<Predicate> predicates = new ArrayList<>();
        if (trainerUsername == null) {
            Join<TrainingEntity, TrainerEntity> trainerJoin = root.join("trainer", JoinType.LEFT);
            predicates.add(criteriaBuilder.equal(trainerJoin.get("user").get("username"), trainerUsername));
        }
        if (beginDate != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), beginDate));
        }
        if (endDate != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), endDate));
        }
        if (traineeName != null) {
            Join<TrainingEntity, TraineeEntity> traineeJoin = root.join("trainee", JoinType.LEFT);
            predicates.add(criteriaBuilder.like(criteriaBuilder.lower(traineeJoin.get("user").get("firstName")),
                    "%" + traineeName.toLowerCase() + "%"));
        }
        criteriaQuery.where(predicates.toArray(new Predicate[0]));
        log.info("Successfully executed the filter search for trainer's trainings in the database");
        return entityManager.createQuery(criteriaQuery).getResultList();
    }
}
