package com.nikolabojanic.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AuthDTORequestTest {

    @Test
    void getterAndSetterTest() {
        AuthDTORequest authDTORequest = new AuthDTORequest();
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);

        authDTORequest.setPassword(password);
        authDTORequest.setUsername(username);

        assertEquals(username, authDTORequest.getUsername());
        assertEquals(password, authDTORequest.getPassword());
    }

    @Test
    void allArgsConstructorTest() {
        //given
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);
        //when
        AuthDTORequest authDTORequest = new AuthDTORequest(username, password);
        //then
        assertEquals(username, authDTORequest.getUsername());
        assertEquals(password, authDTORequest.getPassword());

    }

    @Test
    void noArgsConstructorTest() {
        AuthDTORequest authDTORequest = new AuthDTORequest();

        assertNull(authDTORequest.getUsername());
        assertNull(authDTORequest.getPassword());
    }
}
