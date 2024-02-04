package com.nikolabojanic.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.nikolabojanic.config.security.UserPrincipal;
import com.nikolabojanic.dto.UserPasswordChangeRequestDto;
import com.nikolabojanic.entity.UserEntity;
import com.nikolabojanic.service.UserService;
import com.nikolabojanic.validation.UserValidation;
import io.micrometer.core.instrument.Counter;
import java.util.ArrayList;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private UserValidation userValidation;
    @Mock
    private Counter userEndpointsHitCounter;
    @InjectMocks
    private UserController userController;

    @Test
    void changePasswordTest() {
        //given
        UserPrincipal principal = new UserPrincipal(Long.parseLong(
            RandomStringUtils.randomNumeric(5)),
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            new ArrayList<>());
        UserPasswordChangeRequestDto userPasswordChangeRequestDto = new UserPasswordChangeRequestDto(
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5),
            RandomStringUtils.randomAlphabetic(5)
        );
        doNothing().when(userValidation).validatePasswordRequestDto(any(String.class),
            any(UserPasswordChangeRequestDto.class));
        doNothing().when(userValidation).validateUserPermissionToEdit(any(String.class), any(String.class));
        when(userService.changeUserPassword(any(String.class),
            any(UserPasswordChangeRequestDto.class))).thenReturn(new UserEntity());
        //when
        ResponseEntity<Void> response = userController.changePassword(
            principal,
            userPasswordChangeRequestDto);
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}