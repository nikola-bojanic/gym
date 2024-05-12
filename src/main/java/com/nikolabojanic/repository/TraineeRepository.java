package com.nikolabojanic.repository;

import com.nikolabojanic.entity.TraineeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TraineeRepository extends JpaRepository<TraineeEntity, Long> {
    @Query("select t from TraineeEntity t left join fetch t.trainers where t.user.username = :username")
    Optional<TraineeEntity> findByUsername(String username);

    @Modifying
    @Query(value = "delete from TraineeEntity t where t.user.username = :username")
    void deleteByUsername(String username);
}
