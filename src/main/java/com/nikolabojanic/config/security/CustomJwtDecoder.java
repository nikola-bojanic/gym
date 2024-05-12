package com.nikolabojanic.config.security;

import com.nikolabojanic.service.security.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {
    private final JwtDecoder jwtDecoder;
    private final TokenService tokenService;

    @Override
    public Jwt decode(String token) throws JwtException {
        if (tokenService.isTokenValid(token)) {
            return jwtDecoder.decode(token);
        } else {
            throw new JwtException("Cannot authorize request");
        }
    }
}
