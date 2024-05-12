package com.nikolabojanic.converter;

import com.nikolabojanic.config.security.UserPrincipal;
import com.nikolabojanic.config.security.UserPrincipalAuthenticationToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtConverter implements Converter<Jwt, UserPrincipalAuthenticationToken> {
    @Override
    public UserPrincipalAuthenticationToken convert(Jwt source) {
        log.info("Converted a JWT to user principal.");
        UserPrincipal userPrincipal = new UserPrincipal(
                Long.parseLong(source.getSubject()),
                source.getClaim("usr"),
                null);
        return new UserPrincipalAuthenticationToken(userPrincipal);
    }
}
