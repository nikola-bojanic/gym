package com.nikolabojanic.converter;

import com.nikolabojanic.model.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class UserConverter {
    public UserEntity convertUsernameToUser(String username) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        return user;
    }

    public UserEntity convertRegistrationRequest(String firstName, String lastName) {
        UserEntity user = new UserEntity();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setIsActive(true);
        log.info("Successfully converted user registration request to user model.");
        return user;
    }

    public UserEntity convertUpdateRequestToModel(
            String username,
            String firstName,
            String lastName,
            Boolean isActive) {
        UserEntity user = new UserEntity();
        user.setIsActive(isActive);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        log.info("Successfully converted user update request to user model.");
        return user;
    }

}
