package com.nikolabojanic.converter;

import static org.assertj.core.api.Assertions.assertThat;

import com.nikolabojanic.entity.UserEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserConverterTest {
    @InjectMocks
    private UserConverter userConverter;

    @Test
    void convertUsernameToUserTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(10);
        //when
        UserEntity user = userConverter.convertUsernameToUser(username);
        //then
        assertThat(user.getUsername()).isEqualTo(username);
    }

    @Test
    void convertRegistrationRequestTest() {
        //given
        String firstName = RandomStringUtils.randomAlphabetic(10);
        String lastName = RandomStringUtils.randomAlphabetic(10);
        //when
        UserEntity user = userConverter.convertRegistrationRequest(firstName, lastName);
        //then
        assertThat(user.getFirstName()).isEqualTo(firstName);
        assertThat(user.getLastName()).isEqualTo(lastName);
    }

    @Test
    void convertUpdateRequestToModelTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(10);
        String firstName = RandomStringUtils.randomAlphabetic(10);
        String lastName = RandomStringUtils.randomAlphabetic(10);
        Boolean isActive = true;
        //when
        UserEntity user = userConverter.convertUpdateRequestToModel(username, firstName, lastName, isActive);
        //then
        assertThat(user.getUsername()).isEqualTo(username);
        assertThat(user.getFirstName()).isEqualTo(firstName);
        assertThat(user.getLastName()).isEqualTo(lastName);
        assertThat(user.getIsActive()).isEqualTo(isActive);
    }
}