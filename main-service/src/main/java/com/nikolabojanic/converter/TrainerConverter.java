package com.nikolabojanic.converter;

import com.nikolabojanic.domain.TrainerDomain;
import com.nikolabojanic.dto.ActiveTrainerResponseDto;
import com.nikolabojanic.dto.RegistrationResponseDto;
import com.nikolabojanic.dto.TraineeTrainerResponseDto;
import com.nikolabojanic.dto.TrainerRegistrationRequestDto;
import com.nikolabojanic.dto.TrainerResponseDto;
import com.nikolabojanic.dto.TrainerUpdateRequestDto;
import com.nikolabojanic.dto.TrainerUpdateResponseDto;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.entity.UserEntity;
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

    /**
     * Convert a username to a TrainerEntity model.
     *
     * @param username The username to be converted.
     * @return TrainerEntity representing the converted model.
     */
    public TrainerEntity convertUsernameToModel(String username) {
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(userConverter.convertUsernameToUser(username));
        return trainer;
    }

    /**
     * Convert a TrainerRegistrationRequestDto to a TrainerEntity model.
     *
     * @param requestDto The TrainerRegistrationRequestDto to be converted.
     * @return TrainerEntity representing the converted model.
     */
    public TrainerEntity convertRegistrationRequestToModel(TrainerRegistrationRequestDto requestDto) {
        TrainerEntity trainer = new TrainerEntity();
        trainer.setSpecialization(trainingTypeConverter.convertIdToModel(requestDto.getSpecializationId()));
        trainer.setUser(userConverter.convertRegistrationRequest(requestDto.getFirstName(), requestDto.getLastName()));
        log.info("Successfully converted trainer registration request to model.");
        return trainer;
    }

    /**
     * Convert a TrainerDomain to a RegistrationResponseDto.
     *
     * @param trainer The TrainerDomain to be converted.
     * @return RegistrationResponseDto representing the converted response.
     */
    public RegistrationResponseDto convertModelToRegistrationResponse(TrainerDomain trainer) {
        RegistrationResponseDto responseDto = new RegistrationResponseDto();
        responseDto.setUsername(trainer.getUsername());
        responseDto.setPassword(trainer.getPassword());
        log.info("Successfully converted domain model to trainee registration response.");
        return responseDto;
    }

    /**
     * Convert a TrainerEntity to a TrainerResponseDto.
     *
     * @param trainer The TrainerEntity to be converted.
     * @return TrainerResponseDto representing the converted response.
     */
    public TrainerResponseDto convertModelToResponseDto(TrainerEntity trainer) {
        TrainerResponseDto responseDto = new TrainerResponseDto();
        responseDto.setFirstName(trainer.getUser().getFirstName());
        responseDto.setLastName(trainer.getUser().getLastName());
        responseDto.setIsActive(trainer.getUser().getIsActive());
        if (trainer.getSpecialization() != null) {
            responseDto.setSpecializationId(trainer.getSpecialization().getId());
        } else {
            responseDto.setSpecializationId(null);
        }
        responseDto.setTrainees(trainer.getTrainees().stream()
            .map(traineeConverter::convertModelToTrainerTrainee)
            .toList());
        log.info("Successfully converted trainer model to trainer response.");
        return responseDto;
    }

    /**
     * Convert a TrainerEntity to a TraineeTrainerResponseDto.
     *
     * @param trainer The TrainerEntity to be converted.
     * @return TraineeTrainerResponseDto representing the converted response.
     */
    public TraineeTrainerResponseDto convertModelToTraineeTrainer(TrainerEntity trainer) {
        TraineeTrainerResponseDto responseDto = new TraineeTrainerResponseDto();
        responseDto.setFirstName(trainer.getUser().getFirstName());
        responseDto.setLastName(trainer.getUser().getLastName());
        responseDto.setUsername(trainer.getUser().getUsername());
        if (trainer.getSpecialization() != null) {
            responseDto.setSpecializationId(trainer.getSpecialization().getId());
        } else {
            responseDto.setSpecializationId(null);
        }
        log.info("Successfully converted trainer model to trainee's trainer response.");
        return responseDto;
    }

    /**
     * Convert a TrainerUpdateRequestDto to a TrainerEntity model.
     *
     * @param requestDto The TrainerUpdateRequestDto to be converted.
     * @return TrainerEntity representing the converted model.
     */
    public TrainerEntity convertUpdateRequestToModel(TrainerUpdateRequestDto requestDto) {
        TrainerEntity trainer = new TrainerEntity();
        trainer.setSpecialization(trainingTypeConverter.convertIdToModel(requestDto.getSpecializationId()));
        trainer.setUser(userConverter.convertUpdateRequestToModel(
            requestDto.getUsername(),
            requestDto.getFirstName(),
            requestDto.getLastName(),
            requestDto.getIsActive()));
        log.info("Successfully converted trainee update request to model.");
        return trainer;
    }

    /**
     * Convert a TrainerEntity to a TrainerUpdateResponseDto.
     *
     * @param trainer The TrainerEntity to be converted.
     * @return TrainerUpdateResponseDto representing the converted response.
     */
    public TrainerUpdateResponseDto convertModelToUpdateResponse(TrainerEntity trainer) {
        TrainerUpdateResponseDto responseDto = new TrainerUpdateResponseDto();
        responseDto.setUsername(trainer.getUser().getUsername());
        responseDto.setFirstName(trainer.getUser().getFirstName());
        responseDto.setLastName(trainer.getUser().getLastName());
        responseDto.setIsActive(trainer.getUser().getIsActive());
        if (trainer.getSpecialization() != null) {
            responseDto.setSpecializationId(trainer.getSpecialization().getId());
        } else {
            responseDto.setSpecializationId(null);
        }
        responseDto.setTrainees(trainer.getTrainees().stream()
            .map(traineeConverter::convertModelToTrainerTrainee)
            .toList());
        log.info("Successfully converted model to trainee update response.");
        return responseDto;
    }

    /**
     * Convert a TrainerEntity to an ActiveTrainerResponseDto.
     *
     * @param trainer The TrainerEntity to be converted.
     * @return ActiveTrainerResponseDto representing the converted response.
     */
    public ActiveTrainerResponseDto convertModelToActiveTrainerResponse(TrainerEntity trainer) {
        ActiveTrainerResponseDto responseDto = new ActiveTrainerResponseDto();
        responseDto.setFirstName(trainer.getUser().getFirstName());
        responseDto.setLastName(trainer.getUser().getLastName());
        responseDto.setUsername(trainer.getUser().getUsername());
        if (trainer.getSpecialization() != null) {
            responseDto.setSpecializationId(trainer.getSpecialization().getId());
        } else {
            responseDto.setSpecializationId(null);
        }
        log.info("Successfully converted trainer model to active trainer response.");
        return responseDto;
    }

    /**
     * Convert a UserEntity to a TrainerDomain model.
     *
     * @param entity The UserEntity to be converted.
     * @return TrainerDomain representing the converted model.
     */
    public TrainerDomain convertEntityToDomainModel(UserEntity entity) {
        TrainerDomain domain = new TrainerDomain();
        domain.setUsername(entity.getUsername());
        domain.setPassword(entity.getPassword());
        return domain;
    }
}
