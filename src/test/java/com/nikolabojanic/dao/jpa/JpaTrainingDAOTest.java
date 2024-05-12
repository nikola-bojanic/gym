package com.nikolabojanic.dao.jpa;

import com.nikolabojanic.model.TrainingEntity;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaTrainingDAOTest {
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private JpaTrainingDAO jpaTrainingDAO;

    @Test
    void saveTest() {
        //given
        TrainingEntity training = new TrainingEntity();
        when(entityManager.merge(training)).thenReturn(training);
        //when
        TrainingEntity saved = jpaTrainingDAO.save(training);
        //then
        assertNotNull(saved);
    }
}