package com.nikolabojanic.config;

import com.nikolabojanic.model.Trainer;
import com.nikolabojanic.model.TrainingType;
import com.nikolabojanic.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TrainerFileStorageTest {
    @Mock
    private UserFileStorage userFileStorage;
    @InjectMocks
    private TrainerFileStorage trainerFileStorage;

    @Test
    void readAndWriteTrainersTest() throws IOException {
        Map<Long, Trainer> writeTrainers = new HashMap<>();
        Map<Long, User> testUsers = new HashMap<>();
        User user1 = new User(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)));
        User user2 = new User(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)));
        User user3 = new User(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)));
        testUsers.put(user1.getId(), user1);
        testUsers.put(user2.getId(), user2);
        testUsers.put(user3.getId(), user3);
        TrainingType type1 = new TrainingType(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)));
        TrainingType type2 = new TrainingType(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)));
        TrainingType type3 = new TrainingType(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)));
        Trainer trainer1 = new Trainer(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)), user1, type1);
        Trainer trainer2 = new Trainer(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)), user2, type2);
        Trainer trainer3 = new Trainer(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)), user3, type3);
        writeTrainers.put(trainer1.getId(), trainer1);
        writeTrainers.put(trainer2.getId(), trainer2);
        writeTrainers.put(trainer3.getId(), trainer3);
        String trainersPath = "build/trainers.csv";
        trainerFileStorage.setTrainersPath(trainersPath);
        trainerFileStorage.setTrainers(writeTrainers);
        when(userFileStorage.getUsers()).thenReturn(testUsers);

        trainerFileStorage.writeTrainers();
        trainerFileStorage.readTrainers();

        Map<Long, Trainer> readTrainers = trainerFileStorage.getTrainers();
        assertEquals(writeTrainers.size(), readTrainers.size());
        assertEquals(writeTrainers.get(trainer1.getId()), readTrainers.get(trainer1.getId()));
        assertEquals(writeTrainers.get(trainer2.getId()), readTrainers.get(trainer2.getId()));
        assertEquals(writeTrainers.get(trainer3.getId()), readTrainers.get(trainer3.getId()));
    }

}
