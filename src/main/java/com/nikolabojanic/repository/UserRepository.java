package com.nikolabojanic.repository;

import com.nikolabojanic.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);

    @Query("select coalesce(count(u), 0) from UserEntity u where u.firstName = :firstName and u.lastName = :lastName")
    Long countUsersWithSameName(String firstName, String lastName);
}
