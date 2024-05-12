package com.nikolabojanic.validation;

import com.nikolabojanic.exception.TsValidationException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainerValidation {
    /**
     * Validates request username.
     *
     * @param username {@link String} Request username to be validated.
     */
    public void validateUsernameNotNull(String username) {
        if (username == null || username.isBlank() || username.length() < 4) {
            log.error("Attempted to fetch trainee with bad path variable username. Status: {}", HttpStatus.BAD_REQUEST);
            throw new TsValidationException("Username must be at least 4 characters long");
        }
    }

    /**
     * Validates the provided first and last names for fetching trainers, ensuring they meet the required criteria.
     *
     * @param firstName The first name to be validated.
     * @param lastName  The last name to be validated.
     * @throws TsValidationException If validation fails, an exception is thrown with corresponding messages.
     */

    public void validateFirstAndLastName(String firstName, String lastName) {
        List<String> message = new ArrayList<>();
        if (firstName == null || firstName.isBlank() || firstName.length() < 2) {
            log.error("Attempted to fetch trainers with bad first name. Status: {}", HttpStatus.BAD_REQUEST);
            message.add("First name must be at least 2 characters long");
        }
        if (lastName == null || lastName.isBlank() || lastName.length() < 2) {
            log.error("Attempted to fetch trainers with bad last name. Status: {}", HttpStatus.BAD_REQUEST);
            message.add("Last name must be at least 2 characters long");
        }
        if (!message.isEmpty()) {
            throw new TsValidationException(message.toString());
        }
    }
}
