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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaTraineeDAOTest {
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private JpaTraineeDAO jpaTraineeDAO;

    @Test
    void deleteByUsernameTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(10);
        TraineeEntity trainee = new TraineeEntity();
        TypedQuery<TraineeEntity> query = mock(TypedQuery.class);
        when(entityManager.createQuery("select t from TraineeEntity t where t.user.username = :username",
                TraineeEntity.class))
                .thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(trainee);
        //when
        Optional<TraineeEntity> deleted = jpaTraineeDAO.deleteByUsername(username);
        //then
        assertThat(deleted).hasValue(trainee);
    }

    @Test
    void deleteByNonExistingUsernameTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(10);
        TypedQuery<TraineeEntity> query = mock(TypedQuery.class);
        when(entityManager.createQuery("select t from TraineeEntity t where t.user.username = :username",
                TraineeEntity.class))
                .thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getSingleResult()).thenThrow(NoResultException.class);
        //when
        Optional<TraineeEntity> trainee = jpaTraineeDAO.deleteByUsername(username);
        //then
        assertThat(trainee).isEmpty();
    }

    @Test
    void saveTest() {
        //given
        TraineeEntity trainee = new TraineeEntity();
        UserEntity user = new UserEntity();
        trainee.setUser(user);
        when(entityManager.merge(user)).thenReturn(user);
        when(entityManager.merge(trainee)).thenReturn(trainee);
        //when
        TraineeEntity receivedTrainee = jpaTraineeDAO.save(trainee);
        //then
        assertEquals(trainee, receivedTrainee);
    }

    @Test
    void findByUsernameTest() {
        //given
        TypedQuery<TraineeEntity> query = mock(TypedQuery.class);
        TraineeEntity trainee = new TraineeEntity();
        String username = RandomStringUtils.randomAlphabetic(10);
        when(entityManager.createQuery("select t from TraineeEntity t where t.user.username = :username",
                TraineeEntity.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(trainee);
        //when
        Optional<TraineeEntity> receivedTrainee = jpaTraineeDAO.findByUsername(username);
        //then
        assertThat(receivedTrainee).hasValue(trainee);
    }

    @Test
    void findByNonExistingUsernameTest() {
        //given
        TypedQuery<TraineeEntity> query = mock(TypedQuery.class);
        String username = RandomStringUtils.randomAlphabetic(10);
        when(entityManager.createQuery("select t from TraineeEntity t where t.user.username = :username",
                TraineeEntity.class)).thenReturn(query);
        when(query.setParameter("username", username)).thenReturn(query);
        when(query.getSingleResult()).thenThrow(NoResultException.class);
        //when
        Optional<TraineeEntity> receivedTrainee = jpaTraineeDAO.findByUsername(username);
        //then
        assertThat(receivedTrainee).isEmpty();
    }

    @Test
    void saveTrainersTest() {
        UserEntity user = new UserEntity();
        user.setUsername(RandomStringUtils.randomAlphabetic(10));
        List<TrainerEntity> trainers = List.of(new TrainerEntity(), new TrainerEntity());
        TraineeEntity trainee = new TraineeEntity();
        trainee.setUser(user);
        when(entityManager.merge(trainee)).thenReturn(trainee);

        List<TrainerEntity> savedTrainers = jpaTraineeDAO.saveTrainers(trainee, trainers);

        assertThat(savedTrainers).isEqualTo(trainers);
    }
}
