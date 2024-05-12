package com.nikolabojanic.dao.jpa;

import com.nikolabojanic.dao.TraineeDAO;
import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class JpaTraineeDAO implements TraineeDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TraineeEntity> deleteByUsername(String username) {
        try {
            TraineeEntity trainee = entityManager
                    .createQuery("select t from TraineeEntity t where t.user.username" +
                            " = :username", TraineeEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();
            entityManager.remove(trainee);
            log.warn("Deleted trainee with username {} from database", username);
            return Optional.of(trainee);
        } catch (NoResultException e) {
            log.error("Trainee with username {} does not exist in the database", username);
            return Optional.empty();
        }
    }

    @Override
    public TraineeEntity save(TraineeEntity trainee) {
        UserEntity mergedUser = entityManager.merge(trainee.getUser());
        trainee.setUser(mergedUser);
        entityManager.merge(trainee);
        log.info("Wrote changes to trainee with username {} to database", trainee.getUser().getUsername());
        return trainee;
    }

    @Override
    public Optional<TraineeEntity> findByUsername(String username) {
        try {
            TraineeEntity attachedTrainee = entityManager.
                    createQuery("select t from TraineeEntity t where t.user.username " +
                            "= :username", TraineeEntity.class)
                    .setParameter("username", username)
                    .getSingleResult();
            Hibernate.initialize(attachedTrainee.getTrainers());
            log.info("Retrieved trainee with username {} from database", username);
            return Optional.of(attachedTrainee);
        } catch (NoResultException e) {
            log.error("Trainee with username {} doesn't exist in the database", username);
            return Optional.empty();
        }
    }

    @Override
    public List<TrainerEntity> saveTrainers(TraineeEntity trainee, List<TrainerEntity> trainers) {
        trainee.setTrainers(trainers);
        entityManager.merge(trainee);
        log.info("Wrote changes to trainee's trainer list to database. Username: {}", trainee.getUser().getUsername());
        return trainee.getTrainers();
    }
}
