package com.nikolabojanic.service;

import com.nikolabojanic.dto.UserPasswordChangeRequestDto;
import com.nikolabojanic.entity.UserEntity;
import com.nikolabojanic.exception.ScEntityNotFoundException;
import com.nikolabojanic.repository.UserRepository;
import io.micrometer.core.instrument.Counter;
import java.security.SecureRandom;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final Counter totalTransactionsCounter;
    private final PasswordEncoder passwordEncoder;

    /**
     * Service class responsible for managing user-related operations.
     *
     * @param userRepository           The repository for accessing user data.
     * @param totalTransactionsCounter The counter for tracking total transactions.
     * @param passwordEncoder          The password encoder for encoding user passwords.
     */
    public UserService(
        UserRepository userRepository,
        Counter totalTransactionsCounter,
        @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.totalTransactionsCounter = totalTransactionsCounter;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Finds a user by their username.
     *
     * @param username The username of the user to find.
     * @return The UserEntity associated with the provided username.
     * @throws ScEntityNotFoundException If the user with the specified username is not found.
     */
    public UserEntity findByUsername(String username) {
        totalTransactionsCounter.increment();
        Optional<UserEntity> exists = userRepository.findByUsername(username);
        if (exists.isPresent()) {
            log.info("Successfully retrieved user with username {}.", username);
            return exists.get();
        } else {
            log.error("Attempted to fetch user with non-existent username {}. "
                + "Status: {}", username, HttpStatus.NOT_FOUND.value());
            throw new ScEntityNotFoundException("User with username " + username + " doesn't exist");
        }
    }

    /**
     * Changes the password of a user.
     *
     * @param username   The username of the user whose password needs to be changed.
     * @param requestDto The UserPasswordChangeRequestDto containing information about the password change.
     * @return The updated UserEntity after changing the password.
     * @throws ScEntityNotFoundException If the user with the specified username is not found.
     */
    public UserEntity changeUserPassword(String username, UserPasswordChangeRequestDto requestDto) {
        UserEntity user = findByUsername(username);
        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        log.warn("Changed password for user with username {}", username);
        totalTransactionsCounter.increment();
        return userRepository.save(user);
    }

    /**
     * Generates a username and password for a user based on their first and last names.
     *
     * @param user The UserEntity for which to generate the username and password.
     * @return The updated UserEntity with generated username and password.
     */
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

    /**
     * Generates a random password of length 10.
     *
     * @return The generated random password.
     */
    private String generateRandomPassword() {
        SecureRandom secureRandom = new SecureRandom();
        int[] codePoints = secureRandom.ints(10, 33, 126).toArray();
        return new String(codePoints, 0, codePoints.length);
    }
}