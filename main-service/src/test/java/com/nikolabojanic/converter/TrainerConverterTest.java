package com.nikolabojanic.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.nikolabojanic.domain.TrainerDomain;
import com.nikolabojanic.dto.ActiveTrainerResponseDto;
import com.nikolabojanic.dto.RegistrationResponseDto;
import com.nikolabojanic.dto.TraineeTrainerResponseDto;
import com.nikolabojanic.dto.TrainerRegistrationRequestDto;
import com.nikolabojanic.dto.TrainerResponseDto;
import com.nikolabojanic.dto.TrainerTraineeResponseDto;
import com.nikolabojanic.dto.TrainerUpdateRequestDto;
import com.nikolabojanic.dto.TrainerUpdateResponseDto;
import com.nikolabojanic.entity.TraineeEntity;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.entity.TrainingTypeEntity;
import com.nikolabojanic.entity.UserEntity;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        TrainerRegistrationRequestDto requestDto = new TrainerRegistrationRequestDto();
        requestDto.setSpecializationId(type.getId());
        requestDto.setFirstName(user.getFirstName());
        requestDto.setLastName(user.getLastName());
        when(userConverter.convertRegistrationRequest(requestDto.getFirstName(), requestDto.getLastName()))
            .thenReturn(user);
        when(trainingTypeConverter.convertIdToModel(requestDto.getSpecializationId())).thenReturn(type);
        //when
        TrainerEntity trainer = trainerConverter.convertRegistrationRequestToModel(requestDto);
        //then
        assertThat(trainer.getUser().getFirstName()).isEqualTo(requestDto.getFirstName());
        assertThat(trainer.getUser().getLastName()).isEqualTo(requestDto.getLastName());
        assertThat(trainer.getSpecialization().getId()).isEqualTo(requestDto.getSpecializationId());
    }

    @Test
    void convertModelToRegistrationResponse() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String password = RandomStringUtils.randomAlphabetic(5);
        TrainerDomain trainer = new TrainerDomain();
        trainer.setUsername(username);
        trainer.setPassword(password);
        //when
        RegistrationResponseDto responseDto = trainerConverter.convertModelToRegistrationResponse(trainer);
        //then
        assertThat(responseDto.getUsername()).isEqualTo(trainer.getUsername());
        assertThat(responseDto.getPassword()).isEqualTo(trainer.getPassword());
    }

    @Test
    void convertModelToResponseDtoTest() {
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
        when(traineeConverter.convertModelToTrainerTrainee(trainee)).thenReturn(new TrainerTraineeResponseDto());
        // when
        TrainerResponseDto responseDto = trainerConverter.convertModelToResponseDto(trainer);
        // then
        assertThat(responseDto.getFirstName()).isEqualTo(trainer.getUser().getFirstName());
        assertThat(responseDto.getLastName()).isEqualTo(trainer.getUser().getLastName());
        assertThat(responseDto.getIsActive()).isEqualTo(trainer.getUser().getIsActive());
        assertThat(responseDto.getSpecializationId()).isEqualTo(trainer.getSpecialization().getId());
        assertThat(responseDto.getTrainees()).hasSize(trainer.getTrainees().size());
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
        TraineeTrainerResponseDto responseDto = trainerConverter.convertModelToTraineeTrainer(trainer);
        // then
        assertThat(responseDto.getFirstName()).isEqualTo(trainer.getUser().getFirstName());
        assertThat(responseDto.getLastName()).isEqualTo(trainer.getUser().getLastName());
        assertThat(responseDto.getUsername()).isEqualTo(trainer.getUser().getUsername());
        assertThat(responseDto.getSpecializationId()).isEqualTo(trainer.getSpecialization().getId());
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
        TrainerUpdateRequestDto requestDto = new TrainerUpdateRequestDto();
        requestDto.setUsername(user.getUsername());
        requestDto.setFirstName(user.getFirstName());
        requestDto.setLastName(user.getLastName());
        requestDto.setIsActive(user.getIsActive());
        requestDto.setSpecializationId(type.getId());
        when(trainingTypeConverter.convertIdToModel(requestDto.getSpecializationId())).thenReturn(type);
        when(userConverter.convertUpdateRequestToModel(requestDto.getUsername(),
            requestDto.getFirstName(),
            requestDto.getLastName(),
            requestDto.getIsActive()))
            .thenReturn(user);
        // when
        TrainerEntity domainModel = trainerConverter.convertUpdateRequestToModel(requestDto);
        // then
        assertThat(domainModel.getUser().getUsername()).isEqualTo(requestDto.getUsername());
        assertThat(domainModel.getUser().getFirstName()).isEqualTo(requestDto.getFirstName());
        assertThat(domainModel.getUser().getLastName()).isEqualTo(requestDto.getLastName());
        assertThat(domainModel.getUser().getIsActive()).isEqualTo(requestDto.getIsActive());
        assertThat(domainModel.getSpecialization().getId()).isEqualTo(requestDto.getSpecializationId());
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
        when(traineeConverter.convertModelToTrainerTrainee(trainee)).thenReturn(new TrainerTraineeResponseDto());

        // when
        TrainerUpdateResponseDto responseDto = trainerConverter.convertModelToUpdateResponse(trainer);

        // then
        assertThat(responseDto.getUsername()).isEqualTo(trainer.getUser().getUsername());
        assertThat(responseDto.getFirstName()).isEqualTo(trainer.getUser().getFirstName());
        assertThat(responseDto.getLastName()).isEqualTo(trainer.getUser().getLastName());
        assertThat(responseDto.getIsActive()).isEqualTo(trainer.getUser().getIsActive());
        assertThat(responseDto.getSpecializationId()).isEqualTo(trainer.getSpecialization().getId());
        assertThat(responseDto.getTrainees()).hasSize(trainer.getTrainees().size());
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
        ActiveTrainerResponseDto responseDto = trainerConverter.convertModelToActiveTrainerResponse(trainer);
        // then
        assertThat(responseDto.getUsername()).isEqualTo(trainer.getUser().getUsername());
        assertThat(responseDto.getFirstName()).isEqualTo(trainer.getUser().getFirstName());
        assertThat(responseDto.getLastName()).isEqualTo(trainer.getUser().getLastName());
        assertThat(responseDto.getSpecializationId()).isEqualTo(trainer.getSpecialization().getId());
    }
}
