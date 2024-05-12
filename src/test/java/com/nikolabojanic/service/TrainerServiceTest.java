package com.nikolabojanic.service;

import com.nikolabojanic.dao.TrainerDAO;
import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.UserEntity;
import com.nikolabojanic.validation.TrainerValidation;
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
class TrainerServiceTest {
    @Mock
    private TrainerDAO trainerDAO;
    @Mock
    private UserService userService;
    @Mock
    private TrainingTypeService trainingTypeService;
    @Mock
    private TrainerValidation trainerValidation;
    @Mock
    private UserValidation userValidation;
    @InjectMocks
    private TrainerService trainerService;

    @Test
    void createTrainerProfileTest() {
        String firstName = RandomStringUtils.randomAlphabetic(5, 10);
        String lastName = RandomStringUtils.randomAlphabetic(5, 10);
        UserEntity user = new UserEntity(null, firstName, lastName, null, null, false);
        TrainerEntity mockedTrainer = new TrainerEntity();
        mockedTrainer.setUser(user);
        when(trainerDAO.save(mockedTrainer)).thenReturn(mockedTrainer);
        when(userService.generateUsernameAndPassword(user)).thenReturn(user);

        TrainerEntity createdTrainer = trainerService.createTrainerProfile(mockedTrainer);

        assertEquals(mockedTrainer, createdTrainer);
    }


    @Test
    void updateTrainerProfileTest() {
        Long userId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        String firstName = RandomStringUtils.randomAlphabetic(5, 10);
        String lastName = RandomStringUtils.randomAlphabetic(5, 10);
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);
        UserEntity user = new UserEntity(userId, firstName, lastName, username, password, false);
        Long trainerId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        TrainerEntity mockedTrainer = new TrainerEntity();
        mockedTrainer.setUser(user);
        mockedTrainer.setId(trainerId);
        AuthDTO authDTO = new AuthDTO(username, password);
        when(userService.findByUsername(username)).thenReturn(user);
        when(trainerDAO.findById(mockedTrainer.getId())).thenReturn(Optional.of(mockedTrainer));
        when(trainerDAO.save(mockedTrainer)).thenReturn(mockedTrainer);

        TrainerEntity updatedTrainer = trainerService.updateTrainerProfile(authDTO, mockedTrainer);

        assertEquals(mockedTrainer, updatedTrainer);
    }


    @Test
    void findByIdTest() {
        Long trainerId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(trainerDAO.findById(trainerId)).thenReturn(Optional.of(new TrainerEntity()));

        TrainerEntity fetchedTrainer = trainerService.findById(trainerId);

        assertNotNull(fetchedTrainer);
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
        TrainerEntity mockedTrainer = new TrainerEntity();
        when(userService.findByUsername(username)).thenReturn(user);
        when(trainerDAO.findByUserId(userId)).thenReturn(Optional.of(mockedTrainer));

        TrainerEntity trainer = trainerService.findByUsername(authDTO, username);

        assertEquals(mockedTrainer, trainer);
    }

    @Test
    void changeTrainerPasswordTest() {
        Long userId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        String firstName = RandomStringUtils.randomAlphabetic(5, 10);
        String lastName = RandomStringUtils.randomAlphabetic(5, 10);
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);
        UserEntity user = new UserEntity(userId, firstName, lastName, username, password, false);
        AuthDTO authDTO = new AuthDTO(username, password);
        Long trainerId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        TrainerEntity mockedTrainer = new TrainerEntity();
        mockedTrainer.setId(trainerId);
        mockedTrainer.setUser(user);
        String newPw = RandomStringUtils.randomAlphabetic(8, 10);
        when(userService.findByUsername(username)).thenReturn(user);
        when(trainerDAO.findById(trainerId)).thenReturn(Optional.of(mockedTrainer));

        trainerService.changeTrainerPassword(authDTO, mockedTrainer, newPw);
    }

    @Test
    void findActiveForOtherTraineesTest() {
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);
        AuthDTO authDTO = new AuthDTO(username, password);
        Long traineeId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(trainerDAO.findActiveForOtherTrainees(traineeId)).thenReturn(new ArrayList<>());

        List<TrainerEntity> receivedTrainers = trainerService.findActiveForOtherTrainees(authDTO, traineeId);

        assertNotNull(receivedTrainers);
    }

    @Test
    void changeActiveStatusTest() {
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);
        AuthDTO authDTO = new AuthDTO(username, password);
        Long trainerId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(trainerDAO.findById(trainerId)).thenReturn(Optional.of(new TrainerEntity()));

        trainerService.changeActiveStatus(authDTO, trainerId);
    }

}
