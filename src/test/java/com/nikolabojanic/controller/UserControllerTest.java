package com.nikolabojanic.controller;

import com.nikolabojanic.dto.AuthDTO;
import com.nikolabojanic.dto.UserPasswordChangeRequestDTO;
import com.nikolabojanic.model.UserEntity;
import com.nikolabojanic.service.UserService;
import com.nikolabojanic.validation.UserValidation;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Mock
    private UserService userService;
    @Mock
    private UserValidation userValidation;
    @InjectMocks
    private UserController userController;

    @Test
    void loginTest() {
        //given
        doNothing().when(userService).authentication(any(AuthDTO.class));
        //when
        ResponseEntity<Void> response = userController.login(
                new AuthDTO(RandomStringUtils.randomAlphabetic(10),
                        RandomStringUtils.randomAlphabetic(10)));
        //then
        assertThat(response.getStatusCode()).isNotNull();
    }

    @Test
    void changePasswordTest() {
        //given
        doNothing().when(userValidation).validatePasswordRequestDTO(any(String.class),
                any(UserPasswordChangeRequestDTO.class));
        when(userService.changeUserPassword(any(AuthDTO.class), any(String.class),
                any(UserPasswordChangeRequestDTO.class))).thenReturn(new UserEntity());
        //when
        ResponseEntity<Void> response = userController.changePassword(
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                RandomStringUtils.randomAlphabetic(10),
                new UserPasswordChangeRequestDTO());
        //then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    }
}