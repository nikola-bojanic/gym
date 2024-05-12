package com.nikolabojanic.validation;

import com.nikolabojanic.exception.SCValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrainingTypeValidation {

    public void validateIdNotNull(Long id) {
        if (id == null) {
            log.warn("Attempted to fetch training type with null id. Status: {}", HttpStatus.BAD_REQUEST.value());
            throw new SCValidationException("Training type ID cannot be null");
        }
    }
}
