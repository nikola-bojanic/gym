package com.nikolabojanic.converter;

import com.nikolabojanic.dto.*;
import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.TrainingTypeEntity;
import com.nikolabojanic.model.UserEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@Nested
@ExtendWith(MockitoExtension.class)
class TrainerConverterTest {
    @Mock
    private UserConverter userConverter;
    @Mock
    private TrainingTypeConverter trainingTypeConverter;
    @Mock
    private TraineeConverter traineeConverter;
    @InjectMocks
    private TrainerConverter trainerConverter;

    @Test
    void convertUsernameToModelTest() {
        // given
        String username = RandomStringUtils.randomAlphabetic(10);
        UserEntity user = new UserEntity();
        user.setUsername(username);
        when(userConverter.convertUsernameToUser(username)).thenReturn(user);
        // when
        TrainerEntity domainModel = trainerConverter.convertUsernameToModel(username);
        // then
        assertThat(domainModel.getUser().getUsername()).isEqualTo(username);
    }

    @Test
    void convertRegistrationRequestToModelTest() {
        //given
        UserEntity user = new UserEntity();
        user.setFirstName(RandomStringUtils.randomAlphabetic(10));
        user.setLastName(RandomStringUtils.randomAlphabetic(10));
        TrainingTypeEntity type = new TrainingTypeEntity();
        type.setId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        TrainerRegistrationRequestDTO requestDTO = new TrainerRegistrationRequestDTO();
        requestDTO.setSpecializationId(type.getId());
        requestDTO.setFirstName(user.getFirstName());
        requestDTO.setLastName(user.getLastName());
        when(userConverter.convertRegistrationRequest(requestDTO.getFirstName(), requestDTO.getLastName()))
                .thenReturn(user);
        when(trainingTypeConverter.convertIdToModel(requestDTO.getSpecializationId())).thenReturn(type);
        //when
        TrainerEntity trainer = trainerConverter.convertRegistrationRequestToModel(requestDTO);
        //then
        assertThat(trainer.getUser().getFirstName()).isEqualTo(requestDTO.getFirstName());
        assertThat(trainer.getUser().getLastName()).isEqualTo(requestDTO.getLastName());
        assertThat(trainer.getSpecialization().getId()).isEqualTo(requestDTO.getSpecializationId());
    }

    @Test
    void convertModelToRegistrationResponse() {
        //given
        UserEntity user = new UserEntity();
        user.setUsername(RandomStringUtils.randomAlphabetic(5));
        user.setPassword(RandomStringUtils.randomAlphabetic(5));
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        //when
        RegistrationResponseDTO responseDTO = trainerConverter.convertModelToRegistrationResponse(trainer);
        //then
        assertThat(responseDTO.getUsername()).isEqualTo(trainer.getUser().getUsername());
        assertThat(responseDTO.getPassword()).isEqualTo(trainer.getUser().getPassword());
    }

    @Test
    void convertModelToResponseDTOTest() {
        // given
        TrainingTypeEntity type = new TrainingTypeEntity();
        type.setId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        UserEntity user = new UserEntity();
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        user.setIsActive(true);
        TraineeEntity trainee = new TraineeEntity();
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        trainer.setSpecialization(type);
        trainer.setTrainees(List.of(trainee));
        when(traineeConverter.convertModelToTrainerTrainee(trainee)).thenReturn(new TrainerTraineeResponseDTO());
        // when
        TrainerResponseDTO responseDTO = trainerConverter.convertModelToResponseDTO(trainer);
        // then
        assertThat(responseDTO.getFirstName()).isEqualTo(trainer.getUser().getFirstName());
        assertThat(responseDTO.getLastName()).isEqualTo(trainer.getUser().getLastName());
        assertThat(responseDTO.getIsActive()).isEqualTo(trainer.getUser().getIsActive());
        assertThat(responseDTO.getSpecializationId()).isEqualTo(trainer.getSpecialization().getId());
        assertThat(responseDTO.getTrainees()).hasSize(trainer.getTrainees().size());
    }

