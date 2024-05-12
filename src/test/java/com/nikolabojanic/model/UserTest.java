package com.nikolabojanic.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserTest {

    @Test
    void setAndGetUserTest() {
        //Arrange
        User user = new User();
        //Act
        user.setId(1L);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setUsername("firstNameLastName");
        user.setPassword("1234567890");
        user.setIsActive(false);
        //Assert
        assertEquals(1L, user.getId());
        assertEquals("firstName", user.getFirstName());
        assertEquals("lastName", user.getLastName());
        assertEquals("firstNameLastName", user.getUsername());
        assertEquals("1234567890", user.getPassword());
        assertFalse(user.getIsActive());
    }

    @Test
    void testAllArgsConstructor() {
        //Arrange and act
        User user = new User(1L, "firstName", "lastName", "firstNameLastName", "1234567890", false);
        //Assert
        assertEquals(1L, user.getId());
        assertEquals("firstName", user.getFirstName());
        assertEquals("lastName", user.getLastName());
        assertEquals("firstNameLastName", user.getUsername());
        assertEquals("1234567890", user.getPassword());
        assertFalse(user.getIsActive());
    }

    @Test
    void testNoArgsConstructor() {
        //Arrange and act
        User user = new User();
        //Assert
        assertNull(user.getId());
        assertNull(user.getFirstName());
        assertNull(user.getLastName());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getIsActive());
    }

}