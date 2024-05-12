package com.nikolabojanic.dao;

import com.nikolabojanic.model.UserEntity;

import java.util.Optional;

public interface UserDAO {

    Optional<UserEntity> findByUsername(String username);

    Long countUsersWithSameName(String firstName, String lastName);

    UserEntity save(UserEntity user);
}