    @Test
    void convertModelToTraineeTrainerTest() {
        // given
        TrainingTypeEntity type = new TrainingTypeEntity();
        type.setId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        UserEntity user = new UserEntity();
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        trainer.setSpecialization(type);
        // when
        TraineeTrainerResponseDTO responseDTO = trainerConverter.convertModelToTraineeTrainer(trainer);
        // then
        assertThat(responseDTO.getFirstName()).isEqualTo(trainer.getUser().getFirstName());
        assertThat(responseDTO.getLastName()).isEqualTo(trainer.getUser().getLastName());
        assertThat(responseDTO.getUsername()).isEqualTo(trainer.getUser().getUsername());
        assertThat(responseDTO.getSpecializationId()).isEqualTo(trainer.getSpecialization().getId());
    }


    @Test
    void convertUpdateRequestToModelTest() {
        // given
        TrainingTypeEntity type = new TrainingTypeEntity();
        type.setId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        UserEntity user = new UserEntity();
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        user.setIsActive(true);
        TrainerUpdateRequestDTO requestDTO = new TrainerUpdateRequestDTO();
        requestDTO.setUsername(user.getUsername());
        requestDTO.setFirstName(user.getFirstName());
        requestDTO.setLastName(user.getLastName());
        requestDTO.setIsActive(user.getIsActive());
        requestDTO.setSpecializationId(type.getId());
        when(trainingTypeConverter.convertIdToModel(requestDTO.getSpecializationId())).thenReturn(type);
        when(userConverter.convertUpdateRequestToModel(requestDTO.getUsername(),
                requestDTO.getFirstName(),
                requestDTO.getLastName(),
                requestDTO.getIsActive()))
                .thenReturn(user);
        // when
        TrainerEntity domainModel = trainerConverter.convertUpdateRequestToModel(requestDTO);
        // then
        assertThat(domainModel.getUser().getUsername()).isEqualTo(requestDTO.getUsername());
        assertThat(domainModel.getUser().getFirstName()).isEqualTo(requestDTO.getFirstName());
        assertThat(domainModel.getUser().getLastName()).isEqualTo(requestDTO.getLastName());
        assertThat(domainModel.getUser().getIsActive()).isEqualTo(requestDTO.getIsActive());
        assertThat(domainModel.getSpecialization().getId()).isEqualTo(requestDTO.getSpecializationId());
    }

    @Test
    void convertModelToUpdateResponseTest() {
        // given
        UserEntity user = new UserEntity();
        user.setUsername(RandomStringUtils.randomAlphabetic(5));
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        user.setIsActive(true);
        TraineeEntity trainee = new TraineeEntity();
        TrainingTypeEntity trainingType = new TrainingTypeEntity();
        trainingType.setId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        trainer.setSpecialization(trainingType);
        trainer.setTrainees(List.of(trainee));
        when(traineeConverter.convertModelToTrainerTrainee(trainee)).thenReturn(new TrainerTraineeResponseDTO());

        // when
        TrainerUpdateResponseDTO responseDTO = trainerConverter.convertModelToUpdateResponse(trainer);

        // then
        assertThat(responseDTO.getUsername()).isEqualTo(trainer.getUser().getUsername());
        assertThat(responseDTO.getFirstName()).isEqualTo(trainer.getUser().getFirstName());
        assertThat(responseDTO.getLastName()).isEqualTo(trainer.getUser().getLastName());
        assertThat(responseDTO.getIsActive()).isEqualTo(trainer.getUser().getIsActive());
        assertThat(responseDTO.getSpecializationId()).isEqualTo(trainer.getSpecialization().getId());
        assertThat(responseDTO.getTrainees()).hasSize(trainer.getTrainees().size());
    }

    @Test
    void convertModelToActiveTrainerResponseTest() {
        // given
        UserEntity user = new UserEntity();
        user.setUsername(RandomStringUtils.randomAlphabetic(5));
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        TrainingTypeEntity trainingType = new TrainingTypeEntity();
        trainingType.setId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        trainer.setSpecialization(trainingType);
        // when
        ActiveTrainerResponseDTO responseDTO = trainerConverter.convertModelToActiveTrainerResponse(trainer);
        // then
        assertThat(responseDTO.getUsername()).isEqualTo(trainer.getUser().getUsername());
        assertThat(responseDTO.getFirstName()).isEqualTo(trainer.getUser().getFirstName());
        assertThat(responseDTO.getLastName()).isEqualTo(trainer.getUser().getLastName());
        assertThat(responseDTO.getSpecializationId()).isEqualTo(trainer.getSpecialization().getId());
    }
}
