package com.nikolabojanic.dao.jpa;

import com.nikolabojanic.dao.UserDAO;
import com.nikolabojanic.model.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class JpaUserDAO implements UserDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<UserEntity> getAll() {
        return entityManager.createQuery("from UserEntity", UserEntity.class).getResultList();
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try {
            UserEntity attachedUser = entityManager.createQuery("from UserEntity where username = :username", UserEntity.class).
                    setParameter("username", username).
                    getSingleResult();
            return Optional.ofNullable(attachedUser);
        } catch (NoResultException e) {
            return Optional.empty();
        }

    }

    @Override
    public Long countUsersWithSameName(String firstName, String lastName) {
        Long count = entityManager.createQuery("select count(u) from UserEntity u where u.firstName = :firstName and u.lastName = :lastName", Long.class)
                .setParameter("firstName", firstName).
                setParameter("lastName", lastName)
                .getSingleResult();
        return count == null ? 0 : count;
    }
}
