package com.nikolabojanic.service;

import com.nikolabojanic.dao.TrainingTypeDAO;
import com.nikolabojanic.model.TrainingTypeEntity;
import com.nikolabojanic.validation.TrainingTypeValidation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
@AllArgsConstructor
public class TrainingTypeService {
    private final TrainingTypeDAO trainingTypeDAO;
    private final TrainingTypeValidation trainingTypeValidation;

    public TrainingTypeEntity findById(Long id) {
        trainingTypeValidation.validateIdNotNull(id);
        return trainingTypeDAO.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attempted to fetch training type with non-existent ID {}", id);
                    return new NoSuchElementException("Training type with ID " + id + " doesn't exist");
                });
    }
}
