package com.nikolabojanic.validation;

import com.nikolabojanic.dto.AuthDtoRequest;
import com.nikolabojanic.dto.UserPasswordChangeRequestDto;
import com.nikolabojanic.exception.ScNotAuthorizedException;
import com.nikolabojanic.exception.ScValidationException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserValidation {
    private static final int BAD_REQUEST = HttpStatus.BAD_REQUEST.value();

    /**
     * Validates the request for changing user password, ensuring that the provided data is valid.
     *
     * @param username   The username from the path variable.
     * @param requestDto The UserPasswordChangeRequestDto containing old and new passwords.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validatePasswordRequestDto(String username, UserPasswordChangeRequestDto requestDto) {
        List<String> errors = new ArrayList<>();
        if (username == null || username.length() < 4 || username.isBlank()) {
            log.error("Attempted to find user with bad username. Status: {}", BAD_REQUEST);
            errors.add("Username path variable must be at least 4 characters long");
        }
        if (requestDto == null) {
            log.error("Attempted to change user password with null request. Status: {}", BAD_REQUEST);
            throw new ScValidationException("Cannot change user password with null request");
        }
        if (requestDto.getUsername() == null || requestDto.getUsername().length() < 4
            || requestDto.getUsername().isBlank()) {
            log.error("Attempted to change user password with bad username. Status: {}", BAD_REQUEST);
            errors.add("Username must be at least 4 characters long");
        }
        if (requestDto.getOldPassword() == null || requestDto.getOldPassword().isBlank()
            || requestDto.getOldPassword().length() < 8) {
            log.error("Attempted to change user password with bad old password. Status: {}", BAD_REQUEST);
            errors.add("Old password must be at least 8 characters long");
        }
        if (requestDto.getNewPassword() == null || requestDto.getNewPassword().isBlank()
            || requestDto.getNewPassword().length() < 8) {
            log.error("Attempted to change user password with bad new password. Status: {}", BAD_REQUEST);
            errors.add("New password must be at least 8 characters long");
        }
        if (requestDto.getNewPassword() != null && (requestDto.getNewPassword().equals(requestDto.getOldPassword()))) {
            log.error("Attempted to change user password with same password. Status: {}", BAD_REQUEST);
            errors.add("New password cannot be the same as old password");
        }
        if (!errors.isEmpty()) {
            throw new ScValidationException(errors.toString());
        }
    }

    /**
     * Validates the authentication request, ensuring that it is not null.
     *
     * @param authDtoRequest The authentication request DTO.
     * @throws ScValidationException If validation fails, an exception is thrown with a corresponding message.
     */
    public void validateAuthDto(AuthDtoRequest authDtoRequest) {
        if (authDtoRequest == null) {
            log.error("Attempted to log in with null. Status: {}", BAD_REQUEST);
            throw new ScValidationException("Login request cannot be null");
        }
    }

    /**
     * Validates whether the user has permission to edit based on the provided usernames.
     *
     * @param username       The username from the path variable.
     * @param domainUsername The username from the domain model.
     * @throws ScNotAuthorizedException If the user does not have permission to edit, an exception is thrown.
     */
    public void validateUserPermissionToEdit(String username, String domainUsername) {
        if (!username.equals(domainUsername)) {
            log.error("Attempted to change other user's password. Status: {}", HttpStatus.FORBIDDEN.value());
            throw new ScNotAuthorizedException("Cannot change other user's password");
        }
    }
}
