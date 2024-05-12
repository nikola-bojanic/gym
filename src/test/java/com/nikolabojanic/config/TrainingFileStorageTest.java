package com.nikolabojanic.config;

import com.nikolabojanic.model.Trainee;
import com.nikolabojanic.model.Trainer;
import com.nikolabojanic.model.Training;
import com.nikolabojanic.model.TrainingType;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainingFileStorageTest {
    @Mock
    private TrainerFileStorage trainerFileStorage;
    @Mock
    private TraineeFileStorage traineeFileStorage;
    @InjectMocks
    private TrainingFileStorage trainingFileStorage;


    @Test
    void readAndWriteTrainingsTest() throws IOException {
        Trainer trainer1 = new Trainer(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)));
        Trainer trainer2 = new Trainer(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)));
        Trainer trainer3 = new Trainer(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)));
        Map<Long, Trainer> trainers = new HashMap<>();
        trainers.put(trainer1.getId(), trainer1);
        trainers.put(trainer2.getId(), trainer2);
        trainers.put(trainer3.getId(), trainer3);
        Trainee trainee1 = new Trainee(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)));
        Trainee trainee2 = new Trainee(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)));
        Trainee trainee3 = new Trainee(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)));
        Map<Long, Trainee> trainees = new HashMap<>();
        trainees.put(trainee1.getId(), trainee1);
        trainees.put(trainee2.getId(), trainee2);
        trainees.put(trainee3.getId(), trainee3);
        TrainingType type1 = new TrainingType(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)));
        TrainingType type2 = new TrainingType(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)));
        TrainingType type3 = new TrainingType(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)));
        Training training1 = new Training(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)),
                trainee1,
                trainer1,
                RandomStringUtils.randomAlphabetic(3, 5),
                type1,
                LocalDate.of(2021, 01, 01),
                Double.parseDouble(RandomStringUtils.randomNumeric(1, 2)));
        Training training2 = new Training(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)),
                trainee2,
                trainer2,
                RandomStringUtils.randomAlphabetic(3, 5),
                type2,
                LocalDate.of(2022, 02, 02),
                Double.parseDouble(RandomStringUtils.randomNumeric(1, 2)));
        Training training3 = new Training(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)),
                trainee3,
                trainer3,
                RandomStringUtils.randomAlphabetic(3, 5),
                type3,
                LocalDate.of(2023, 03, 03),
                Double.parseDouble(RandomStringUtils.randomNumeric(1, 2)));
        Map<Long, Training> writeTrainings = new HashMap<>();
        writeTrainings.put(training1.getId(), training1);
        writeTrainings.put(training2.getId(), training2);
        writeTrainings.put(training3.getId(), training3);
        when(trainerFileStorage.getTrainers()).thenReturn(trainers);
        when(traineeFileStorage.getTrainees()).thenReturn(trainees);
        String trainingsPath = "build/trainings.csv";
        trainingFileStorage.setTrainingsPath(trainingsPath);
        trainingFileStorage.setTrainings(writeTrainings);

        trainingFileStorage.writeTrainings();
        trainingFileStorage.readTrainings();

        Map<Long, Training> readTrainings = trainingFileStorage.getTrainings();
        assertEquals(writeTrainings.size(), readTrainings.size());
        assertEquals(writeTrainings.get(training1.getId()), readTrainings.get(training1.getId()));
        assertEquals(writeTrainings.get(training2.getId()), readTrainings.get(training2.getId()));
        assertEquals(writeTrainings.get(training3.getId()), readTrainings.get(training3.getId()));
    }
}
