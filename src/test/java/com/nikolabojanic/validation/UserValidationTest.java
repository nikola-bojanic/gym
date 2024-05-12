package com.nikolabojanic.validation;

import com.nikolabojanic.model.UserEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserValidationTest {
    @InjectMocks
    private UserValidation userValidation;

    @Test
    void validateUsernameTest() {
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userValidation.validateUsername(null);
        });
        //then
        assertEquals("Username cannot be null", exception.getMessage());
    }

    @Test
    void validateAuthDtoTest() {
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userValidation.validateAuthDto(null);
        });
        //then
        assertEquals("Login request cannot be null", exception.getMessage());
    }

    @Test
    void validateUserFieldsNullUser() {
        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userValidation.validateUserFields(null);
        });
        //then
        assertEquals("Cannot create/update user profile with null value", exception.getMessage());
    }

    @Test
    void validateUserFieldsFirstNameNull() {
        //given
        UserEntity user = new UserEntity();

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userValidation.validateUserFields(user);
        });

        //then
        assertEquals("Cannot create/update user profile without a first name", exception.getMessage());

    }

    @Test
    void validateUserFieldsLastNameNull() {
        //given
        UserEntity user = new UserEntity();
        user.setFirstName(RandomStringUtils.randomAlphabetic(3, 8));

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userValidation.validateUserFields(user);
        });

        //then
        assertEquals("Cannot create/update user profile without a last name", exception.getMessage());


    }

    @Test
    void validateUserFieldsIsActiveNull() {
        //given
        UserEntity user = new UserEntity();
        user.setFirstName(RandomStringUtils.randomAlphabetic(3, 8));
        user.setLastName(RandomStringUtils.randomAlphabetic(3, 8));

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userValidation.validateUserFields(user);
        });

        //then
        assertEquals("Cannot create/update user profile without an active status", exception.getMessage());
    }

    @Test
    void validateUserPermissionToEditTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String otherUserUsername = RandomStringUtils.randomAlphabetic(8, 10);

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userValidation.validateUserPermissionToEdit(username, otherUserUsername);
        });

        //then
        assertEquals("No hacking allowed!", exception.getMessage());

    }

    @Test
    void validateUsernameAndPasswordUsernameNullTest() {
        //given
        String password = RandomStringUtils.randomAlphabetic(8, 10);

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userValidation.validateUsernameAndPassword(null, password);
        });

        //then
        assertEquals("Username must be at least 4 characters long", exception.getMessage());
    }

    @Test
    void validateUsernameAndPasswordPasswordNullTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(8, 10);

        //when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userValidation.validateUsernameAndPassword(username, null);
        });

        //then
        assertEquals("Password must be at least 8 characters long", exception.getMessage());
    }
}