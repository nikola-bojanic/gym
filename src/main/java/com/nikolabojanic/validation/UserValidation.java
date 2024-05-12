package com.nikolabojanic.validation;

import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.dto.UserPasswordChangeRequestDTO;
import com.nikolabojanic.exception.SCNotAuthorizedException;
import com.nikolabojanic.exception.SCValidationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserValidation {
    Integer badRequest = HttpStatus.BAD_REQUEST.value();

    public void validatePasswordRequestDTO(String username, UserPasswordChangeRequestDTO requestDTO) {
        if (username == null || username.length() < 4 || username.isBlank()) {
            log.error("Attempted to find user with bad username. Status: {}", badRequest);
            throw new SCValidationException("Username path variable must be at least 4 characters long");
        } else if (requestDTO == null) {
            log.error("Attempted to change user password with null request. Status: {}", badRequest);
            throw new SCValidationException("Cannot change user password with null request");
        } else if (requestDTO.getUsername() == null || requestDTO.getUsername().length() < 4
                || requestDTO.getUsername().isBlank()) {
            log.error("Attempted to change user password with bad username. Status: {}", badRequest);
            throw new SCValidationException("Username must be at least 4 characters long");
        } else if (requestDTO.getOldPassword() == null || requestDTO.getOldPassword().isBlank()
                || requestDTO.getOldPassword().length() < 8) {
            log.error("Attempted to change user password with bad old password. Status: {}", badRequest);
            throw new SCValidationException("Old password must be at least 8 characters long");
        } else if (requestDTO.getNewPassword() == null || requestDTO.getNewPassword().isBlank()
                || requestDTO.getNewPassword().length() < 8) {
            log.error("Attempted to change user password with bad new password. Status: {}", badRequest);
            throw new SCValidationException("New password must be at least 8 characters long");
        } else if (requestDTO.getNewPassword().equals(requestDTO.getOldPassword())) {
            log.error("Attempted to change user password with same password. Status: {}", badRequest);
            throw new SCValidationException("New password cannot be the same as old password");
        }
    }

    public void validateAuthDto(AuthDTO authDTO) {
        if (authDTO == null) {
            log.error("Attempted to log in with null. Status: {}", badRequest);
            throw new SCValidationException("Login request cannot be null");
        }
    }

    public void validateUserPermissionToEdit(String username, String domainUsername) {
        if (!username.equals(domainUsername)) {
            log.error("Attempted to change other user's password. Status: {}", HttpStatus.FORBIDDEN.value());
            throw new SCNotAuthorizedException("Cannot change other user's password");
        }
    }
}
