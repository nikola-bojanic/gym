package com.nikolabojanic.repository;

import com.nikolabojanic.entity.TrainingTypeEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainingTypeRepository extends JpaRepository<TrainingTypeEntity, Long> {
    Optional<TrainingTypeEntity> findByName(String name);
}
