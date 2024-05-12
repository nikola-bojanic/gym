package com.nikolabojanic.converter;

import com.nikolabojanic.dto.*;
import com.nikolabojanic.model.TrainerEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class TrainerConverter {
    private final UserConverter userConverter;
    private final TrainingTypeConverter trainingTypeConverter;
    private final TraineeConverter traineeConverter;

    public TrainerEntity convertUsernameToModel(String username) {
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(userConverter.convertUsernameToUser(username));
        return trainer;
    }

    public TrainerEntity convertRegistrationRequestToModel(TrainerRegistrationRequestDTO requestDTO) {
        TrainerEntity trainer = new TrainerEntity();
        trainer.setSpecialization(trainingTypeConverter.convertIdToModel(requestDTO.getSpecializationId()));
        trainer.setUser(userConverter.convertRegistrationRequest(requestDTO.getFirstName(), requestDTO.getLastName()));
        log.info("Successfully converted trainer registration request to model.");
        return trainer;
    }

    public RegistrationResponseDTO convertModelToRegistrationResponse(TrainerEntity trainer) {
        RegistrationResponseDTO responseDTO = new RegistrationResponseDTO();
        responseDTO.setUsername(trainer.getUser().getUsername());
        responseDTO.setPassword(trainer.getUser().getPassword());
        log.info("Successfully converted model to trainee registration response.");
        return responseDTO;
    }

    public TrainerResponseDTO convertModelToResponseDTO(TrainerEntity trainer) {
        TrainerResponseDTO responseDTO = new TrainerResponseDTO();
        responseDTO.setFirstName(trainer.getUser().getFirstName());
        responseDTO.setLastName(trainer.getUser().getLastName());
        responseDTO.setIsActive(trainer.getUser().getIsActive());
        if (trainer.getSpecialization() != null) {
            responseDTO.setSpecializationId(trainer.getSpecialization().getId());
        } else {
            responseDTO.setSpecializationId(null);
        }
        responseDTO.setTrainees(trainer.getTrainees().stream()
                .map(traineeConverter::convertModelToTrainerTrainee)
                .toList());
        log.info("Successfully converted trainer model to trainer response.");
        return responseDTO;
    }

    public TraineeTrainerResponseDTO convertModelToTraineeTrainer(TrainerEntity trainer) {
        TraineeTrainerResponseDTO responseDTO = new TraineeTrainerResponseDTO();
        responseDTO.setFirstName(trainer.getUser().getFirstName());
        responseDTO.setLastName(trainer.getUser().getLastName());
        responseDTO.setUsername(trainer.getUser().getUsername());
        if (trainer.getSpecialization() != null) {
            responseDTO.setSpecializationId(trainer.getSpecialization().getId());
        } else {
            responseDTO.setSpecializationId(null);
        }
        log.info("Successfully converted trainer model to trainee's trainer response.");
        return responseDTO;
    }

    public TrainerEntity convertUpdateRequestToModel(TrainerUpdateRequestDTO requestDTO) {
        TrainerEntity trainer = new TrainerEntity();
        trainer.setSpecialization(trainingTypeConverter.convertIdToModel(requestDTO.getSpecializationId()));
        trainer.setUser(userConverter.convertUpdateRequestToModel(
                requestDTO.getUsername(),
                requestDTO.getFirstName(),
                requestDTO.getLastName(),
                requestDTO.getIsActive()));
        log.info("Successfully converted trainee update request to model.");
        return trainer;
    }

    public TrainerUpdateResponseDTO convertModelToUpdateResponse(TrainerEntity trainer) {
        TrainerUpdateResponseDTO responseDTO = new TrainerUpdateResponseDTO();
        responseDTO.setUsername(trainer.getUser().getUsername());
        responseDTO.setFirstName(trainer.getUser().getFirstName());
        responseDTO.setLastName(trainer.getUser().getLastName());
        responseDTO.setIsActive(trainer.getUser().getIsActive());
        if (trainer.getSpecialization() != null) {
            responseDTO.setSpecializationId(trainer.getSpecialization().getId());
        } else {
            responseDTO.setSpecializationId(null);
        }
        responseDTO.setTrainees(trainer.getTrainees().stream()
                .map(traineeConverter::convertModelToTrainerTrainee)
                .toList());
        log.info("Successfully converted model to trainee update response.");
        return responseDTO;
    }

    public ActiveTrainerResponseDTO convertModelToActiveTrainerResponse(TrainerEntity trainer) {
        ActiveTrainerResponseDTO responseDTO = new ActiveTrainerResponseDTO();
        responseDTO.setFirstName(trainer.getUser().getFirstName());
        responseDTO.setLastName(trainer.getUser().getLastName());
        responseDTO.setUsername(trainer.getUser().getUsername());
        if (trainer.getSpecialization() != null) {
            responseDTO.setSpecializationId(trainer.getSpecialization().getId());
        } else {
            responseDTO.setSpecializationId(null);
        }
        log.info("Successfully converted trainer model to active trainer response.");
        return responseDTO;
    }
}
