package com.nikolabojanic.dao.jpa;

import com.nikolabojanic.dao.UserDAO;
import com.nikolabojanic.model.UserEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
public class JpaUserDAO implements UserDAO {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try {
            UserEntity attachedUser = entityManager.createQuery("from UserEntity where username = " +
                            ":username", UserEntity.class).
                    setParameter("username", username).
                    getSingleResult();
            log.info("Retrieved user with username {} from the database", username);
            return Optional.of(attachedUser);
        } catch (NoResultException e) {
            log.info("User with username {} doesn't exist in the database", username);
            return Optional.empty();
        }
    }

    @Override
    public Long countUsersWithSameName(String firstName, String lastName) {
        return entityManager.createQuery("select coalesce(count(u), 0) from UserEntity u where u.firstName = " +
                        ":firstName and u.lastName = :lastName", Long.class)
                .setParameter("firstName", firstName).
                setParameter("lastName", lastName)
                .getSingleResult();
    }

    @Override
    public UserEntity save(UserEntity user) {
        UserEntity saved = entityManager.merge(user);
        log.info("Wrote changes to user with username {} to the database", saved.getUsername());
        return saved;
    }
}
