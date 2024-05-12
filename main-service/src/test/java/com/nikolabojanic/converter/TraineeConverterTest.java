package com.nikolabojanic.converter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.nikolabojanic.domain.TraineeDomain;
import com.nikolabojanic.dto.RegistrationResponseDto;
import com.nikolabojanic.dto.TraineeRegistrationRequestDto;
import com.nikolabojanic.dto.TraineeResponseDto;
import com.nikolabojanic.dto.TraineeTrainerResponseDto;
import com.nikolabojanic.dto.TraineeUpdateRequestDto;
import com.nikolabojanic.dto.TraineeUpdateResponseDto;
import com.nikolabojanic.dto.TrainerTraineeResponseDto;
import com.nikolabojanic.entity.TraineeEntity;
import com.nikolabojanic.entity.TrainerEntity;
import com.nikolabojanic.entity.UserEntity;
import java.time.LocalDate;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        TraineeRegistrationRequestDto requestDto = new TraineeRegistrationRequestDto();
        requestDto.setAddress(RandomStringUtils.randomAlphabetic(5));
        requestDto.setDateOfBirth(LocalDate.now());
        requestDto.setFirstName(firstName);
        requestDto.setLastName(lastName);
        when(userConverter.convertRegistrationRequest(requestDto.getFirstName(), requestDto.getLastName()))
            .thenReturn(user);
        //when
        TraineeEntity domainModel = traineeConverter.convertRegistrationRequestToModel(requestDto);
        //then
        assertThat(domainModel.getUser().getFirstName()).isEqualTo(requestDto.getFirstName());
        assertThat(domainModel.getUser().getLastName()).isEqualTo(requestDto.getLastName());
        assertThat(domainModel.getDateOfBirth()).isEqualTo(requestDto.getDateOfBirth());
        assertThat(domainModel.getAddress()).isEqualTo(requestDto.getAddress());
    }

    @Test
    void convertModelToRegistrationResponseTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(10);
        String password = RandomStringUtils.randomAlphabetic(10);
        TraineeDomain trainee = new TraineeDomain();
        trainee.setUsername(username);
        trainee.setPassword(password);
        //when
        RegistrationResponseDto responseDto = traineeConverter.convertModelToRegistrationResponse(trainee);
        //then
        assertThat(responseDto.getUsername()).isEqualTo(trainee.getUsername());
        assertThat(responseDto.getPassword()).isEqualTo(trainee.getPassword());
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
        when(trainerConverter.convertModelToTraineeTrainer(trainer)).thenReturn(new TraineeTrainerResponseDto());
        //when
        TraineeResponseDto responseDto = traineeConverter.convertModelToResponse(trainee);
        //then
        assertThat(responseDto.getFirstName()).isEqualTo(trainee.getUser().getFirstName());
        assertThat(responseDto.getLastName()).isEqualTo(trainee.getUser().getLastName());
        assertThat(responseDto.getIsActive()).isEqualTo(trainee.getUser().getIsActive());
        assertThat(responseDto.getDateOfBirth()).isEqualTo(trainee.getDateOfBirth());
        assertThat(responseDto.getAddress()).isEqualTo(trainee.getAddress());
        assertThat(responseDto.getTrainers()).hasSize(trainee.getTrainers().size());
    }

    @Test
    void convertUpdateRequestToModelTest() {
        // given
        TraineeUpdateRequestDto requestDto = new TraineeUpdateRequestDto();
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(10));
        requestDto.setFirstName(RandomStringUtils.randomAlphabetic(5));
        requestDto.setLastName(RandomStringUtils.randomAlphabetic(5));
        requestDto.setIsActive(true);
        requestDto.setDateOfBirth(LocalDate.now());
        requestDto.setAddress(RandomStringUtils.randomAlphabetic(10));
        UserEntity updatedUser = new UserEntity();
        updatedUser.setUsername(requestDto.getUsername());
        updatedUser.setFirstName(requestDto.getFirstName());
        updatedUser.setLastName(requestDto.getLastName());
        updatedUser.setIsActive(requestDto.getIsActive());
        when(userConverter.convertUpdateRequestToModel(requestDto.getUsername(), requestDto.getFirstName(),
            requestDto.getLastName(), requestDto.getIsActive())).thenReturn(updatedUser);
        // when
        TraineeEntity domainModel = traineeConverter.convertUpdateRequestToModel(requestDto);
        // then
        assertThat(domainModel.getUser().getUsername()).isEqualTo(requestDto.getUsername());
        assertThat(domainModel.getUser().getFirstName()).isEqualTo(requestDto.getFirstName());
        assertThat(domainModel.getUser().getLastName()).isEqualTo(requestDto.getLastName());
        assertThat(domainModel.getUser().getIsActive()).isEqualTo(requestDto.getIsActive());
        assertThat(domainModel.getDateOfBirth()).isEqualTo(requestDto.getDateOfBirth());
        assertThat(domainModel.getAddress()).isEqualTo(requestDto.getAddress());
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
        when(trainerConverter.convertModelToTraineeTrainer(trainer)).thenReturn(new TraineeTrainerResponseDto());
        // when
        TraineeUpdateResponseDto responseDto = traineeConverter.convertModelToUpdateResponse(trainee);
        // then
        assertThat(responseDto.getUsername()).isEqualTo(trainee.getUser().getUsername());
        assertThat(responseDto.getFirstName()).isEqualTo(trainee.getUser().getFirstName());
        assertThat(responseDto.getLastName()).isEqualTo(trainee.getUser().getLastName());
        assertThat(responseDto.getIsActive()).isEqualTo(trainee.getUser().getIsActive());
        assertThat(responseDto.getDateOfBirth()).isEqualTo(trainee.getDateOfBirth());
        assertThat(responseDto.getAddress()).isEqualTo(trainee.getAddress());
        assertThat(responseDto.getTrainers()).hasSize(trainee.getTrainers().size());
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
        TrainerTraineeResponseDto responseDto = traineeConverter.convertModelToTrainerTrainee(trainee);
        // then
        assertThat(responseDto.getUsername()).isEqualTo(trainee.getUser().getUsername());
        assertThat(responseDto.getFirstName()).isEqualTo(trainee.getUser().getFirstName());
        assertThat(responseDto.getLastName()).isEqualTo(trainee.getUser().getLastName());
    }
}
