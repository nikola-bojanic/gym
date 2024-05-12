package com.nikolabojanic.dao.jpa;

import com.nikolabojanic.dao.TrainingTypeDAO;
import com.nikolabojanic.model.TrainingTypeEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class JpaTrainingTypeDAO implements TrainingTypeDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TrainingTypeEntity> findById(Long id) {
        TrainingTypeEntity type = entityManager.find(TrainingTypeEntity.class, id);
        return type == null ? Optional.empty() : Optional.of(type);
    }
}
