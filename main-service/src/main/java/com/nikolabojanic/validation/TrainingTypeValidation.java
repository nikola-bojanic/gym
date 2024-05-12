package com.nikolabojanic.validation;

import com.nikolabojanic.exception.ScValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainingTypeValidation {
    /**
     * Validates the provided training type name, ensuring that it is not null, blank, and has a minimum length of 4 characters.
     *
     * @param name The name to validate.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validateUsername(String name) {
        if (name == null || name.isBlank() || name.length() < 2) {
            log.error("Attempted to create a training type with a bad name. Status: {}",
                HttpStatus.BAD_REQUEST.value());
            throw new ScValidationException("Training type name must be at least 2 characters long");
        }
    }
}