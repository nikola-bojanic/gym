package com.nikolabojanic.converter;

import com.nikolabojanic.dto.*;
import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.UserEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeConverterTest {
    @Mock
    private UserConverter userConverter;
    @Mock
    private TrainerConverter trainerConverter;
    @InjectMocks
    private TraineeConverter traineeConverter;

    @Test
    void convertUsernameToModelTest() {
        // given
        String username = RandomStringUtils.randomAlphabetic(10);
        UserEntity user = new UserEntity();
        user.setUsername(username);
        when(userConverter.convertUsernameToUser(username)).thenReturn(user);
        // when
        TraineeEntity domainModel = traineeConverter.convertUsernameToModel(username);
        // then
        assertThat(domainModel.getUser().getUsername()).isEqualTo(username);
    }

    @Test
    void convertRegistrationRequestToModelTest() {
        //given
        String firstName = RandomStringUtils.randomAlphabetic(5);
        String lastName = RandomStringUtils.randomAlphabetic(5);
        UserEntity user = new UserEntity();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        TraineeRegistrationRequestDTO requestDTO = new TraineeRegistrationRequestDTO();
        requestDTO.setAddress(RandomStringUtils.randomAlphabetic(5));
        requestDTO.setDateOfBirth(LocalDate.now());
        requestDTO.setFirstName(firstName);
        requestDTO.setLastName(lastName);
        when(userConverter.convertRegistrationRequest(requestDTO.getFirstName(), requestDTO.getLastName()))
                .thenReturn(user);
        //when
        TraineeEntity domainModel = traineeConverter.convertRegistrationRequestToModel(requestDTO);
        //then
        assertThat(domainModel.getUser().getFirstName()).isEqualTo(requestDTO.getFirstName());
        assertThat(domainModel.getUser().getLastName()).isEqualTo(requestDTO.getLastName());
        assertThat(domainModel.getDateOfBirth()).isEqualTo(requestDTO.getDateOfBirth());
        assertThat(domainModel.getAddress()).isEqualTo(requestDTO.getAddress());
    }

    @Test
    void convertModelToRegistrationResponseTest() {
        //given
        UserEntity user = new UserEntity();
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        //when
        RegistrationResponseDTO responseDTO = traineeConverter.convertModelToRegistrationResponse(trainee);
        //then
        assertThat(responseDTO.getUsername()).isEqualTo(trainee.getUser().getUsername());
        assertThat(responseDTO.getPassword()).isEqualTo(trainee.getUser().getPassword());
    }

    @Test
    void convertModelToResponseTest() {
        //given
        UserEntity user = new UserEntity();
        user.setFirstName(RandomStringUtils.randomAlphabetic(10));
        user.setLastName(RandomStringUtils.randomAlphabetic(10));
        user.setIsActive(false);
        TrainerEntity trainer = new TrainerEntity();
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        trainee.setAddress(RandomStringUtils.randomAlphabetic(10));
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setTrainers(List.of(trainer));
        when(trainerConverter.convertModelToTraineeTrainer(trainer)).thenReturn(new TraineeTrainerResponseDTO());
        //when
        TraineeResponseDTO responseDTO = traineeConverter.convertModelToResponse(trainee);
        //then
        assertThat(responseDTO.getFirstName()).isEqualTo(trainee.getUser().getFirstName());
        assertThat(responseDTO.getLastName()).isEqualTo(trainee.getUser().getLastName());
        assertThat(responseDTO.getIsActive()).isEqualTo(trainee.getUser().getIsActive());
        assertThat(responseDTO.getDateOfBirth()).isEqualTo(trainee.getDateOfBirth());
        assertThat(responseDTO.getAddress()).isEqualTo(trainee.getAddress());
        assertThat(responseDTO.getTrainers()).hasSize(trainee.getTrainers().size());
    }

    @Test
    void convertUpdateRequestToModelTest() {
        // given
        TraineeUpdateRequestDTO requestDTO = new TraineeUpdateRequestDTO();
        requestDTO.setUsername(RandomStringUtils.randomAlphabetic(10));
        requestDTO.setFirstName(RandomStringUtils.randomAlphabetic(5));
        requestDTO.setLastName(RandomStringUtils.randomAlphabetic(5));
        requestDTO.setIsActive(true);
        requestDTO.setDateOfBirth(LocalDate.now());
        requestDTO.setAddress(RandomStringUtils.randomAlphabetic(10));
        UserEntity updatedUser = new UserEntity();
        updatedUser.setUsername(requestDTO.getUsername());
        updatedUser.setFirstName(requestDTO.getFirstName());
        updatedUser.setLastName(requestDTO.getLastName());
        updatedUser.setIsActive(requestDTO.getIsActive());
        when(userConverter.convertUpdateRequestToModel(requestDTO.getUsername(), requestDTO.getFirstName(),
                requestDTO.getLastName(), requestDTO.getIsActive())).thenReturn(updatedUser);
        // when
        TraineeEntity domainModel = traineeConverter.convertUpdateRequestToModel(requestDTO);
        // then
        assertThat(domainModel.getUser().getUsername()).isEqualTo(requestDTO.getUsername());
        assertThat(domainModel.getUser().getFirstName()).isEqualTo(requestDTO.getFirstName());
        assertThat(domainModel.getUser().getLastName()).isEqualTo(requestDTO.getLastName());
        assertThat(domainModel.getUser().getIsActive()).isEqualTo(requestDTO.getIsActive());
        assertThat(domainModel.getDateOfBirth()).isEqualTo(requestDTO.getDateOfBirth());
        assertThat(domainModel.getAddress()).isEqualTo(requestDTO.getAddress());
    }

    @Test
    void convertModelToUpdateResponseTest() {
        // given
        TraineeEntity trainee = new TraineeEntity();
        trainee.setDateOfBirth(LocalDate.now());
        trainee.setAddress(RandomStringUtils.randomAlphabetic(10));
        UserEntity user = new UserEntity();
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        user.setIsActive(true);
        trainee.setUser(user);
        TrainerEntity trainer = new TrainerEntity();
        trainee.setTrainers(List.of(trainer));
        when(trainerConverter.convertModelToTraineeTrainer(trainer)).thenReturn(new TraineeTrainerResponseDTO());
        // when
        TraineeUpdateResponseDTO responseDTO = traineeConverter.convertModelToUpdateResponse(trainee);
        // then
        assertThat(responseDTO.getUsername()).isEqualTo(trainee.getUser().getUsername());
        assertThat(responseDTO.getFirstName()).isEqualTo(trainee.getUser().getFirstName());
        assertThat(responseDTO.getLastName()).isEqualTo(trainee.getUser().getLastName());
        assertThat(responseDTO.getIsActive()).isEqualTo(trainee.getUser().getIsActive());
        assertThat(responseDTO.getDateOfBirth()).isEqualTo(trainee.getDateOfBirth());
        assertThat(responseDTO.getAddress()).isEqualTo(trainee.getAddress());
        assertThat(responseDTO.getTrainers()).hasSize(trainee.getTrainers().size());
    }

    @Test
    void convertModelToTrainerTraineeTest() {
        // given
        TraineeEntity trainee = new TraineeEntity();
        UserEntity user = new UserEntity();
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        user.setFirstName(RandomStringUtils.randomAlphabetic(5));
        user.setLastName(RandomStringUtils.randomAlphabetic(5));
        trainee.setUser(user);
        // when
        TrainerTraineeResponseDTO responseDTO = traineeConverter.convertModelToTrainerTrainee(trainee);
        // then
        assertThat(responseDTO.getUsername()).isEqualTo(trainee.getUser().getUsername());
        assertThat(responseDTO.getFirstName()).isEqualTo(trainee.getUser().getFirstName());
        assertThat(responseDTO.getLastName()).isEqualTo(trainee.getUser().getLastName());
    }
}
