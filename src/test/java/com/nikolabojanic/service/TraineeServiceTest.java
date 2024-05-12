package com.nikolabojanic.service;

import com.nikolabojanic.dao.TraineeDAO;
import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.UserEntity;
import com.nikolabojanic.validation.TraineeValidation;
import com.nikolabojanic.validation.UserValidation;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraineeServiceTest {
    @Mock
    private TraineeDAO traineeDAO;
    @Mock
    private UserService userService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private TraineeValidation traineeValidation;
    @Mock
    private UserValidation userValidation;
    @InjectMocks
    private TraineeService traineeService;

    @Test
    void createTraineeProfileTest() {
        String firstName = RandomStringUtils.randomAlphabetic(5, 10);
        String lastName = RandomStringUtils.randomAlphabetic(5, 10);
        UserEntity user = new UserEntity(null, firstName, lastName, null, null, false);
        TraineeEntity mockedTrainee = new TraineeEntity();
        mockedTrainee.setUser(user);
        when(userService.generateUsernameAndPassword(user)).thenReturn(user);
        when(traineeDAO.save(mockedTrainee)).thenReturn(mockedTrainee);

        TraineeEntity createdTrainee = traineeService.createTraineeProfile(mockedTrainee);

        assertEquals(mockedTrainee, createdTrainee);
    }

//    @Test
//    void createTraineeProfileWithNullTest() {
//        TraineeEntity trainee = null;
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            traineeService.createTraineeProfile(trainee);
//        });
//
//        assertEquals("Cannot create trainee profile with null value", exception.getMessage());
//    }
//
//    @Test
//    void createTraineeProfileWithIdTest() {
//        TraineeEntity trainee = new TraineeEntity();
//        trainee.setId(Long.parseLong(RandomStringUtils.randomNumeric(4, 5)));
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            traineeService.createTraineeProfile(trainee);
//        });
//
//        assertEquals("Cannot create trainee profile with an ID", exception.getMessage());
//    }

//    @Test
//    void createTraineeProfileWithNullUserTest() {
//        TraineeEntity trainee = new TraineeEntity();
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            traineeService.createTraineeProfile(trainee);
//        });
//
//        assertEquals("Cannot create trainee profile with null user", exception.getMessage());
//    }

//    @Test
//    void createTraineeProfileWithUserIdTest() {
//        TraineeEntity trainee = new TraineeEntity();
//        UserEntity user = new UserEntity();
//        user.setId(Long.parseLong(RandomStringUtils.randomNumeric(4, 7)));
//        trainee.setUser(user);
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            traineeService.createTraineeProfile(trainee);
//        });
//
//        assertEquals("Cannot create trainee profile with an user ID", exception.getMessage());
//    }

    @Test
    void updateTraineeProfileTest() {
        Long userId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        String firstName = RandomStringUtils.randomAlphabetic(5, 10);
        String lastName = RandomStringUtils.randomAlphabetic(5, 10);
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);
        UserEntity user = new UserEntity(userId, firstName, lastName, username, password, false);
        Long traineeId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        TraineeEntity mockedTrainee = new TraineeEntity();
        mockedTrainee.setUser(user);
        mockedTrainee.setId(traineeId);
        AuthDTO authDTO = new AuthDTO(username, password);
        when(userService.findByUsername(username)).thenReturn(user);
        when(traineeDAO.findById(mockedTrainee.getId())).thenReturn(Optional.of(mockedTrainee));
        when(traineeDAO.save(mockedTrainee)).thenReturn(mockedTrainee);

        TraineeEntity updatedTrainee = traineeService.updateTraineeProfile(authDTO, mockedTrainee);

        assertEquals(mockedTrainee, updatedTrainee);
    }

