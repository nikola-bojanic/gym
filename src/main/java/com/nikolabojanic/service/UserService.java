package com.nikolabojanic.service;

import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.dto.UserPasswordChangeRequestDTO;
import com.nikolabojanic.exception.SCAuthenticationException;
import com.nikolabojanic.exception.SCEntityNotFoundException;
import com.nikolabojanic.model.UserEntity;
import com.nikolabojanic.repository.UserRepository;
import com.nikolabojanic.validation.UserValidation;
import io.micrometer.core.instrument.Counter;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserValidation userValidation;
    private final Counter totalTransactionsCounter;

    public UserEntity findByUsername(String username) {
        totalTransactionsCounter.increment();
        Optional<UserEntity> exists = userRepository.findByUsername(username);
        if (exists.isPresent()) {
            log.info("Successfully retrieved user with username {}.", username);
            return exists.get();
        } else {
            log.error("Attempted to fetch user with non-existent username {}. " +
                    "Status: {}", username, HttpStatus.NOT_FOUND.value());
            throw new SCEntityNotFoundException("User with username " + username + " doesn't exist");
        }
    }

    public void authentication(AuthDTO authDTO) {
        userValidation.validateAuthDto(authDTO);
        totalTransactionsCounter.increment();
        Optional<UserEntity> exists = userRepository.findByUsername(authDTO.getUsername());
        if (exists.isEmpty()) {
            log.error("Attempted to login with non-existing username: {}," +
                    " Status: {}", authDTO.getUsername(), HttpStatus.UNAUTHORIZED.value());
            throw new SCAuthenticationException("Provided authentication username doesn't exist");
        }
        UserEntity domainModel = exists.get();
        if (!authDTO.getPassword().equals(domainModel.getPassword())) {
            log.error("Attempted to login with bad password. Status: {}", HttpStatus.UNAUTHORIZED.value());
            throw new SCAuthenticationException("Passwords do not match");
        }
    }

    public UserEntity changeUserPassword(String username, UserPasswordChangeRequestDTO requestDTO) {
        UserEntity user = findByUsername(username);
        user.setPassword(requestDTO.getNewPassword());
        log.warn("Changed password for user with username {}", username);
        totalTransactionsCounter.increment();
        return userRepository.save(user);
    }

    public UserEntity generateUsernameAndPassword(UserEntity user) {
        totalTransactionsCounter.increment();
        Long count = userRepository.countUsersWithSameName(user.getFirstName(), user.getLastName());
        if (count == 0) {
            user.setUsername(user.getFirstName() + "." + user.getLastName());
        } else {
            user.setUsername(user.getFirstName() + "." + user.getLastName() + ++count);
        }
        user.setPassword(generateRandomPassword());
        log.info("Generated username and password for user. Username: {}", user.getUsername());
        return user;
    }

    private String generateRandomPassword() {
        SecureRandom secureRandom = new SecureRandom();
        int[] codePoints = secureRandom.ints(10, 33, 126).toArray();
        return new String(codePoints, 0, codePoints.length);
    }
}