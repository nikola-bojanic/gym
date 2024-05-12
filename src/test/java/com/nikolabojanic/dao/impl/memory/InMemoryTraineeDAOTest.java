package com.nikolabojanic.dao.impl.memory;

import com.nikolabojanic.config.TraineeFileStorage;
import com.nikolabojanic.config.TrainerFileStorage;
import com.nikolabojanic.model.Trainee;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class InMemoryTraineeDAOTest {
    @Mock
    private TraineeFileStorage traineeFileStorage;
    @Mock
    private TrainerFileStorage trainerFileStorage;
    @InjectMocks
    private InMemoryTraineeDAO inMemoryTraineeDAO;


    @Test
    void getAllTraineesTest() {
        Map<Long, Trainee> testTrainees = new HashMap<>();
        Trainee trainee1 = new Trainee(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)));
        Trainee trainee2 = new Trainee(Long.parseLong(RandomStringUtils.randomNumeric(2, 3)));
        testTrainees.put(1L, trainee1);
        testTrainees.put(2L, trainee2);
        when(traineeFileStorage.getTrainees()).thenReturn(testTrainees);

        List<Trainee> receivedTrainees = inMemoryTraineeDAO.getAll();

        assertEquals(testTrainees.size(), receivedTrainees.size());
        assertEquals(trainee1, receivedTrainees.get(0));
        assertEquals(trainee2, receivedTrainees.get(1));
    }

    @Test
    void getTraineeByIdTest() {
        Map<Long, Trainee> testTrainees = new HashMap<>();
        Trainee trainee = new Trainee(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)));
        testTrainees.put(trainee.getId(), trainee);
        when(traineeFileStorage.getTrainees()).thenReturn(testTrainees);

        Trainee received = inMemoryTraineeDAO.findById(trainee.getId()).get();

        assertEquals(trainee, received);
    }

    @Test
    void deleteTest() {
        Map<Long, Trainee> testTrainees = new HashMap<>();
        Trainee trainee = new Trainee(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)));
        testTrainees.put(trainee.getId(), trainee);
        when(traineeFileStorage.getTrainees()).thenReturn(testTrainees);

        assertNull(inMemoryTraineeDAO.delete(trainee.getId()));
    }

    @Test
    void saveWithIdTest() {
        Trainee trainee = new Trainee(Long.parseLong(RandomStringUtils.randomNumeric(1, 2)));

        Trainee saved = inMemoryTraineeDAO.save(trainee);

        assertEquals(trainee, saved);
    }

    @Test
    void saveWithoutId() {
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(1, 2));
        Trainee trainee = new Trainee();
        when(traineeFileStorage.getMaxId()).thenReturn(id);
        Trainee saved = inMemoryTraineeDAO.save(trainee);

        assertEquals(id, saved.getId());
    }
}
