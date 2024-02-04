package com.nikolabojanic.converter;

import com.nikolabojanic.config.security.UserPrincipal;
import com.nikolabojanic.config.security.UserPrincipalAuthenticationToken;
import com.nimbusds.jose.shaded.gson.internal.LinkedTreeMap;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtConverter implements Converter<Jwt, UserPrincipalAuthenticationToken> {
    @Override
    public UserPrincipalAuthenticationToken convert(Jwt source) {
        List<LinkedTreeMap<String, String>> roles = source.getClaim("roles");
        List<GrantedAuthority> authorities = new ArrayList<>();
        roles.forEach(entry -> entry.values().forEach(r -> authorities.add(new SimpleGrantedAuthority(r))));
        log.info("Converted a JWT to user principal.");
        UserPrincipal userPrincipal = new UserPrincipal(
            Long.parseLong(source.getSubject()),
            source.getClaim("username"),
            null,
            authorities);
        return new UserPrincipalAuthenticationToken(userPrincipal);
    }
}
