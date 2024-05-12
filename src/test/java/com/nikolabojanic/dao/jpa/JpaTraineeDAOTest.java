package com.nikolabojanic.dao.jpa;

import com.nikolabojanic.model.TraineeEntity;
import com.nikolabojanic.model.TrainerEntity;
import com.nikolabojanic.model.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaTraineeDAOTest {
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private JpaTraineeDAO jpaTraineeDAO;

    @Test
    void findByIdTest() {
        Long traineeId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(entityManager.find(TraineeEntity.class, traineeId)).thenReturn(new TraineeEntity());

        Optional<TraineeEntity> receivedTrainee = jpaTraineeDAO.findById(traineeId);

        assertTrue(receivedTrainee.isPresent());
    }

    @Test
    void findByNonExistingIdTest() {
        Long traineeId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(entityManager.find(TraineeEntity.class, traineeId)).thenReturn(null);

        Optional<TraineeEntity> receivedTrainee = jpaTraineeDAO.findById(traineeId);

        assertTrue(receivedTrainee.isEmpty());
    }

    @Test
    void deleteTest() {
        Long traineeId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(entityManager.find(TraineeEntity.class, traineeId)).thenReturn(new TraineeEntity());

        jpaTraineeDAO.delete(traineeId);
    }

    @Test
    void saveTest() {
        TraineeEntity trainee = new TraineeEntity();
        UserEntity user = new UserEntity();
        trainee.setUser(user);
        when(entityManager.merge(user)).thenReturn(user);
        when(entityManager.merge(trainee)).thenReturn(trainee);

        TraineeEntity receivedTrainee = jpaTraineeDAO.save(trainee);

        assertEquals(trainee, receivedTrainee);
    }

    @Test
    void findByUserIdTest() {
        TypedQuery<TraineeEntity> query = mock(TypedQuery.class);
        Long userId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(entityManager.createQuery("from TraineeEntity where user.id = :userId", TraineeEntity.class)).thenReturn(query);
        when(query.setParameter("userId", userId)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(new TraineeEntity());

        Optional<TraineeEntity> receivedTrainee = jpaTraineeDAO.findByUserId(userId);

        assertTrue(receivedTrainee.isPresent());
    }

    @Test
    void findByNonExistingUserIdTest() {
        TypedQuery<TraineeEntity> query = mock(TypedQuery.class);
        Long userId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(entityManager.createQuery("from TraineeEntity where user.id = :userId", TraineeEntity.class)).thenReturn(query);
        when(query.setParameter("userId", userId)).thenReturn(query);
        when(query.getSingleResult()).thenThrow(NoResultException.class);

        Optional<TraineeEntity> receivedTrainee = jpaTraineeDAO.findByUserId(userId);

        assertTrue(receivedTrainee.isEmpty());
    }

    @Test
    void saveTrainersTest() {
        List<TrainerEntity> trainers = List.of(new TrainerEntity(), new TrainerEntity());
        TraineeEntity trainee = new TraineeEntity();
        when(entityManager.merge(trainee)).thenReturn(trainee);

        TraineeEntity receivedTrainee = jpaTraineeDAO.saveTrainers(trainee, trainers);

        assertEquals(receivedTrainee.getTrainers(), trainers);
    }

    @Test
    void changeActiveStatusTest() {
        UserEntity user = new UserEntity();
        user.setId(Long.parseLong(RandomStringUtils.randomNumeric(3, 6)));
        user.setIsActive(true);
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        when(entityManager.find(UserEntity.class, trainee.getUser().getId())).thenReturn(user);

        jpaTraineeDAO.changeActiveStatus(trainee);
    }
}
