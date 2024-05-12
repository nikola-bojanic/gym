package com.nikolabojanic.config;

import com.nikolabojanic.model.Trainee;
import com.nikolabojanic.model.Trainer;
import com.nikolabojanic.model.TrainingType;
import com.nikolabojanic.model.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
class TraineeFileStorageTest {
    @Mock
    private TrainerFileStorage trainerFileStorage;
    @Mock
    private UserFileStorage userFileStorage;
    @InjectMocks
    private TraineeFileStorage traineeFileStorage;


    @Test
    void testReadAndWriteTrainees() throws IOException {
        Map<Long, Trainee> writeTrainees = getWriteTrainees();
        traineeFileStorage.setTraineesPath("build/trainees.csv");
        traineeFileStorage.setTrainees(writeTrainees);
        when(userFileStorage.getUsers()).thenReturn(getWriteUsers());
        traineeFileStorage.writeTrainees();
        traineeFileStorage.readTrainees();


        checkValues(traineeFileStorage.getTrainees());
    }

    @Test
    void readAndWriteTraineesTrainersTest() throws IOException {
        Map<Long, Trainee> writeTrainees = getWriteTrainees();
        Map<Long, Trainer> writeTrainers = getWriteTrainers();
        Set<Trainer> trainee1 = new HashSet<>();
        Set<Trainer> trainee2 = new HashSet<>();
        Set<Trainer> trainee3 = new HashSet<>();
        Set<Trainee> trainer1 = new HashSet<>();
        Set<Trainee> trainer2 = new HashSet<>();
        Set<Trainee> trainer3 = new HashSet<>();
        trainee1.add(writeTrainers.get(1L));
        trainee1.add(writeTrainers.get(2L));
        trainee2.add(writeTrainers.get(2L));
        trainee2.add(writeTrainers.get(3L));
        trainee3.add(writeTrainers.get(1L));
        trainee3.add(writeTrainers.get(3L));

        trainer1.add(writeTrainees.get(1L));
        trainer1.add(writeTrainees.get(3L));
        trainer2.add(writeTrainees.get(1L));
        trainer2.add(writeTrainees.get(2L));
        trainer3.add(writeTrainees.get(2L));
        trainer3.add(writeTrainees.get(3L));
        writeTrainees.get(1L).setTrainers(trainee1);
        writeTrainees.get(2L).setTrainers(trainee2);
        writeTrainees.get(3L).setTrainers(trainee3);
        writeTrainers.get(1L).setTrainees(trainer1);
        writeTrainers.get(2L).setTrainees(trainer2);
        writeTrainers.get(3L).setTrainees(trainer3);
        when(trainerFileStorage.getTrainers()).thenReturn(writeTrainers);
        traineeFileStorage.setTraineesTrainersPath("build/trainees_trainers.csv");
        traineeFileStorage.setTrainees(writeTrainees);

        traineeFileStorage.writeTraineesTrainers();
        traineeFileStorage.readTraineesTrainers();

        Map<Long, Trainee> readTrainees = traineeFileStorage.getTrainees();
        assertEquals(3, readTrainees.size());
        assertEquals(trainee1, readTrainees.get(1L).getTrainers());
        assertEquals(trainee2, readTrainees.get(2L).getTrainers());
        assertEquals(trainee3, readTrainees.get(3L).getTrainers());

        Map<Long, Trainer> readTrainers = trainerFileStorage.getTrainers();
        assertEquals(3, readTrainers.size());
        assertEquals(trainer1, readTrainers.get(1L).getTrainees());
        assertEquals(trainer2, readTrainers.get(2L).getTrainees());
        assertEquals(trainer3, readTrainers.get(3L).getTrainees());
    }


    //random utils apache commons lang
    private Map<Long, Trainee> getWriteTrainees() {
        Map<Long, Trainee> writeTrainees = new HashMap<>();
        Trainee trainee1 = new Trainee(1L, LocalDate.of(2021, 1, 1), "address1", new User(1L));
        Trainee trainee2 = new Trainee(2L, LocalDate.of(2022, 2, 2), "address2", new User(2L));
        Trainee trainee3 = new Trainee(3L, LocalDate.of(2023, 3, 3), "address3", new User(3L));
        writeTrainees.put(trainee1.getId(), trainee1);
        writeTrainees.put(trainee2.getId(), trainee2);
        writeTrainees.put(trainee3.getId(), trainee3);
        return writeTrainees;
    }

    private Map<Long, Trainer> getWriteTrainers() {
        Map<Long, Trainer> writeTrainers = new HashMap<>();
        Trainer trainer1 = new Trainer(1L, new User(1L), new TrainingType(1L));
        Trainer trainer2 = new Trainer(2L, new User(2L), new TrainingType(2L));
        Trainer trainer3 = new Trainer(3L, new User(3L), new TrainingType(3L));
        writeTrainers.put(trainer1.getId(), trainer1);
        writeTrainers.put(trainer2.getId(), trainer2);
        writeTrainers.put(trainer3.getId(), trainer3);
        return writeTrainers;
    }

    private Map<Long, User> getWriteUsers() {
        Map<Long, User> writeUsers = new HashMap<>();
        User user1 = new User(1L);
        User user2 = new User(2L);
        User user3 = new User(3L);
        writeUsers.put(user1.getId(), user1);
        writeUsers.put(user2.getId(), user2);
        writeUsers.put(user3.getId(), user3);
        return writeUsers;
    }

    private void checkValues(Map<Long, Trainee> readTrainees) {
        assertEquals(3, readTrainees.size());
        assertEquals(1L, readTrainees.get(1L).getId());
        assertEquals(LocalDate.parse("2021-01-01"), readTrainees.get(1L).getDateOfBirth());
        assertEquals("address1", readTrainees.get(1L).getAddress());
        assertEquals(1L, readTrainees.get(1L).getUser().getId());

        assertEquals(1L, readTrainees.get(1L).getId());
        assertEquals(LocalDate.parse("2022-02-02"), readTrainees.get(2L).getDateOfBirth());
        assertEquals("address2", readTrainees.get(2L).getAddress());
        assertEquals(2L, readTrainees.get(2L).getUser().getId());
        assertEquals(2L, readTrainees.get(2L).getId());

        assertEquals(3L, readTrainees.get(3L).getId());
        assertEquals(LocalDate.parse("2023-03-03"), readTrainees.get(3L).getDateOfBirth());
        assertEquals("address3", readTrainees.get(3L).getAddress());
        assertEquals(3L, readTrainees.get(3L).getUser().getId());
    }
}
