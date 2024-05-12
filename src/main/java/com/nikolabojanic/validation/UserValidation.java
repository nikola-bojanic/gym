package com.nikolabojanic.validation;

import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.model.UserEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserValidation {
    public void validateUserFields(UserEntity user) {
        if (user == null) {
            log.error("Attempted to create/update user profile with null user");
            throw new IllegalArgumentException("Cannot create/update user profile with null value");
        } else if (user.getFirstName() == null || user.getFirstName().isBlank() || user.getFirstName().length() < 2) {
            log.error("Attempted to create/update user profile with missing first name");
            throw new IllegalArgumentException("Cannot create/update user profile without a first name");
        } else if (user.getLastName() == null || user.getLastName().isBlank() || user.getLastName().length() < 2) {
            log.error("Attempted to create/update user profile with missing last name");
            throw new IllegalArgumentException("Cannot create/update user profile without a last name");
        } else if (user.getIsActive() == null) {
            log.error("Attempted to create/update user profile with missing active status");
            throw new IllegalArgumentException("Cannot create/update user profile without an active status");
        }
    }

    public void validateUsernameAndPassword(String username, String password) {
        if (username == null || username.length() < 4 || username.isBlank()) {
            log.error("Attempted to change user password with bad username");
            throw new IllegalArgumentException("Username must be at least 4 characters long");
        } else if (password == null || password.length() < 8 || password.isBlank()) {
            log.error("Attempted to change user password with bad username");
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
    }

    public void validateAuthDto(AuthDTO authDTO) {
        if (authDTO == null) {
            log.error("Attempted to log in with null");
            throw new IllegalArgumentException("Login request cannot be null");
        }
    }

    public void validateUsername(String username) {
        if (username == null) {
            log.error("Attempted to fetch user with null username");
            throw new IllegalArgumentException("Username cannot be null");
        }
    }

    public void validateUserPermissionToEdit(String username, String domainUsername) {
        if (!username.equals(domainUsername)) {
            log.error("Attempted to change other trainee's profile");
            throw new IllegalArgumentException("No hacking allowed!");
        }
    }
}
