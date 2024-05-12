package com.nikolabojanic.dao.jpa;

import com.nikolabojanic.dao.TrainerDAO;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class JpaTrainerDAO implements TrainerDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public TrainerEntity save(TrainerEntity trainer) {
        UserEntity mergedUser = entityManager.merge(trainer.getUser());
        trainer.setUser(mergedUser);
        entityManager.merge(trainer);
        log.info("Wrote changes to trainer with username {} to database", trainer.getUser().getUsername());
        return trainer;
    }

    @Override
    public Optional<TrainerEntity> findByUsername(String username) {
        try {
            TrainerEntity attachedTrainer = entityManager.
                    createQuery("select t from TrainerEntity t where t.user.username " +
                            "= :username", TrainerEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();
            Hibernate.initialize(attachedTrainer.getTrainees());
            log.info("Retrieved trainer with username {} from database", username);
            return Optional.of(attachedTrainer);
        } catch (NoResultException e) {
            log.error("Trainer with username {} doesn't exist in the database", username);
            return Optional.empty();
        }
    }

    @Override
    public List<TrainerEntity> findActiveForOtherTrainees(Long traineeId) {
        try {
            List<TrainerEntity> trainers = entityManager.createQuery(
                            "from TrainerEntity t where " +
                                    "t.user.isActive = true and " +
                                    "(t.id not in(select tt.id from TrainerEntity tt left join tt.trainees trainee" +
                                    " WHERE trainee.id = :traineeId))",
                            TrainerEntity.class)
                    .setParameter("traineeId", traineeId)
                    .getResultList();
            log.info("Retrieved active trainers for other trainees. Trainee id: {}", traineeId);
            return trainers;
        } catch (NoResultException e) {
            log.info("Found no active trainers for other trainees. Trainee id: {}", traineeId);
            return new ArrayList<>();
        }
    }
}
