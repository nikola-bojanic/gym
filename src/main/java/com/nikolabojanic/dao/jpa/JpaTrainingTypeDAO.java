package com.nikolabojanic.dao.jpa;

import com.nikolabojanic.dao.TrainingTypeDAO;
import com.nikolabojanic.model.TrainingTypeEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
public class JpaTrainingTypeDAO implements TrainingTypeDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<TrainingTypeEntity> findById(Long id) {
        TrainingTypeEntity type = entityManager.find(TrainingTypeEntity.class, id);
        if (type == null) {
            log.error("Training type with id {} doesn't exist in the database", id);
            return Optional.empty();
        } else {
            log.info("Retrieved training type with id {} from the database", id);
            return Optional.of(type);
        }
    }

    @Override
    public List<TrainingTypeEntity> getAll() {
        log.info("Retrieved training types from the database");
        return entityManager.createQuery("from TrainingTypeEntity", TrainingTypeEntity.class).getResultList();
    }
}
