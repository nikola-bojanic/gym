package com.nikolabojanic.validation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.nikolabojanic.dto.AuthDtoRequest;
import com.nikolabojanic.dto.UserPasswordChangeRequestDto;
import com.nikolabojanic.exception.ScNotAuthorizedException;
import com.nikolabojanic.exception.ScValidationException;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserValidationTest {

    @InjectMocks
    private UserValidation userValidation;

    @Test
    void validatePasswordRequestDtoTest() {
        //arrange
        String username = RandomStringUtils.randomAlphabetic(6);
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto(
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(8),
            RandomStringUtils.randomAlphabetic(8)
        );
        //act
        //assert
        assertDoesNotThrow(() -> userValidation.validatePasswordRequestDto(username, requestDto));
    }

    @Test
    void validateNullRequestDtoTest() {
        //act
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDto(null, null))
            //assert
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Cannot change user password with null request");

    }

    @Test
    void validateNullUsernameNullOldPasswordNullNewPasswordRequestDtoTest() {
        //arrange
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto();
        List<String> message = List.of(
            "Username path variable must be at least 4 characters long",
            "Username must be at least 4 characters long",
            "Old password must be at least 8 characters long",
            "New password must be at least 8 characters long"
        );
        //act
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDto(null, requestDto))
            //assert
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
    }

    @Test
    void validateBlankUsernameBlankOldPasswordBlankNewPasswordRequestDtoTest() {
        //arrange
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto(
            "     ",
            " ",
            " "
        );
        String username = "     ";
        List<String> message = List.of(
            "Username path variable must be at least 4 characters long",
            "Username must be at least 4 characters long",
            "Old password must be at least 8 characters long",
            "New password must be at least 8 characters long",
            "New password cannot be the same as old password"
        );
        //act
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDto(username, requestDto))
            //assert
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
    }

    @Test
    void validateShortUsernameShortOldPasswordShortNewPasswordRequestDtoTest() {
        //arrange
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto(
            RandomStringUtils.randomAlphabetic(3),
            RandomStringUtils.randomAlphabetic(7),
            RandomStringUtils.randomAlphabetic(7)
        );
        String username = " ";
        List<String> message = List.of(
            "Username path variable must be at least 4 characters long",
            "Username must be at least 4 characters long",
            "Old password must be at least 8 characters long",
            "New password must be at least 8 characters long"
        );
        //act
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDto(username, requestDto))
            //assert
            .isInstanceOf(ScValidationException.class)
            .hasMessage(message.toString());
    }

    @Test
    void validateNullAuthDtoTest() {
        //given
        AuthDtoRequest authDtoRequest = null;
        //when
        assertThatThrownBy(() -> userValidation.validateAuthDto(authDtoRequest))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Login request cannot be null");
    }

    @Test
    void validateAuthDtoTest() {
        //given
        AuthDtoRequest authDtoRequest = new AuthDtoRequest();
        //then
        assertDoesNotThrow(() -> userValidation.validateAuthDto(authDtoRequest));
    }

    @Test
    void validateUserPermissionToEditTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String domainUsername = RandomStringUtils.randomAlphabetic(5);
        //then
        assertDoesNotThrow(() -> userValidation.validateUserPermissionToEdit(username, username));
        assertThatThrownBy(() -> userValidation.validateUserPermissionToEdit(username, domainUsername))
            .isInstanceOf(ScNotAuthorizedException.class)
            .hasMessage("Cannot change other user's password");
    }

}