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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaTrainerDAOTest {
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private JpaTrainerDAO jpaTrainerDAO;

    @Test
    void findByUsernameTest() {
        //given
        TypedQuery<TrainerEntity> query = mock(TypedQuery.class);
        TrainerEntity trainer = new TrainerEntity();
        String username = RandomStringUtils.randomAlphabetic(10);
        when(entityManager.createQuery("select t from TrainerEntity t where t.user.username = :username",
                TrainerEntity.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(trainer);
        //when
        Optional<TrainerEntity> receivedTrainer = jpaTrainerDAO.findByUsername(username);
        //then
        assertThat(receivedTrainer).hasValue(trainer);
    }

    @Test
    void findByNonExistingUsernameTest() {
        //given
        TypedQuery<TrainerEntity> query = mock(TypedQuery.class);
        String username = RandomStringUtils.randomAlphabetic(10);
        when(entityManager.createQuery("select t from TrainerEntity t where t.user.username = :username",
                TrainerEntity.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getSingleResult()).thenThrow(NoResultException.class);
        //when
        Optional<TrainerEntity> receivedTrainer = jpaTrainerDAO.findByUsername(username);
        //then
        assertThat(receivedTrainer).isEmpty();
    }

    @Test
    void saveTest() {
        //given
        TrainerEntity trainer = new TrainerEntity();
        UserEntity user = new UserEntity();
        trainer.setUser(user);
        when(entityManager.merge(user)).thenReturn(user);
        when(entityManager.merge(trainer)).thenReturn(trainer);
        //when
        TrainerEntity receivedTrainer = jpaTrainerDAO.save(trainer);
        //then
        assertEquals(trainer, receivedTrainer);
    }

    @Test
    void findActiveForOtherTraineesTest() {
        //given
        TypedQuery<TrainerEntity> query = mock(TypedQuery.class);
        Long traineeId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(entityManager.createQuery("from TrainerEntity t where " +
                        "t.user.isActive = true and " +
                        "(t.id not in(select tt.id from TrainerEntity tt left join tt.trainees trainee " +
                        "WHERE trainee.id = :traineeId))",
                TrainerEntity.class)).thenReturn(query);
        when(query.setParameter("traineeId", traineeId)).thenReturn(query);
        List<TrainerEntity> trainers = List.of(new TrainerEntity(), new TrainerEntity());
        when(query.getResultList()).thenReturn(trainers);
        //when
        List<TrainerEntity> receivedTrainers = jpaTrainerDAO.findActiveForOtherTrainees(traineeId);
        //then
        assertEquals(trainers, receivedTrainers);
    }

    @Test
    void findNoneActiveForOtherTraineesTest() {
        //given
        TypedQuery<TrainerEntity> query = mock(TypedQuery.class);
        Long traineeId = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(entityManager.createQuery("from TrainerEntity t where " +
                        "t.user.isActive = true and " +
                        "(t.id not in(select tt.id from TrainerEntity tt left join tt.trainees trainee" +
                        " WHERE trainee.id = :traineeId))",
                TrainerEntity.class)).thenReturn(query);
        when(query.setParameter("traineeId", traineeId)).thenReturn(query);
        List<TrainerEntity> trainers = List.of(new TrainerEntity(), new TrainerEntity());
        when(query.getResultList()).thenThrow(NoResultException.class);
        //when
        List<TrainerEntity> receivedTrainers = jpaTrainerDAO.findActiveForOtherTrainees(traineeId);
        //then
        assertEquals(new ArrayList<>(), receivedTrainers);
    }
}