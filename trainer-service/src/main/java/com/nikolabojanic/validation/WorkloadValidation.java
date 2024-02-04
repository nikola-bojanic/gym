package com.nikolabojanic.validation;

import com.nikolabojanic.exception.TsValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class WorkloadValidation {
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
}
