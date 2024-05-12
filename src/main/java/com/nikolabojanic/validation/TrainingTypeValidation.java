package com.nikolabojanic.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TrainingTypeValidation {

    public void validateIdNotNull(Long id) {
        if (id == null) {
            log.warn("Attempted to fetch training type with null id");
            throw new IllegalArgumentException("ID cannot be null");
        }
    }
}
