package com.nikolabojanic.repository;

import com.nikolabojanic.entity.TrainerEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends JpaRepository<TrainerEntity, Long> {
    @Query("select t from TrainerEntity t where t.username = :username")
    Optional<TrainerEntity> findByUsername(String username);
}
