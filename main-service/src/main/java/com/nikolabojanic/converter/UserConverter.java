package com.nikolabojanic.converter;

import com.nikolabojanic.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@AllArgsConstructor
@Component
public class UserConverter {
    /**
     * Convert a username to a UserEntity model.
     *
     * @param username The username to be converted.
     * @return UserEntity representing the converted model.
     */
    public UserEntity convertUsernameToUser(String username) {
        UserEntity user = new UserEntity();
        user.setUsername(username);
        return user;
    }

    /**
     * Convert registration request details to a UserEntity model.
     *
     * @param firstName The first name from the registration request.
     * @param lastName  The last name from the registration request.
     * @return UserEntity representing the converted model.
     */
    public UserEntity convertRegistrationRequest(String firstName, String lastName) {
        UserEntity user = new UserEntity();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setIsActive(true);
        log.info("Successfully converted user registration request to user model.");
        return user;
    }

    /**
     * Convert an update request to a UserEntity model.
     *
     * @param username  The username from the update request.
     * @param firstName The updated first name.
     * @param lastName  The updated last name.
     * @param isActive  The updated status (active or not).
     * @return UserEntity representing the converted model.
     */
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
