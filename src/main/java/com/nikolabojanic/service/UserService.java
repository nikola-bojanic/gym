package com.nikolabojanic.service;

import com.nikolabojanic.dto.UserPasswordChangeRequestDTO;
import com.nikolabojanic.entity.UserEntity;
import com.nikolabojanic.exception.ScEntityNotFoundException;
import com.nikolabojanic.repository.UserRepository;
import io.micrometer.core.instrument.Counter;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Optional;

@Service
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final Counter totalTransactionsCounter;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       Counter totalTransactionsCounter,
                       @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.totalTransactionsCounter = totalTransactionsCounter;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity findByUsername(String username) {
        totalTransactionsCounter.increment();
        Optional<UserEntity> exists = userRepository.findByUsername(username);
        if (exists.isPresent()) {
            log.info("Successfully retrieved user with username {}.", username);
            return exists.get();
        } else {
            log.error("Attempted to fetch user with non-existent username {}. " +
                    "Status: {}", username, HttpStatus.NOT_FOUND.value());
            throw new ScEntityNotFoundException("User with username " + username + " doesn't exist");
        }
    }

    public UserEntity changeUserPassword(String username, UserPasswordChangeRequestDTO requestDTO) {
        UserEntity user = findByUsername(username);
        user.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));
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