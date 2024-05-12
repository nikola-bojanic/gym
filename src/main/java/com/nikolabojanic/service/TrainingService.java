package com.nikolabojanic.service;

import com.nikolabojanic.dao.TrainingDAO;
import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.TrainingEntity;
import com.nikolabojanic.validation.TrainingValidation;
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
    private final TrainingValidation trainingValidation;

    public TrainingEntity create(TrainingEntity training) {
        trainingValidation.validateCreateTrainingRequest(training);
        training.setType(trainingTypeService.findById(training.getType().getId()));
        log.info("Created a new training");
        return trainingDAO.save(training);
    }

    public List<TrainingEntity> findTrainingsByTraineeUsernameAndTime(AuthDTO authDTO, String username, LocalDate begin, LocalDate end) {
        userService.authentication(authDTO);
        trainingValidation.validateDate(begin, end);
        TraineeEntity trainee = traineeService.findByUsername(authDTO, username);
        return trainingDAO.findByTraineeIdAndDate(trainee.getId(), begin, end);
    }

    public List<TrainingEntity> findTrainingsByTrainerUsernameAndTime(AuthDTO authDTO, String username, LocalDate begin, LocalDate end) {
        userService.authentication(authDTO);
        trainingValidation.validateDate(begin, end);
        TrainerEntity trainer = trainerService.findByUsername(authDTO, username);
        return trainingDAO.findByTrainerIdAndDate(trainer.getId(), begin, end);
    }

}
