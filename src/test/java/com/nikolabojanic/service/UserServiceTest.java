package com.nikolabojanic.service;

import com.nikolabojanic.dao.UserDAO;
import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.model.UserEntity;
import com.nikolabojanic.validation.UserValidation;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserDAO userDAO;
    @Mock
    private UserValidation userValidation;
    @InjectMocks
    private UserService userService;

    @Test
    void findByUsernameTest() {
        Long id = Long.parseLong(RandomStringUtils.randomNumeric(3, 6));
        String firstName = RandomStringUtils.randomAlphabetic(5, 10);
        String lastName = RandomStringUtils.randomAlphabetic(5, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        UserEntity user = new UserEntity(id, firstName, lastName, username, password, false);
        when(userDAO.findByUsername(username)).thenReturn(Optional.of(user));

        UserEntity mockedUser = userService.findByUsername(username);

        assertEquals(id, mockedUser.getId());
        assertEquals(firstName, mockedUser.getFirstName());
        assertEquals(lastName, mockedUser.getLastName());
        assertEquals(username, mockedUser.getUsername());
        assertEquals(password, mockedUser.getPassword());
        assertFalse(mockedUser.getIsActive());
    }


    @Test
    void findByNonExistingUsernameTest() {
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        when(userDAO.findByUsername(username)).thenReturn(Optional.empty());

        NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
            userService.findByUsername(username);
        });

        assertEquals("User with that username cannot be found", exception.getMessage());
    }

    @Test
    void authenticationTest() {
        String password = RandomStringUtils.randomAlphabetic(8, 10);
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String differentPassword = RandomStringUtils.randomAlphabetic(8, 10);
        UserEntity user = new UserEntity();
        user.setPassword(differentPassword);
        when(userDAO.findByUsername(username)).thenReturn(Optional.of(user));
        AuthDTO authDTO = new AuthDTO(username, password);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.authentication(authDTO);
        });

        assertEquals("Passwords do not match", exception.getMessage());
    }

//    @Test
//    void authenticationWithNullTest() {
//        AuthDTO authDTO = null;
//
//        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
//            userService.authentication(authDTO);
//        });
//
//        assertEquals("Login request cannot be null", exception.getMessage());
//    }

    @Test
    void generateUsernameAndPasswordTest() {
        String firstName1 = RandomStringUtils.randomAlphabetic(8, 10);
        String lastName1 = RandomStringUtils.randomAlphabetic(3, 6);
        UserEntity user1 = new UserEntity();
        user1.setFirstName(firstName1);
        user1.setLastName(lastName1);
        user1.setIsActive(true);
        String username1 = firstName1 + "." + lastName1;

        UserEntity user = userService.generateUsernameAndPassword(user1);

        assertEquals(username1, user.getUsername());
        assertEquals(10, user.getPassword().length());
    }

}
