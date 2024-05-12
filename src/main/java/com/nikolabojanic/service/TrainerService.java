package com.nikolabojanic.service;

import com.nikolabojanic.dao.TrainerDAO;
import com.nikolabojanic.model.Trainer;
import com.nikolabojanic.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class TrainerService {
    private TrainerDAO trainerDAO;

    public Trainer create(Trainer trainer) {
        if (trainer.getId() != null) {
            log.warn("Attempted to create a trainer with an ID");
            throw new IllegalArgumentException("Cannot create a trainer with an ID");
        }
        return trainerDAO.save(trainer);
    }

    public Trainer update(Trainer trainer) {
        return findById(trainer.getId()) != null ? trainerDAO.save(trainer) : null;
    }

    public Trainer findById(Long id) {
        if (id == null) {
            log.warn("Attempted to fetch trainer with null ID");
            throw new IllegalArgumentException("ID cannot be null");
        }
        return trainerDAO.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attempted to fetch trainer with non-existent ID {}", id);
                    return new IllegalArgumentException("Trainer with ID: " + id + " doesn't exist");
                });
    }

    public List<User> getTrainerUsers() {
        List<User> trainers = new ArrayList<>();
        for (Trainer trainer : trainerDAO.getAll()) {
            trainers.add(trainer.getUser());
        }
        return trainers;
    }
}
