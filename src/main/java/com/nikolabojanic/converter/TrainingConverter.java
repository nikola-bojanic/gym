package com.nikolabojanic.converter;

import com.nikolabojanic.dto.TraineeTrainingResponseDTO;
import com.nikolabojanic.dto.TrainerTrainingResponseDTO;
import com.nikolabojanic.dto.TrainingRequestDTO;
import com.nikolabojanic.dto.TrainingResponseDTO;
import com.nikolabojanic.model.TrainingEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class TrainingConverter {
    private final TraineeConverter traineeConverter;
    private final TrainerConverter trainerConverter;

    public TrainingEntity convertToEntity(TrainingRequestDTO dto) {
        TrainingEntity domainModel = new TrainingEntity();
        domainModel.setTrainer(trainerConverter.convertUsernameToModel(dto.getTrainerUsername()));
        domainModel.setTrainee(traineeConverter.convertUsernameToModel(dto.getTraineeUsername()));
        domainModel.setName(dto.getName());
        domainModel.setDate(dto.getDate());
        domainModel.setDuration(dto.getDuration());
        log.info("Successfully converted training request to model.");
        return domainModel;
    }

    public TrainingResponseDTO convertToDto(TrainingEntity domainModel) {
        TrainingResponseDTO dto = new TrainingResponseDTO();
        dto.setName(domainModel.getName());
        dto.setDuration(domainModel.getDuration());
        dto.setDate(domainModel.getDate());
        dto.setTrainerUsername(domainModel.getTrainer().getUser().getUsername());
        dto.setTraineeUsername(domainModel.getTrainee().getUser().getUsername());
        log.info("Successfully converted training model to training response.");
        return dto;
    }

    public TrainerTrainingResponseDTO convertModelToTrainerTraining(TrainingEntity training) {
        TrainerTrainingResponseDTO responseDTO = new TrainerTrainingResponseDTO();
        responseDTO.setDate(training.getDate());
        responseDTO.setName(training.getName());
        responseDTO.setDuration(training.getDuration());
        if (training.getTrainee() != null && training.getTrainee().getUser() != null) {
            responseDTO.setTraineeName(training.getTrainee().getUser().getFirstName());
        }
        if (training.getType() != null) {
            responseDTO.setTrainingTypeId(training.getType().getId());
        }
        log.info("Successfully converted training model to trainer's training response.");
        return responseDTO;
    }

    public TraineeTrainingResponseDTO convertModelToTraineeTraining(TrainingEntity training) {
        TraineeTrainingResponseDTO responseDTO = new TraineeTrainingResponseDTO();
        responseDTO.setDate(training.getDate());
        responseDTO.setName(training.getName());
        responseDTO.setDuration(training.getDuration());
        if (training.getTrainer() != null && training.getTrainer().getUser() != null) {
            responseDTO.setTrainerName(training.getTrainer().getUser().getFirstName());
        }
        if (training.getType() != null) {
            responseDTO.setTrainingTypeId(training.getType().getId());
        }
        log.info("Successfully converted training model to trainee's training response.");
        return responseDTO;
    }


}
