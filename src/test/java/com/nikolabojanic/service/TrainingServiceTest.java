package com.nikolabojanic.service;

import com.nikolabojanic.dao.TrainingDAO;
import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.TrainingEntity;
import com.nikolabojanic.model.TrainingTypeEntity;
import com.nikolabojanic.validation.TrainingValidation;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingServiceTest {
    @Mock
    private TrainingDAO trainingDAO;
    @Mock
    private TraineeService traineeService;
    @Mock
    private TrainingTypeService trainingTypeService;
    @Mock
    private TrainerService trainerService;
    @Mock
    private UserService userService;
    @Mock
    private TrainingValidation trainingValidation;

    @InjectMocks
    private TrainingService trainingService;

    @Test
    void createTest() {
        TrainerEntity trainer = new TrainerEntity();
        TraineeEntity trainee = new TraineeEntity();
        LocalDate date = LocalDate.of(2023, 11, 22);
        String name = RandomStringUtils.randomAlphabetic(3, 6);
        Double duration = Double.parseDouble(RandomStringUtils.randomNumeric(3, 6));
        TrainingTypeEntity type = new TrainingTypeEntity();
        TrainingEntity training = new TrainingEntity(null, trainee, trainer, name, type, date, duration);
        when(trainingDAO.save(training)).thenReturn(training);
        when(trainingTypeService.findById(training.getType().getId())).thenReturn(new TrainingTypeEntity());

        TrainingEntity saved = trainingService.create(training);
        assertEquals(saved, training);
        verify(trainingValidation).validateCreateTrainingRequest(training);
    }


    @Test
    void findTrainingsByTraineeUsernameAndTimeTest() {
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);
        AuthDTO authDTO = new AuthDTO(username, password);
        LocalDate begin = LocalDate.now();
        LocalDate end = LocalDate.now();
        Long traineeId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        TraineeEntity mockedTrainee = new TraineeEntity();
        mockedTrainee.setId(traineeId);
        when(traineeService.findByUsername(authDTO, username)).thenReturn(mockedTrainee);
        when(trainingDAO.findByTraineeIdAndDate(mockedTrainee.getId(), begin, end)).thenReturn(new ArrayList<>());
//        ArgumentCaptor<AuthDTO> authDtoCaptor = ArgumentCaptor.forClass(AuthDTO.class);
//        Mockito.doNothing().when(userService).authentication(authDtoCaptor.capture());
        List<TrainingEntity> trainings = trainingService.findTrainingsByTraineeUsernameAndTime(authDTO, username, begin, end);

        assertNotNull(trainings);
//        var authValue = authDtoCaptor.getValue();
        verify(userService).authentication(authDTO);
        verify(trainingValidation).validateDate(begin, end);
    }

    @Test
    void findTrainingsByTrainerUsernameAndTimeTest() {
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);
        AuthDTO authDTO = new AuthDTO(username, password);
        LocalDate begin = LocalDate.now();
        LocalDate end = LocalDate.now();
        Long trainerId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        TrainerEntity mockedTrainer = new TrainerEntity();
        mockedTrainer.setId(trainerId);
        when(trainerService.findByUsername(authDTO, username)).thenReturn(mockedTrainer);
        when(trainingDAO.findByTrainerIdAndDate(mockedTrainer.getId(), begin, end)).thenReturn(new ArrayList<>());

        List<TrainingEntity> trainings = trainingService.findTrainingsByTrainerUsernameAndTime(authDTO, username, begin, end);

        assertNotNull(trainings);
        verify(trainingValidation).validateDate(begin, end);
    }
}