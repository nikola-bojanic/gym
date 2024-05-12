package com.nikolabojanic.converter;

import com.nikolabojanic.dto.TraineeTrainingResponseDTO;
import com.nikolabojanic.dto.TrainerTrainingResponseDTO;
import com.nikolabojanic.dto.TrainingRequestDTO;
import com.nikolabojanic.dto.TrainingResponseDTO;
import com.nikolabojanic.entity.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingConverterTest {
    @Mock
    private TrainerConverter trainerConverter;
    @Mock
    private TraineeConverter traineeConverter;
    @InjectMocks
    private TrainingConverter trainingConverter;

    @Test
    void convertToEntityTest() {
        //given
        UserEntity user = new UserEntity();
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        TrainingRequestDTO requestDTO = new TrainingRequestDTO();
        requestDTO.setDate(LocalDate.now());
        requestDTO.setName(RandomStringUtils.randomAlphabetic(10));
        requestDTO.setDuration(Double.parseDouble(RandomStringUtils.randomNumeric(5)));
        requestDTO.setTrainerUsername(trainer.getUser().getUsername());
        requestDTO.setTraineeUsername(trainee.getUser().getUsername());
        when(trainerConverter.convertUsernameToModel(requestDTO.getTrainerUsername())).thenReturn(trainer);
        when(traineeConverter.convertUsernameToModel(requestDTO.getTraineeUsername())).thenReturn(trainee);
        //when
        TrainingEntity training = trainingConverter.convertToEntity(requestDTO);
        //then
        assertThat(training.getDate()).isEqualTo(requestDTO.getDate());
        assertThat(training.getTrainer().getUser().getUsername()).isEqualTo(requestDTO.getTrainerUsername());
        assertThat(training.getTrainee().getUser().getUsername()).isEqualTo(requestDTO.getTraineeUsername());
        assertThat(training.getDuration()).isEqualTo(requestDTO.getDuration());
        assertThat(training.getName()).isEqualTo(requestDTO.getName());
    }

    @Test
    void convertToDtoTest() {
        //given
        UserEntity user = new UserEntity();
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        TrainingEntity training = new TrainingEntity();
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setName(RandomStringUtils.randomAlphabetic(10));
        training.setDate(LocalDate.now());
        training.setDuration(Double.parseDouble(RandomStringUtils.randomNumeric(5)));
        //when
        TrainingResponseDTO responseDTO = trainingConverter.convertToDto(training);
        //then
        assertThat(responseDTO.getDate()).isEqualTo(training.getDate());
        assertThat(responseDTO.getDuration()).isEqualTo(training.getDuration());
        assertThat(responseDTO.getName()).isEqualTo(training.getName());
        assertThat(responseDTO.getTrainerUsername()).isEqualTo(training.getTrainer().getUser().getUsername());
        assertThat(responseDTO.getTraineeUsername()).isEqualTo(training.getTrainee().getUser().getUsername());
    }

    @Test
    void convertModelToTrainerTrainingTest() {
        //given
        UserEntity user = new UserEntity();
        user.setFirstName(RandomStringUtils.randomAlphabetic(10));
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        TrainingTypeEntity type = new TrainingTypeEntity();
        type.setId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        TrainingEntity training = new TrainingEntity();
        training.setDuration(Double.parseDouble(RandomStringUtils.randomNumeric(5)));
        training.setDate(LocalDate.now());
        training.setName(RandomStringUtils.randomAlphabetic(10));
        training.setTrainee(trainee);
        training.setType(type);
        //when
        TrainerTrainingResponseDTO responseDTO = trainingConverter.convertModelToTrainerTraining(training);
        //then
        assertThat(responseDTO.getDate()).isEqualTo(training.getDate());
        assertThat(responseDTO.getDuration()).isEqualTo(training.getDuration());
        assertThat(responseDTO.getName()).isEqualTo(training.getName());
        assertThat(responseDTO.getTrainingTypeId()).isEqualTo(training.getType().getId());
        assertThat(responseDTO.getTraineeName()).isEqualTo(training.getTrainee().getUser().getFirstName());
    }

    @Test
    void convertModelToTraineeTrainingTest() {
        //given
        UserEntity user = new UserEntity();
        user.setFirstName(RandomStringUtils.randomAlphabetic(10));
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        TrainingTypeEntity type = new TrainingTypeEntity();
        type.setId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        TrainingEntity training = new TrainingEntity();
        training.setDuration(Double.parseDouble(RandomStringUtils.randomNumeric(5)));
        training.setDate(LocalDate.now());
        training.setName(RandomStringUtils.randomAlphabetic(10));
        training.setTrainer(trainer);
        training.setType(type);
        //when
        TraineeTrainingResponseDTO responseDTO = trainingConverter.convertModelToTraineeTraining(training);
        //then
        assertThat(responseDTO.getDate()).isEqualTo(training.getDate());
        assertThat(responseDTO.getDuration()).isEqualTo(training.getDuration());
        assertThat(responseDTO.getName()).isEqualTo(training.getName());
        assertThat(responseDTO.getTrainingTypeId()).isEqualTo(training.getType().getId());
        assertThat(responseDTO.getTrainerName()).isEqualTo(training.getTrainer().getUser().getFirstName());

    }
}