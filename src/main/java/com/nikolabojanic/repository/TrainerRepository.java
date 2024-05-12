package com.nikolabojanic.repository;

import com.nikolabojanic.entity.TrainerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TrainerRepository extends JpaRepository<TrainerEntity, Long> {
    @Query("select t from TrainerEntity t left join fetch t.trainees where t.user.username = :username")
    Optional<TrainerEntity> findByUsername(String username);

    @Query("from TrainerEntity t where " +
            "t.user.isActive = true and " +
            "(t.id not in(select tt.id from TrainerEntity tt left join tt.trainees trainee" +
            " WHERE trainee.id = :traineeId))")
    List<TrainerEntity> findActiveForOtherTrainees(Long traineeId);
}
