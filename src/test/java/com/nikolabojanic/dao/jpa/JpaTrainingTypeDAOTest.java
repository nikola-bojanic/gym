package com.nikolabojanic.dao.jpa;

import com.nikolabojanic.model.TrainingTypeEntity;
import jakarta.persistence.EntityManager;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaTrainingTypeDAOTest {
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private JpaTrainingTypeDAO jpaTrainingTypeDAO;

    @Test
    void getAllTest() {
        //given
        TypedQuery<TrainingTypeEntity> query = mock(TypedQuery.class);
        when(entityManager.createQuery("from TrainingTypeEntity", TrainingTypeEntity.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(new ArrayList<>());
        //when
        List<TrainingTypeEntity> retrieved = jpaTrainingTypeDAO.getAll();
        //then
        assertThat(retrieved).isEqualTo(new ArrayList<>());

    }

    @Test
    void findByIdTest() {
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(entityManager.find(TrainingTypeEntity.class, id)).thenReturn(new TrainingTypeEntity());

        Optional<TrainingTypeEntity> receivedType = jpaTrainingTypeDAO.findById(id);

        assertTrue(receivedType.isPresent());
    }

    @Test
    void findByNonExistingIdTest() {
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        when(entityManager.find(TrainingTypeEntity.class, id)).thenReturn(null);

        Optional<TrainingTypeEntity> receivedType = jpaTrainingTypeDAO.findById(id);

        assertTrue(receivedType.isEmpty());
    }
}