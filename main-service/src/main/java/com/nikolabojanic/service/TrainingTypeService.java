package com.nikolabojanic.service;

import com.nikolabojanic.entity.TrainingTypeEntity;
import com.nikolabojanic.exception.ScEntityNotFoundException;
import com.nikolabojanic.repository.TrainingTypeRepository;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class TrainingTypeService {
    private final TrainingTypeRepository trainingTypeRepository;

    /**
     * Retrieves a list of all training types.
     *
     * @return List of TrainingTypeEntity representing all training types.
     */
    public List<TrainingTypeEntity> getAll() {
        return trainingTypeRepository.findAll();
    }

    /**
     * Finds a training type by its ID.
     *
     * @param id The ID of the training type to find.
     * @return The TrainingTypeEntity associated with the provided ID.
     * @throws ScEntityNotFoundException If the training type with the specified ID is not found.
     */
    public TrainingTypeEntity findById(Long id) {
        Optional<TrainingTypeEntity> exists = trainingTypeRepository.findById(id);
        if (exists.isPresent()) {
            log.info("Successfully retrieved training type with id {}.", id);
            return exists.get();
        } else {
            log.error("Attempted to fetch training type with non-existent id {}."
                + " Status: {}", id, HttpStatus.NOT_FOUND.value());
            throw new ScEntityNotFoundException("TrainingType with id " + id + " doesn't exist");
        }
    }
}
