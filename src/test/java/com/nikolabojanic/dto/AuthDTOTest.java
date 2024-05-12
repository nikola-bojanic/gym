package com.nikolabojanic.dto;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AuthDTOTest {

    @Test
    void testGetterAndSetter() {
        AuthDTO authDTO = new AuthDTO();
        String username = RandomStringUtils.randomAlphabetic(8, 10);
        String password = RandomStringUtils.randomAlphabetic(8, 10);

        authDTO.setPassword(password);
        authDTO.setUsername(username);

        assertEquals(username, authDTO.getUsername());
        assertEquals(password, authDTO.getPassword());
    }

    @Test
    void testNoArgsConstructor() {
        AuthDTO authDTO = new AuthDTO();

        assertNull(authDTO.getUsername());
        assertNull(authDTO.getPassword());
    }
}