//    @Test
//    void updateTraineeProfileWithNullTest() {
//        AuthDTO authDTO = new AuthDTO(RandomStringUtils.randomAlphabetic(8, 10), RandomStringUtils.randomAlphabetic(8, 10));
//        TraineeEntity trainee = null;
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            traineeService.updateTraineeProfile(authDTO, trainee);
//        });
//
//        assertEquals("Cannot update a trainee with null value", exception.getMessage());
//    }
//
//    @Test
//    void updateTraineeProfileWithoutIdTest() {
//        AuthDTO authDTO = new AuthDTO(RandomStringUtils.randomAlphabetic(8, 10), RandomStringUtils.randomAlphabetic(8, 10));
//        TraineeEntity trainee = new TraineeEntity();
//        UserEntity user = new UserEntity();
//        trainee.setUser(user);
//
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            traineeService.updateTraineeProfile(authDTO, trainee);
//        });
//
//        assertEquals("Cannot update trainee with null id", exception.getMessage());
//    }
//
//    @Test
//    void updateTraineeProfileWithNullUserTest() {
//        AuthDTO authDTO = new AuthDTO(RandomStringUtils.randomAlphabetic(8, 10), RandomStringUtils.randomAlphabetic(8, 10));
//        TraineeEntity trainee = new TraineeEntity();
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            traineeService.updateTraineeProfile(authDTO, trainee);
//        });
//
//        assertEquals("Cannot update trainee profile with null user", exception.getMessage());
//    }
//
//    @Test
//    void updateTraineeProfileWithoutUserIdTest() {
//        AuthDTO authDTO = new AuthDTO(RandomStringUtils.randomAlphabetic(8, 10), RandomStringUtils.randomAlphabetic(8, 10));
//        TraineeEntity trainee = new TraineeEntity();
//        trainee.setId(Long.parseLong(RandomStringUtils.randomNumeric(4, 5)));
//        UserEntity user = new UserEntity();
//        trainee.setUser(user);
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            traineeService.updateTraineeProfile(authDTO, trainee);
//        });
//
//        assertEquals("Cannot update user without an ID", exception.getMessage());
//    }

    @Test
    void findByIdTest() {
        Long traineeId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(traineeDAO.findById(traineeId)).thenReturn(Optional.of(new TraineeEntity()));

        TraineeEntity fetchedTrainee = traineeService.findById(traineeId);

        assertNotNull(fetchedTrainee);
    }

    @Test
    void findByUsernameTest() {
        Long userId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        String firstName = RandomStringUtils.randomAlphabetic(5, 10);
        String lastName = RandomStringUtils.randomAlphabetic(5, 10);
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);
        UserEntity user = new UserEntity(userId, firstName, lastName, username, password, false);
        AuthDTO authDTO = new AuthDTO(username, password);
        TraineeEntity mockedTrainee = new TraineeEntity();
        when(userService.findByUsername(username)).thenReturn(user);
        when(traineeDAO.findByUserId(userId)).thenReturn(Optional.of(mockedTrainee));

        TraineeEntity trainee = traineeService.findByUsername(authDTO, username);

        assertEquals(mockedTrainee, trainee);
    }

    @Test
    void changeTraineePasswordTest() {
        Long userId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        String firstName = RandomStringUtils.randomAlphabetic(5, 10);
        String lastName = RandomStringUtils.randomAlphabetic(5, 10);
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);
        UserEntity user = new UserEntity(userId, firstName, lastName, username, password, false);
        AuthDTO authDTO = new AuthDTO(username, password);
        Long traineeId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        TraineeEntity mockedTrainee = new TraineeEntity();
        mockedTrainee.setId(traineeId);
        mockedTrainee.setUser(user);
        String newPw = RandomStringUtils.randomAlphabetic(8, 10);
        when(userService.findByUsername(username)).thenReturn(user);
        when(traineeDAO.findById(mockedTrainee.getId())).thenReturn(Optional.of(mockedTrainee));

        traineeService.changeTraineePassword(authDTO, mockedTrainee, newPw);
    }

    @Test
    void deleteByUsernameTest() {
        Long userId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        String firstName = RandomStringUtils.randomAlphabetic(5, 10);
        String lastName = RandomStringUtils.randomAlphabetic(5, 10);
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);
        UserEntity user = new UserEntity(userId, firstName, lastName, username, password, false);
        AuthDTO authDTO = new AuthDTO(username, password);
        Long traineeId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        TraineeEntity mockedTrainee = new TraineeEntity();
        mockedTrainee.setId(traineeId);
        mockedTrainee.setUser(user);
        when(userService.findByUsername(username)).thenReturn(user);
        when(traineeDAO.findByUserId(user.getId())).thenReturn(Optional.of(mockedTrainee));

        traineeService.deleteByUsername(authDTO, username);
    }

    @Test
    void updateTraineeTrainersTest() {
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);
        AuthDTO authDTO = new AuthDTO(username, password);
        Long traineeId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        TraineeEntity mockedTrainee = new TraineeEntity();
        mockedTrainee.setId(traineeId);
        Long trainerId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        TrainerEntity mockedTrainer = new TrainerEntity();
        mockedTrainer.setId(trainerId);
        List<TrainerEntity> trainers = new ArrayList<>();
        trainers.add(mockedTrainer);
        when(trainerService.findById(mockedTrainer.getId())).thenReturn(mockedTrainer);
        when(traineeDAO.findById(mockedTrainee.getId())).thenReturn(Optional.of(mockedTrainee));
        when(traineeDAO.saveTrainers(mockedTrainee, trainers)).thenReturn(mockedTrainee);

        TraineeEntity updatedTrainee = traineeService.updateTraineeTrainers(authDTO, mockedTrainee, trainers);

        assertEquals(mockedTrainee, updatedTrainee);
    }

    @Test
    void changeActiveStatusTest() {
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);
        AuthDTO authDTO = new AuthDTO(username, password);
        Long traineeId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(traineeDAO.findById(traineeId)).thenReturn(Optional.of(new TraineeEntity()));

        traineeService.changeActiveStatus(authDTO, traineeId);
    }

}