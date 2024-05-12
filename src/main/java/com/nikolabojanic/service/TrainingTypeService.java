package com.nikolabojanic.service;

import com.nikolabojanic.dao.TrainingTypeDAO;
import com.nikolabojanic.exception.SCEntityNotFoundException;
import com.nikolabojanic.model.TrainingTypeEntity;
import com.nikolabojanic.validation.TrainingTypeValidation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class TrainingTypeService {
    private final TrainingTypeDAO trainingTypeDAO;
    private final TrainingTypeValidation trainingTypeValidation;

    public List<TrainingTypeEntity> getAll() {
        return trainingTypeDAO.getAll();
    }

    public TrainingTypeEntity findById(Long id) {
        trainingTypeValidation.validateIdNotNull(id);
        Optional<TrainingTypeEntity> exists = trainingTypeDAO.findById(id);
        if (exists.isPresent()) {
            log.info("Successfully retrieved training type with id {}.", id);
            return exists.get();
        } else {
            log.error("Attempted to fetch training type with non-existent id {}." +
                    " Status: {}", id, HttpStatus.NOT_FOUND.value());
            throw new SCEntityNotFoundException("TrainingType with id " + id + " doesn't exist");
        }
    }
}
