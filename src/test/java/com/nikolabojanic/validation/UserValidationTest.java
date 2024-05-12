package com.nikolabojanic.validation;

import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.dto.UserPasswordChangeRequestDTO;
import com.nikolabojanic.exception.SCNotAuthorizedException;
import com.nikolabojanic.exception.SCValidationException;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@ExtendWith(MockitoExtension.class)
class UserValidationTest {

    @InjectMocks
    private UserValidation userValidation;

    @Test
    void validateNullUsernamePasswordRequestDTOTest() {
        //given
        String username = null;
        UserPasswordChangeRequestDTO requestDTO = new UserPasswordChangeRequestDTO();
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDTO(username, requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Username path variable must be at least 4 characters long");
    }

    @Test
    void validateBlankUsernamePasswordRequestDTOTest() {
        //given
        String username = "";
        UserPasswordChangeRequestDTO requestDTO = new UserPasswordChangeRequestDTO();
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDTO(username, requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Username path variable must be at least 4 characters long");
    }

    @Test
    void validateShortUsernamePasswordRequestDTOTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(3);
        UserPasswordChangeRequestDTO requestDTO = new UserPasswordChangeRequestDTO();
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDTO(username, requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Username path variable must be at least 4 characters long");
    }

    @Test
    void validateNullRequestDTOPasswordRequestDTOTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDTO requestDTO = null;
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDTO(username, requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Cannot change user password with null request");
    }

    @Test
    void validateNullUsernameRequestDTOPasswordRequestDTOTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDTO requestDTO = new UserPasswordChangeRequestDTO();
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDTO(username, requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateBlankUsernameRequestDTOPasswordRequestDTOTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDTO requestDTO = new UserPasswordChangeRequestDTO();
        requestDTO.setUsername("");
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDTO(username, requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateShortUsernameRequestDTOPasswordRequestDTOTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDTO requestDTO = new UserPasswordChangeRequestDTO();
        requestDTO.setUsername(RandomStringUtils.randomAlphabetic(3));
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDTO(username, requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Username must be at least 4 characters long");
    }

    @Test
    void validateNullOldPasswordPasswordRequestDTOTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDTO requestDTO = new UserPasswordChangeRequestDTO();
        requestDTO.setUsername(RandomStringUtils.randomAlphabetic(5));
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDTO(username, requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Old password must be at least 8 characters long");
    }

    @Test
    void validateBlankOldPasswordPasswordRequestDTOTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDTO requestDTO = new UserPasswordChangeRequestDTO();
        requestDTO.setUsername(RandomStringUtils.randomAlphabetic(5));
        requestDTO.setOldPassword("");
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDTO(username, requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Old password must be at least 8 characters long");
    }

    @Test
    void validateShortOldPasswordPasswordRequestDTOTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDTO requestDTO = new UserPasswordChangeRequestDTO();
        requestDTO.setUsername(RandomStringUtils.randomAlphabetic(5));
        requestDTO.setOldPassword(RandomStringUtils.randomAlphabetic(3));
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDTO(username, requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Old password must be at least 8 characters long");
    }

    @Test
    void validateNullNewPasswordPasswordRequestDTOTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDTO requestDTO = new UserPasswordChangeRequestDTO();
        requestDTO.setUsername(RandomStringUtils.randomAlphabetic(5));
        requestDTO.setOldPassword(RandomStringUtils.randomAlphabetic(8));
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDTO(username, requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("New password must be at least 8 characters long");
    }

    @Test
    void validateBlankNewPasswordPasswordRequestDTOTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDTO requestDTO = new UserPasswordChangeRequestDTO();
        requestDTO.setUsername(RandomStringUtils.randomAlphabetic(5));
        requestDTO.setOldPassword(RandomStringUtils.randomAlphabetic(8));
        requestDTO.setNewPassword("");
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDTO(username, requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("New password must be at least 8 characters long");
    }

    @Test
    void validateShortNewPasswordPasswordRequestDTOTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDTO requestDTO = new UserPasswordChangeRequestDTO();
        requestDTO.setUsername(RandomStringUtils.randomAlphabetic(5));
        requestDTO.setOldPassword(RandomStringUtils.randomAlphabetic(8));
        requestDTO.setNewPassword(RandomStringUtils.randomAlphabetic(3));
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDTO(username, requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("New password must be at least 8 characters long");
    }

    @Test
    void validateSameOldAndNewPasswordPasswordRequestDTOTest() {
        //given
        String password = RandomStringUtils.randomAlphabetic(8);
        String username = RandomStringUtils.randomAlphabetic(5);
        UserPasswordChangeRequestDTO requestDTO = new UserPasswordChangeRequestDTO();
        requestDTO.setUsername(username);
        requestDTO.setOldPassword(password);
        requestDTO.setNewPassword(password);
        //when
        assertThatThrownBy(() -> userValidation.validatePasswordRequestDTO(username, requestDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("New password cannot be the same as old password");
    }

    @Test
    void validateNullAuthDTOTest() {
        //given
        AuthDTO authDTO = null;
        //when
        assertThatThrownBy(() -> userValidation.validateAuthDto(authDTO))
                //then
                .isInstanceOf(SCValidationException.class)
                .hasMessage("Login request cannot be null");
    }

    @Test
    void validateAuthDTOTest() {
        //given
        AuthDTO authDTO = new AuthDTO();
        //then
        assertDoesNotThrow(() -> userValidation.validateAuthDto(authDTO));
    }

    @Test
    void validateUserPermissionToEditTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(5);
        String domainUsername = RandomStringUtils.randomAlphabetic(5);
        //then
        assertDoesNotThrow(() -> userValidation.validateUserPermissionToEdit(username, username));
        assertThatThrownBy(() -> userValidation.validateUserPermissionToEdit(username, domainUsername))
                .isInstanceOf(SCNotAuthorizedException.class)
                .hasMessage("Cannot change other user's password");
    }

}
