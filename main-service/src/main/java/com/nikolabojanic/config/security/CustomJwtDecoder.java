package com.nikolabojanic.config.security;

import com.nikolabojanic.exception.ScNotAuthorizedException;
import com.nikolabojanic.service.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

@RequiredArgsConstructor
@Slf4j
public class CustomJwtDecoder implements JwtDecoder {
    private final JwtDecoder jwtDecoder;
    private final TokenService tokenService;

    @Override
    public Jwt decode(String token) throws JwtException {
        if (tokenService.isTokenValid(token)) {
            try {
                return jwtDecoder.decode(token);
            } catch (JwtException e) {
                log.error(e.getMessage());
                throw new ScNotAuthorizedException("Cannot authorize request");
            }
        } else {
            throw new ScNotAuthorizedException("Cannot authorize request");
        }
    }
}
