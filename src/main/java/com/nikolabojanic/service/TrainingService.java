package com.nikolabojanic.service;

import com.nikolabojanic.dao.TrainingDAO;
import com.nikolabojanic.model.Training;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class TrainingService {
    private TrainingDAO trainingDAO;

    public Training create(Training training) {
        if (training.getId() != null) {
            log.warn("Attempted to create training with an existing ID");
            throw new IllegalArgumentException("Cannot create a training with an ID");
        }
        return trainingDAO.save(training);
    }

    public Training findById(Long id) {
        if (id == null) {
            log.warn("Attempted to fetch training with null ID");
            throw new IllegalArgumentException("ID cannot be null");
        }
        return trainingDAO.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attempted to fetch training with non-existent ID {}", id);
                    return new IllegalArgumentException("Training with ID:" + id + " doesn't exist");
                });
    }
}
