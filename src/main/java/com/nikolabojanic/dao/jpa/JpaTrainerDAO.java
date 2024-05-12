package com.nikolabojanic.dao.jpa;

import com.nikolabojanic.dao.TrainerDAO;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class JpaTrainerDAO implements TrainerDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TrainerEntity> findById(Long id) {
        TrainerEntity trainer = entityManager.find(TrainerEntity.class, id);
        return trainer == null ? Optional.empty() : Optional.of(trainer);
    }

    @Override
    public TrainerEntity save(TrainerEntity trainer) {
        UserEntity mergedUser = entityManager.merge(trainer.getUser());
        trainer.setUser(mergedUser);
        entityManager.merge(trainer);
        return trainer;
    }

    @Override
    public Optional<TrainerEntity> findByUserId(Long id) {
        try {
            TrainerEntity attachedTrainer = entityManager.
                    createQuery("from TrainerEntity where user.id = :userId", TrainerEntity.class)
                    .setParameter("userId", id)
                    .getSingleResult();
            return Optional.of(attachedTrainer);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<TrainerEntity> findActiveForOtherTrainees(Long traineeId) {
        try {
            return entityManager.createQuery(
                            "from TrainerEntity t where " +
                                    "t.user.isActive = true and " +
                                    "(t.id not in(select tt.id from TrainerEntity tt left join tt.trainees trainee WHERE trainee.id = :traineeId))",
                            TrainerEntity.class)
                    .setParameter("traineeId", traineeId)
                    .getResultList();
        } catch (NoResultException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public void changeActiveStatus(TrainerEntity trainer) {
        UserEntity user = entityManager.find(UserEntity.class, trainer.getUser().getId());
        user.setIsActive(!user.getIsActive());
        trainer.setUser(user);
    }
}
