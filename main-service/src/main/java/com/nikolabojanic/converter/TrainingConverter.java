package com.nikolabojanic.converter;

import com.nikolabojanic.dto.TraineeTrainingResponseDto;
import com.nikolabojanic.dto.TrainerTrainingResponseDto;
import com.nikolabojanic.dto.TrainingRequestDto;
import com.nikolabojanic.dto.TrainingResponseDto;
import com.nikolabojanic.entity.TrainingEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class TrainingConverter {
    private final TraineeConverter traineeConverter;
    private final TrainerConverter trainerConverter;

    /**
     * Convert a TrainingRequestDto to a TrainingEntity model.
     *
     * @param dto The TrainingRequestDto to be converted.
     * @return TrainingEntity representing the converted model.
     */
    public TrainingEntity convertToEntity(TrainingRequestDto dto) {
        TrainingEntity domainModel = new TrainingEntity();
        domainModel.setTrainer(trainerConverter.convertUsernameToModel(dto.getTrainerUsername()));
        domainModel.setTrainee(traineeConverter.convertUsernameToModel(dto.getTraineeUsername()));
        domainModel.setName(dto.getName());
        domainModel.setDate(dto.getDate());
        domainModel.setDuration(dto.getDuration());
        log.info("Successfully converted training request to model.");
        return domainModel;
    }

    /**
     * Convert a TrainingEntity to a TrainingResponseDto.
     *
     * @param domainModel The TrainingEntity to be converted.
     * @return TrainingResponseDto representing the converted response.
     */
    public TrainingResponseDto convertToDto(TrainingEntity domainModel) {
        TrainingResponseDto dto = new TrainingResponseDto();
        dto.setId(domainModel.getId());
        dto.setName(domainModel.getName());
        dto.setDuration(domainModel.getDuration());
        dto.setDate(domainModel.getDate());
        dto.setTrainerUsername(domainModel.getTrainer().getUser().getUsername());
        dto.setTraineeUsername(domainModel.getTrainee().getUser().getUsername());
        log.info("Successfully converted training model to training response.");
        return dto;
    }

    /**
     * Convert a TrainingEntity to a TrainerTrainingResponseDto.
     *
     * @param training The TrainingEntity to be converted.
     * @return TrainerTrainingResponseDto representing the converted response.
     */
    public TrainerTrainingResponseDto convertModelToTrainerTraining(TrainingEntity training) {
        TrainerTrainingResponseDto responseDto = new TrainerTrainingResponseDto();
        responseDto.setDate(training.getDate());
        responseDto.setName(training.getName());
        responseDto.setDuration(training.getDuration());
        if (training.getTrainee() != null && training.getTrainee().getUser() != null) {
            responseDto.setTraineeName(training.getTrainee().getUser().getFirstName());
        }
        if (training.getType() != null) {
            responseDto.setTrainingTypeId(training.getType().getId());
        }
        log.info("Successfully converted training model to trainer's training response.");
        return responseDto;
    }

    /**
     * Convert a TrainingEntity to a TraineeTrainingResponseDto.
     *
     * @param training The TrainingEntity to be converted.
     * @return TraineeTrainingResponseDto representing the converted response.
     */
    public TraineeTrainingResponseDto convertModelToTraineeTraining(TrainingEntity training) {
        TraineeTrainingResponseDto responseDto = new TraineeTrainingResponseDto();
        responseDto.setDate(training.getDate());
        responseDto.setName(training.getName());
        responseDto.setDuration(training.getDuration());
        if (training.getTrainer() != null && training.getTrainer().getUser() != null) {
            responseDto.setTrainerName(training.getTrainer().getUser().getFirstName());
        }
        if (training.getType() != null) {
            responseDto.setTrainingTypeId(training.getType().getId());
        }
        log.info("Successfully converted training model to trainee's training response.");
        return responseDto;
    }


}
