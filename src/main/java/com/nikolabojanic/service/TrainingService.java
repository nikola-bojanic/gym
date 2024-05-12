package com.nikolabojanic.service;

import com.nikolabojanic.dao.TrainingDAO;
import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.model.TrainingEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Transactional
@Service
public class TrainingService {
    private final TrainingDAO trainingDAO;
    private final TrainingTypeService trainingTypeService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
    private final UserService userService;

    public TrainingEntity create(AuthDTO authDTO, TrainingEntity training) {
        userService.authentication(authDTO);
        if (training.getTrainer() != null && training.getTrainer().getUser() != null
                && training.getTrainer().getUser().getUsername() != null) {
            training.setTrainer(trainerService.findByUsername(authDTO, training.getTrainer().getUser().getUsername()));
        }
        if (training.getTrainee() != null && training.getTrainee().getUser() != null
                && training.getTrainee().getUser().getUsername() != null) {
            training.setTrainee(traineeService.findByUsername(authDTO, training.getTrainee().getUser().getUsername()));
        }
        if (training.getType() != null) {
            training.setType(trainingTypeService.findById(training.getType().getId()));
        }
        log.info("Created a new training");
        return trainingDAO.save(training);
    }

    public List<TrainingEntity> findByTraineeAndFilter(
            AuthDTO authDTO,
            String username,
            LocalDate begin,
            LocalDate end,
            String trainerName,
            Long typeId) {
        userService.authentication(authDTO);
        log.info("Retrieved trainings by trainee username and filter.");
        return trainingDAO.findByTraineeAndFilter(username, begin, end, trainerName, typeId);
    }

    public List<TrainingEntity> findByTrainerAndFilter(
            AuthDTO authDTO,
            String username,
            LocalDate begin,
            LocalDate end,
            String traineeName) {
        userService.authentication(authDTO);
        log.info("Retrieved trainings by trainer username and filter.");
        return trainingDAO.findByTrainerAndFilter(username, begin, end, traineeName);
    }
}
