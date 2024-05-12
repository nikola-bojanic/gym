package com.nikolabojanic.dao.jpa;

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
class JpaTrainerDAOTest {
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private JpaTrainerDAO jpaTrainerDAO;

    @Test
    void findByIdTest() {
        Long trainerId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(entityManager.find(TrainerEntity.class, trainerId)).thenReturn(new TrainerEntity());

        Optional<TrainerEntity> receivedTrainer = jpaTrainerDAO.findById(trainerId);

        assertTrue(receivedTrainer.isPresent());
    }

    @Test
    void findByNonExistingIdTest() {
        Long trainerId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(entityManager.find(TrainerEntity.class, trainerId)).thenReturn(null);

        Optional<TrainerEntity> receivedTrainer = jpaTrainerDAO.findById(trainerId);

        assertTrue(receivedTrainer.isEmpty());
    }

    @Test
    void saveTest() {
        TrainerEntity trainer = new TrainerEntity();
        UserEntity user = new UserEntity();
        trainer.setUser(user);
        when(entityManager.merge(user)).thenReturn(user);
        when(entityManager.merge(trainer)).thenReturn(trainer);

        TrainerEntity receivedTrainer = jpaTrainerDAO.save(trainer);

        assertEquals(trainer, receivedTrainer);
    }

    @Test
    void findByUserIdTest() {
        TypedQuery<TrainerEntity> query = mock(TypedQuery.class);
        Long userId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(entityManager.createQuery("from TrainerEntity where user.id = :userId", TrainerEntity.class)).thenReturn(query);
        when(query.setParameter("userId", userId)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(new TrainerEntity());

        Optional<TrainerEntity> receivedTrainer = jpaTrainerDAO.findByUserId(userId);

        assertTrue(receivedTrainer.isPresent());
    }

    @Test
    void findByNonExistingUserIdTest() {
        TypedQuery<TrainerEntity> query = mock(TypedQuery.class);
        Long userId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(entityManager.createQuery("from TrainerEntity where user.id = :userId", TrainerEntity.class)).thenReturn(query);
        when(query.setParameter("userId", userId)).thenReturn(query);
        when(query.getSingleResult()).thenThrow(NoResultException.class);

        Optional<TrainerEntity> receivedTrainer = jpaTrainerDAO.findByUserId(userId);

        assertTrue(receivedTrainer.isEmpty());
    }

    @Test
    void findActiveForOtherTraineesTest() {
        TypedQuery<TrainerEntity> query = mock(TypedQuery.class);
        Long traineeId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(entityManager.createQuery("from TrainerEntity t where " +
                        "t.user.isActive = true and " +
                        "(t.id not in(select tt.id from TrainerEntity tt left join tt.trainees trainee WHERE trainee.id = :traineeId))",
                TrainerEntity.class)).thenReturn(query);
        when(query.setParameter("traineeId", traineeId)).thenReturn(query);
        List<TrainerEntity> trainers = List.of(new TrainerEntity(), new TrainerEntity());
        when(query.getResultList()).thenReturn(trainers);

        List<TrainerEntity> receivedTrainers = jpaTrainerDAO.findActiveForOtherTrainees(traineeId);

        assertEquals(trainers, receivedTrainers);
    }


    @Test
    void changeActiveStatusTest() {
        UserEntity user = new UserEntity();
        user.setId(Long.parseLong(RandomStringUtils.randomNumeric(3, 6)));
        user.setIsActive(true);
        TrainerEntity trainer = new TrainerEntity();
        trainer.setUser(user);
        when(entityManager.find(UserEntity.class, trainer.getUser().getId())).thenReturn(user);

        jpaTrainerDAO.changeActiveStatus(trainer);
    }

}