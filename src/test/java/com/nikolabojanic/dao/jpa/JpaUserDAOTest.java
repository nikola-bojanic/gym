package com.nikolabojanic.dao.jpa;

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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaUserDAOTest {
    @Mock
    private EntityManager entityManager;

    @InjectMocks
    private JpaUserDAO jpaUserDAO;

    @Test
    void findByUsernameTest() {
        TypedQuery<UserEntity> typedQuery = mock(TypedQuery.class);
        UserEntity user = new UserEntity();
        String username = RandomStringUtils.randomAlphabetic(3, 6);
        when(entityManager.createQuery("from UserEntity where username = :username",
                UserEntity.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("username", username)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenReturn(user);

        Optional<UserEntity> receivedUser = jpaUserDAO.findByUsername(username);

        assertTrue(receivedUser.isPresent());
    }

    @Test
    void findByNonExistingUsernameTest() {
        TypedQuery<UserEntity> typedQuery = mock(TypedQuery.class);
        UserEntity user = new UserEntity();
        String username = RandomStringUtils.randomAlphabetic(3, 6);
        when(entityManager.createQuery("from UserEntity where username = :username",
                UserEntity.class)).thenReturn(typedQuery);
        when(typedQuery.setParameter("username", username)).thenReturn(typedQuery);
        when(typedQuery.getSingleResult()).thenThrow(NoResultException.class);

        Optional<UserEntity> receivedUser = jpaUserDAO.findByUsername(username);

        assertTrue(receivedUser.isEmpty());
    }

    @Test
    void saveTest() {
        //given
        UserEntity user = new UserEntity();
        when(entityManager.merge(user)).thenReturn(user);
        //when
        UserEntity savedUser = jpaUserDAO.save(user);
        assertThat(savedUser).isEqualTo(user);
    }

    @Test
    void countUsersWithSameNameTest() {
        TypedQuery<Long> query = mock(TypedQuery.class);
        String firstName = RandomStringUtils.randomAlphabetic(4, 7);
        String lastName = RandomStringUtils.randomAlphabetic(4, 7);
        Long count = Long.parseLong(RandomStringUtils.randomNumeric(4, 7));
        when(entityManager.createQuery("select coalesce(count(u), 0) from UserEntity u where u.firstName " +
                "= :firstName and u.lastName = :lastName", Long.class)).thenReturn(query);
        when(query.setParameter("firstName", firstName)).thenReturn(query);
        when(query.setParameter("lastName", lastName)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(count);

        Long receivedCount = jpaUserDAO.countUsersWithSameName(firstName, lastName);

        assertEquals(count, receivedCount);
    }
}