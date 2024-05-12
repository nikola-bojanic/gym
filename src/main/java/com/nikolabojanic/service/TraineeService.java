package com.nikolabojanic.service;

import com.nikolabojanic.dao.TraineeDAO;
import com.nikolabojanic.model.Trainee;
import com.nikolabojanic.model.User;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class TraineeService {
    private TraineeDAO traineeDAO;

    public Trainee create(Trainee trainee) {
        if (trainee.getId() != null) {
            log.warn("Attempted to create trainee with an existing ID");
            throw new IllegalArgumentException("Cannot create a training with an ID");
        }
        return traineeDAO.save(trainee);
    }

    public Trainee update(Trainee trainee) {
        return findById(trainee.getId()) != null ? traineeDAO.save(trainee) : null;
    }

    public void delete(Long id) {
        if (findById(id) != null) {
            traineeDAO.delete(id);
        }
    }

    public Trainee findById(Long id) {
        if (id == null) {
            log.warn("Attempted to fetch trainee with null id");
            throw new IllegalArgumentException("ID cannot be null");
        }
        return traineeDAO.findById(id)
                .orElseThrow(() -> {
                    log.warn("Attempted to fetch trainee with non-existent ID {}", id);
                    return new IllegalArgumentException("Trainee with ID " + id + " doesn't exist");
                });
    }

    public List<User> getTraineeUsers() {
        List<User> trainees = new ArrayList<>();
        for (Trainee trainee : traineeDAO.getAll()) {
            trainees.add(trainee.getUser());
        }
        return trainees;
    }
}
