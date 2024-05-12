package com.nikolabojanic.dao;

import com.nikolabojanic.model.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    List<UserEntity> getAll();

    Optional<UserEntity> findByUsername(String username);

    Long countUsersWithSameName(String firstName, String lastName);
}