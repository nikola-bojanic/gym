package com.nikolabojanic.service;

import com.nikolabojanic.dao.UserDAO;
import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.model.UserEntity;
import com.nikolabojanic.validation.UserValidation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserDAO userDAO;
    private final UserValidation userValidation;

    public UserEntity findByUsername(String username) {
        userValidation.validateUsername(username);
        Optional<UserEntity> exists = userDAO.findByUsername(username);
        return exists.orElseThrow(() -> {
            log.error("Attempted to fetch non-existing element with username: {}", username);
            return new NoSuchElementException("User with that username cannot be found");
        });
    }

    public void authentication(AuthDTO authDTO) {
        userValidation.validateAuthDto(authDTO);
        UserEntity domainModel = findByUsername(authDTO.getUsername());
        if (!authDTO.getPassword().equals(domainModel.getPassword()))
            throw new IllegalArgumentException("Passwords do not match");
    }

    public UserEntity generateUsernameAndPassword(UserEntity user) {
        userValidation.validateUserFields(user);
        Long count = userDAO.countUsersWithSameName(user.getFirstName(), user.getLastName());
        if (count == 0) {
            user.setUsername(user.getFirstName() + "." + user.getLastName());
        } else {
            user.setUsername(user.getFirstName() + "." + user.getLastName() + ++count);
        }
        user.setPassword(generateRandomPassword());
        log.info("Generated username and password for user.");
        return user;
    }

    private String generateRandomPassword() {
        SecureRandom secureRandom = new SecureRandom();
        int[] codePoints = secureRandom.ints(10, 33, 126).toArray();
        return new String(codePoints, 0, codePoints.length);
    }
}