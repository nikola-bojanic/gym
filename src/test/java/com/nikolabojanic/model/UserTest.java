package com.nikolabojanic.model;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserTest {

    @Test
    void setAndGetUserTest() {
        UserEntity user = new UserEntity();
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        String firstName = RandomStringUtils.randomAlphabetic(5, 10);
        String lastName = RandomStringUtils.randomAlphabetic(5, 10);
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);

        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setUsername(username);
        user.setPassword(password);
        user.setIsActive(false);

        assertEquals(id, user.getId());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertFalse(user.getIsActive());
    }

    @Test
    void testAllArgsConstructor() {
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        String firstName = RandomStringUtils.randomAlphabetic(5, 10);
        String lastName = RandomStringUtils.randomAlphabetic(5, 10);
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);

        UserEntity user = new UserEntity(id, firstName, lastName, username, password, false);

        assertEquals(id, user.getId());
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertFalse(user.getIsActive());
    }

    @Test
    void testNoArgsConstructor() {
        UserEntity user = new UserEntity();

        assertNull(user.getId());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getIsActive());
    }

}