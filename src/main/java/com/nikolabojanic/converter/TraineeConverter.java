package com.nikolabojanic.converter;

import com.nikolabojanic.dto.*;
import com.nikolabojanic.model.TraineeEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TraineeConverter {
    private final UserConverter userConverter;
    private final TrainerConverter trainerConverter;

    @Autowired
    public TraineeConverter(UserConverter userConverter, @Lazy TrainerConverter trainerConverter) {
        this.userConverter = userConverter;
        this.trainerConverter = trainerConverter;
    }

    public TraineeEntity convertUsernameToModel(String username) {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(userConverter.convertUsernameToUser(username));
        return trainee;
    }

    public TraineeEntity convertRegistrationRequestToModel(TraineeRegistrationRequestDTO requestDTO) {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setAddress(requestDTO.getAddress());
        trainee.setDateOfBirth(requestDTO.getDateOfBirth());
        trainee.setUser(userConverter.convertRegistrationRequest(requestDTO.getFirstName(), requestDTO.getLastName()));
        log.info("Successfully converted trainee registration request to model.");
        return trainee;
    }

    public RegistrationResponseDTO convertModelToRegistrationResponse(TraineeEntity trainee) {
        RegistrationResponseDTO responseDTO = new RegistrationResponseDTO();
        responseDTO.setUsername(trainee.getUser().getUsername());
        responseDTO.setPassword(trainee.getUser().getPassword());
        log.info("Successfully converted trainee model to registration response.");
        return responseDTO;
    }

    public TraineeResponseDTO convertModelToResponse(TraineeEntity trainee) {
        TraineeResponseDTO responseDTO = new TraineeResponseDTO();
        responseDTO.setFirstName(trainee.getUser().getFirstName());
        responseDTO.setLastName(trainee.getUser().getLastName());
        responseDTO.setIsActive(trainee.getUser().getIsActive());
        responseDTO.setAddress(trainee.getAddress());
        responseDTO.setDateOfBirth(trainee.getDateOfBirth());
        if (trainee.getTrainers() != null) {
            responseDTO.setTrainers(trainee.getTrainers().stream().
                    map(trainerConverter::convertModelToTraineeTrainer)
                    .toList());
        }
        log.info("Successfully converted trainee model to trainee response.");
        return responseDTO;
    }

    public TraineeUpdateResponseDTO convertModelToUpdateResponse(TraineeEntity trainee) {
        TraineeUpdateResponseDTO responseDTO = new TraineeUpdateResponseDTO();
        responseDTO.setUsername(trainee.getUser().getUsername());
        responseDTO.setFirstName(trainee.getUser().getFirstName());
        responseDTO.setLastName(trainee.getUser().getLastName());
        responseDTO.setIsActive(trainee.getUser().getIsActive());
        responseDTO.setAddress(trainee.getAddress());
        responseDTO.setDateOfBirth(trainee.getDateOfBirth());
        responseDTO.setTrainers(trainee.getTrainers().stream().
                map(trainerConverter::convertModelToTraineeTrainer)
                .toList());
        log.info("Successfully converted trainee model to update response.");
        return responseDTO;
    }

    public TraineeEntity convertUpdateRequestToModel(TraineeUpdateRequestDTO requestDTO) {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setDateOfBirth(requestDTO.getDateOfBirth());
        trainee.setAddress(requestDTO.getAddress());
        trainee.setUser(userConverter.convertUpdateRequestToModel(
                requestDTO.getUsername(),
                requestDTO.getFirstName(),
                requestDTO.getLastName(),
                requestDTO.getIsActive()));
        log.info("Successfully converted trainee update request to model");
        return trainee;
    }

    public TrainerTraineeResponseDTO convertModelToTrainerTrainee(TraineeEntity trainee) {
        TrainerTraineeResponseDTO responseDTO = new TrainerTraineeResponseDTO();
        responseDTO.setFirstName(trainee.getUser().getFirstName());
        responseDTO.setLastName(trainee.getUser().getLastName());
        responseDTO.setUsername(trainee.getUser().getUsername());
        log.info("Successfully converted trainee model to trainer's trainee response");
        return responseDTO;
    }
}
