package com.nikolabojanic.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class AuthDtoRequestTest {

    @Test
    void getterAndSetterTest() {
        AuthDtoRequest authDtoRequest = new AuthDtoRequest();
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);

        authDtoRequest.setPassword(password);
        authDtoRequest.setUsername(username);

        assertEquals(username, authDtoRequest.getUsername());
        assertEquals(password, authDtoRequest.getPassword());
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);
        //when
        AuthDtoRequest authDtoRequest = new AuthDtoRequest(username, password);
        //then
        assertEquals(username, authDtoRequest.getUsername());
        assertEquals(password, authDtoRequest.getPassword());

    }

    @Test
    void noArgsConstructorTest() {
        AuthDtoRequest authDtoRequest = new AuthDtoRequest();

        assertNull(authDtoRequest.getUsername());
        assertNull(authDtoRequest.getPassword());
    }
}
