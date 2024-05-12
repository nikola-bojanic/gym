package com.nikolabojanic.dao.jpa;

import com.nikolabojanic.model.TrainingEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaTrainingDAOTest {
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private JpaTrainingDAO jpaTrainingDAO;

    @Test
    void saveTest() {
        TrainingEntity training = new TrainingEntity();
        when(entityManager.merge(training)).thenReturn(training);

        TrainingEntity saved = jpaTrainingDAO.save(training);

        assertNotNull(saved);
    }


    @Test
    void findByTraineeIdAndDateTest() {
        TypedQuery<TrainingEntity> query = mock(TypedQuery.class);
        List<TrainingEntity> trainings = List.of(new TrainingEntity(), new TrainingEntity());
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        LocalDate begin = LocalDate.now();
        LocalDate end = LocalDate.now();
        when(entityManager.createQuery("from TrainingEntity where trainee.id = :traineeId and date between :beginDate and :endDate", TrainingEntity.class)).thenReturn(query);
        when(query.setParameter("traineeId", id)).thenReturn(query);
        when(query.setParameter("beginDate", begin)).thenReturn(query);
        when(query.setParameter("endDate", end)).thenReturn(query);
        when(query.getResultList()).thenReturn(trainings);

        List<TrainingEntity> receivedTrainings = jpaTrainingDAO.findByTraineeIdAndDate(id, begin, end);

        assertEquals(trainings, receivedTrainings);
    }

    @Test
    void findByTrainerIdAndDateTest() {
        TypedQuery<TrainingEntity> query = mock(TypedQuery.class);
        List<TrainingEntity> trainings = List.of(new TrainingEntity(), new TrainingEntity());
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        LocalDate begin = LocalDate.now();
        LocalDate end = LocalDate.now();
        when(entityManager.createQuery("from TrainingEntity where trainer.id = :trainerId and date between :beginDate and :endDate", TrainingEntity.class)).thenReturn(query);
        when(query.setParameter("trainerId", id)).thenReturn(query);
        when(query.setParameter("beginDate", begin)).thenReturn(query);
        when(query.setParameter("endDate", end)).thenReturn(query);
        when(query.getResultList()).thenReturn(trainings);

        List<TrainingEntity> receivedTrainings = jpaTrainingDAO.findByTrainerIdAndDate(id, begin, end);

        assertEquals(trainings, receivedTrainings);
    }

    @Test
    void findEmptyListByTraineeIdAndDateTest() {
        TypedQuery<TrainingEntity> query = mock(TypedQuery.class);
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        LocalDate begin = LocalDate.now();
        LocalDate end = LocalDate.now();
        when(entityManager.createQuery("from TrainingEntity where trainee.id = :traineeId and date between :beginDate and :endDate", TrainingEntity.class)).thenReturn(query);
        when(query.setParameter("traineeId", id)).thenReturn(query);
        when(query.setParameter("beginDate", begin)).thenReturn(query);
        when(query.setParameter("endDate", end)).thenReturn(query);
        when(query.getResultList()).thenThrow(NoResultException.class);

        List<TrainingEntity> receivedTrainings = jpaTrainingDAO.findByTraineeIdAndDate(id, begin, end);

        assertEquals(receivedTrainings, new ArrayList<>());
    }

    @Test
    void findEmptyListByTrainerIdAndDateTest() {
        TypedQuery<TrainingEntity> query = mock(TypedQuery.class);
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        LocalDate begin = LocalDate.now();
        LocalDate end = LocalDate.now();
        when(entityManager.createQuery("from TrainingEntity where trainer.id = :trainerId and date between :beginDate and :endDate", TrainingEntity.class)).thenReturn(query);
        when(query.setParameter("trainerId", id)).thenReturn(query);
        when(query.setParameter("beginDate", begin)).thenReturn(query);
        when(query.setParameter("endDate", end)).thenReturn(query);
        when(query.getResultList()).thenThrow(NoResultException.class);

        List<TrainingEntity> receivedTrainings = jpaTrainingDAO.findByTrainerIdAndDate(id, begin, end);

        assertEquals(receivedTrainings, new ArrayList<>());
    }

}