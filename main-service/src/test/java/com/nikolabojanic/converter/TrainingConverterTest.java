package com.nikolabojanic.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.nikolabojanic.dto.TraineeTrainingResponseDto;
import com.nikolabojanic.dto.TrainerTrainingResponseDto;
import com.nikolabojanic.dto.TrainingRequestDto;
import com.nikolabojanic.dto.TrainingResponseDto;
import com.nikolabojanic.entity.TraineeEntity;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.entity.TrainingEntity;
import com.nikolabojanic.entity.TrainingTypeEntity;
import com.nikolabojanic.entity.UserEntity;
import java.time.LocalDate;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        TrainingRequestDto requestDto = new TrainingRequestDto();
        requestDto.setDate(LocalDate.now());
        requestDto.setName(RandomStringUtils.randomAlphabetic(10));
        requestDto.setDuration(Double.parseDouble(RandomStringUtils.randomNumeric(5)));
        requestDto.setTrainerUsername(trainer.getUser().getUsername());
        requestDto.setTraineeUsername(trainee.getUser().getUsername());
        when(trainerConverter.convertUsernameToModel(requestDto.getTrainerUsername())).thenReturn(trainer);
        when(traineeConverter.convertUsernameToModel(requestDto.getTraineeUsername())).thenReturn(trainee);
        //when
        TrainingEntity training = trainingConverter.convertToEntity(requestDto);
        //then
        assertThat(training.getDate()).isEqualTo(requestDto.getDate());
        assertThat(training.getTrainer().getUser().getUsername()).isEqualTo(requestDto.getTrainerUsername());
        assertThat(training.getTrainee().getUser().getUsername()).isEqualTo(requestDto.getTraineeUsername());
        assertThat(training.getDuration()).isEqualTo(requestDto.getDuration());
        assertThat(training.getName()).isEqualTo(requestDto.getName());
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
        training.setId(Long.parseLong(RandomStringUtils.randomNumeric(5)));
        training.setTrainer(trainer);
        training.setTrainee(trainee);
        training.setName(RandomStringUtils.randomAlphabetic(10));
        training.setDate(LocalDate.now());
        training.setDuration(Double.parseDouble(RandomStringUtils.randomNumeric(5)));
        //when
        TrainingResponseDto responseDto = trainingConverter.convertToDto(training);
        //then
        assertThat(responseDto.getId()).isEqualTo(training.getId());
        assertThat(responseDto.getDate()).isEqualTo(training.getDate());
        assertThat(responseDto.getDuration()).isEqualTo(training.getDuration());
        assertThat(responseDto.getName()).isEqualTo(training.getName());
        assertThat(responseDto.getTrainerUsername()).isEqualTo(training.getTrainer().getUser().getUsername());
        assertThat(responseDto.getTraineeUsername()).isEqualTo(training.getTrainee().getUser().getUsername());
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
        TrainerTrainingResponseDto responseDto = trainingConverter.convertModelToTrainerTraining(training);
        //then
        assertThat(responseDto.getDate()).isEqualTo(training.getDate());
        assertThat(responseDto.getDuration()).isEqualTo(training.getDuration());
        assertThat(responseDto.getName()).isEqualTo(training.getName());
        assertThat(responseDto.getTrainingTypeId()).isEqualTo(training.getType().getId());
        assertThat(responseDto.getTraineeName()).isEqualTo(training.getTrainee().getUser().getFirstName());
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
        TraineeTrainingResponseDto responseDto = trainingConverter.convertModelToTraineeTraining(training);
        //then
        assertThat(responseDto.getDate()).isEqualTo(training.getDate());
        assertThat(responseDto.getDuration()).isEqualTo(training.getDuration());
        assertThat(responseDto.getName()).isEqualTo(training.getName());
        assertThat(responseDto.getTrainingTypeId()).isEqualTo(training.getType().getId());
        assertThat(responseDto.getTrainerName()).isEqualTo(training.getTrainer().getUser().getFirstName());

    }
}