package com.nikolabojanic.dao.jpa;

import com.nikolabojanic.dao.TraineeDAO;
import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaTraineeDAO implements TraineeDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TraineeEntity> findById(Long id) {
        TraineeEntity trainee = entityManager.find(TraineeEntity.class, id);
        return trainee == null ? Optional.empty() : Optional.of(trainee);
    }

    @Override
    public void delete(Long id) {
        TraineeEntity trainee = entityManager.find(TraineeEntity.class, id);
        entityManager.remove(trainee);
    }

    @Override
    public TraineeEntity save(TraineeEntity trainee) {
        UserEntity mergedUser = entityManager.merge(trainee.getUser());
        trainee.setUser(mergedUser);
        entityManager.merge(trainee);
        return trainee;
    }

    @Override
    public Optional<TraineeEntity> findByUserId(Long id) {
        try {
            TraineeEntity attachedTrainee = entityManager.
                    createQuery("from TraineeEntity where user.id = :userId", TraineeEntity.class)
                    .setParameter("userId", id)
                    .getSingleResult();
            return Optional.of(attachedTrainee);
        } catch (NoResultException e) {
            return Optional.empty();
        }

    }

    @Override
    public TraineeEntity saveTrainers(TraineeEntity trainee, List<TrainerEntity> trainers) {
        trainee.setTrainers(trainers);
        entityManager.merge(trainee);
        return trainee;
    }

    @Override
    public void changeActiveStatus(TraineeEntity trainee) {
        UserEntity user = entityManager.find(UserEntity.class, trainee.getUser().getId());
        user.setIsActive(!user.getIsActive());
    }
}
