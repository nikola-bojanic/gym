package com.nikolabojanic.converter;

import com.nikolabojanic.domain.TraineeDomain;
import com.nikolabojanic.dto.RegistrationResponseDto;
import com.nikolabojanic.dto.TraineeRegistrationRequestDto;
import com.nikolabojanic.dto.TraineeResponseDto;
import com.nikolabojanic.dto.TraineeUpdateRequestDto;
import com.nikolabojanic.dto.TraineeUpdateResponseDto;
import com.nikolabojanic.dto.TrainerTraineeResponseDto;
import com.nikolabojanic.entity.TraineeEntity;
import com.nikolabojanic.entity.UserEntity;
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

    /**
     * Convert a username to a TraineeEntity model.
     *
     * @param username The username to be converted to a TraineeEntity model.
     * @return TraineeEntity representing the converted model with user details.
     */
    public TraineeEntity convertUsernameToModel(String username) {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(userConverter.convertUsernameToUser(username));
        return trainee;
    }

    /**
     * Convert a TraineeRegistrationRequestDto to a TraineeEntity model.
     *
     * @param requestDto The TraineeRegistrationRequestDto containing registration details.
     * @return TraineeEntity representing the converted model with registration details.
     */
    public TraineeEntity convertRegistrationRequestToModel(TraineeRegistrationRequestDto requestDto) {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setAddress(requestDto.getAddress());
        trainee.setDateOfBirth(requestDto.getDateOfBirth());
        trainee.setUser(userConverter.convertRegistrationRequest(requestDto.getFirstName(), requestDto.getLastName()));
        log.info("Successfully converted trainee registration request to model.");
        return trainee;
    }

    /**
     * Convert a TraineeDomain model to a RegistrationResponseDto.
     *
     * @param trainee The TraineeDomain model to be converted.
     * @return RegistrationResponseDto representing the converted response.
     */
    public RegistrationResponseDto convertModelToRegistrationResponse(TraineeDomain trainee) {
        RegistrationResponseDto responseDto = new RegistrationResponseDto();
        responseDto.setUsername(trainee.getUsername());
        responseDto.setPassword(trainee.getPassword());
        log.info("Successfully converted trainee domain model to registration response.");
        return responseDto;
    }

    /**
     * Convert a TraineeEntity model to a TraineeResponseDto.
     *
     * @param trainee The TraineeEntity model to be converted.
     * @return TraineeResponseDto representing the converted response.
     */
    public TraineeResponseDto convertModelToResponse(TraineeEntity trainee) {
        TraineeResponseDto responseDto = new TraineeResponseDto();
        responseDto.setFirstName(trainee.getUser().getFirstName());
        responseDto.setLastName(trainee.getUser().getLastName());
        responseDto.setIsActive(trainee.getUser().getIsActive());
        responseDto.setAddress(trainee.getAddress());
        responseDto.setDateOfBirth(trainee.getDateOfBirth());
        if (trainee.getTrainers() != null) {
            responseDto.setTrainers(trainee.getTrainers().stream()
                .map(trainerConverter::convertModelToTraineeTrainer)
                .toList());
        }
        log.info("Successfully converted trainee model to trainee response.");
        return responseDto;
    }

    /**
     * Convert a TraineeEntity model to a TraineeUpdateResponseDto.
     *
     * @param trainee The TraineeEntity model to be converted.
     * @return TraineeUpdateResponseDto representing the converted update response.
     */
    public TraineeUpdateResponseDto convertModelToUpdateResponse(TraineeEntity trainee) {
        TraineeUpdateResponseDto responseDto = new TraineeUpdateResponseDto();
        responseDto.setUsername(trainee.getUser().getUsername());
        responseDto.setFirstName(trainee.getUser().getFirstName());
        responseDto.setLastName(trainee.getUser().getLastName());
        responseDto.setIsActive(trainee.getUser().getIsActive());
        responseDto.setAddress(trainee.getAddress());
        responseDto.setDateOfBirth(trainee.getDateOfBirth());
        responseDto.setTrainers(trainee.getTrainers().stream()
            .map(trainerConverter::convertModelToTraineeTrainer)
            .toList());
        log.info("Successfully converted trainee model to update response.");
        return responseDto;
    }

    /**
     * Convert a TraineeUpdateRequestDto to a TraineeEntity model for updating a trainee profile.
     *
     * @param requestDto The TraineeUpdateRequestDto containing update details.
     * @return TraineeEntity representing the converted model with update details.
     */
    public TraineeEntity convertUpdateRequestToModel(TraineeUpdateRequestDto requestDto) {
        TraineeEntity trainee = new TraineeEntity();
        trainee.setDateOfBirth(requestDto.getDateOfBirth());
        trainee.setAddress(requestDto.getAddress());
        trainee.setUser(userConverter.convertUpdateRequestToModel(
            requestDto.getUsername(),
            requestDto.getFirstName(),
            requestDto.getLastName(),
            requestDto.getIsActive()));
        log.info("Successfully converted trainee update request to model");
        return trainee;
    }

    /**
     * Convert a TraineeEntity model to a TrainerTraineeResponseDto representing a trainer's trainee.
     *
     * @param trainee The TraineeEntity model to be converted.
     * @return TrainerTraineeResponseDto representing the converted response for a trainer's trainee.
     */
    public TrainerTraineeResponseDto convertModelToTrainerTrainee(TraineeEntity trainee) {
        TrainerTraineeResponseDto responseDto = new TrainerTraineeResponseDto();
        responseDto.setFirstName(trainee.getUser().getFirstName());
        responseDto.setLastName(trainee.getUser().getLastName());
        responseDto.setUsername(trainee.getUser().getUsername());
        log.info("Successfully converted trainee model to trainer's trainee response");
        return responseDto;
    }

    /**
     * Convert a UserEntity to a TraineeDomain model.
     *
     * @param entity The UserEntity to be converted.
     * @return TraineeDomain representing the converted domain model.
     */
    public TraineeDomain convertEntityToDomainModel(UserEntity entity) {
        TraineeDomain domainModel = new TraineeDomain();
        domainModel.setUsername(entity.getUsername());
        domainModel.setPassword(entity.getPassword());
        return domainModel;
    }
}
