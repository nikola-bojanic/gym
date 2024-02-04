package com.nikolabojanic.validation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import com.nikolabojanic.dto.AuthDtoRequest;
import com.nikolabojanic.dto.UserPasswordChangeRequestDto;
import com.nikolabojanic.exception.ScNotAuthorizedException;
import com.nikolabojanic.exception.ScValidationException;
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
    void validateNullUsernamePasswordRequestDtoTest() {
        //given
        String username = null;
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto();
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDto(username, requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Username path variable must be at least 4 characters long");
    }

    @Test
    void validateBlankUsernamePasswordRequestDtoTest() {
        //given
        String username = "";
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto();
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDto(username, requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Username path variable must be at least 4 characters long");
    }

    @Test
    void validateShortUsernamePasswordRequestDtoTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(3);
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto();
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDto(username, requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Username path variable must be at least 4 characters long");
    }

    @Test
    void validateNullRequestDtoPasswordRequestDtoTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDto requestDto = null;
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDto(username, requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Cannot change user password with null request");
    }

    @Test
    void validateNullUsernameRequestDtoPasswordRequestDtoTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto();
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDto(username, requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateBlankUsernameRequestDtoPasswordRequestDtoTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto();
        requestDto.setUsername("");
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDto(username, requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateShortUsernameRequestDtoPasswordRequestDtoTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto();
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(3));
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDto(username, requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateNullOldPasswordPasswordRequestDtoTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto();
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(5));
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDto(username, requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Old password must be at least 8 characters long");
    }

    @Test
    void validateBlankOldPasswordPasswordRequestDtoTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto();
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(5));
        requestDto.setOldPassword("");
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDto(username, requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Old password must be at least 8 characters long");
    }

    @Test
    void validateShortOldPasswordPasswordRequestDtoTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto();
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(5));
        requestDto.setOldPassword(RandomStringUtils.randomAlphabetic(3));
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDto(username, requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("Old password must be at least 8 characters long");
    }

    @Test
    void validateNullNewPasswordPasswordRequestDtoTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto();
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(5));
        requestDto.setOldPassword(RandomStringUtils.randomAlphabetic(8));
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDto(username, requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("New password must be at least 8 characters long");
    }

    @Test
    void validateBlankNewPasswordPasswordRequestDtoTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto();
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(5));
        requestDto.setOldPassword(RandomStringUtils.randomAlphabetic(8));
        requestDto.setNewPassword("");
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDto(username, requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("New password must be at least 8 characters long");
    }

    @Test
    void validateShortNewPasswordPasswordRequestDtoTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto();
        requestDto.setUsername(RandomStringUtils.randomAlphabetic(5));
        requestDto.setOldPassword(RandomStringUtils.randomAlphabetic(8));
        requestDto.setNewPassword(RandomStringUtils.randomAlphabetic(3));
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDto(username, requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("New password must be at least 8 characters long");
    }

    @Test
    void validateSameOldAndNewPasswordPasswordRequestDtoTest() {
        //given
        String password = RandomStringUtils.randomAlphabetic(8);
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDto requestDto = new UserPasswordChangeRequestDto();
        requestDto.setUsername(username);
        requestDto.setOldPassword(password);
        requestDto.setNewPassword(password);
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDto(username, requestDto))
            //then
            .isInstanceOf(ScValidationException.class)
            .hasMessage("New password cannot be the same as old password");
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
