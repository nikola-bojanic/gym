package com.nikolabojanic.config.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtIssuer {
    private final JwtProperties jwtProperties;

    public String generateJwt(Long id, Map<String, Object> claims) {
        SecretKey key = jwtProperties.getKey();
        JWSAlgorithm algorithm = jwtProperties.getAlgorithm();

        JWSHeader header = new JWSHeader(algorithm);
        JWTClaimsSet claimsSet = buildClaimsSet(id, claims);

        SignedJWT jwt = new SignedJWT(header, claimsSet);
        try {
            MACSigner signer = new MACSigner(key);
            jwt.sign(signer);
        } catch (JOSEException e) {
            throw new RuntimeException("Unable to generate JWT", e);
        }

        return jwt.serialize();
    }

    private JWTClaimsSet buildClaimsSet(Long id, Map<String, Object> claims) {
        String issuer = jwtProperties.getIssuer();
        Instant issuedAt = Instant.now();
        Instant expirationTime = Instant.now().plus(Duration.of(1, ChronoUnit.DAYS));

        JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder()
                .subject(id.toString())
                .issuer(issuer)
                .issueTime(Date.from(issuedAt))
                .expirationTime(Date.from(expirationTime));

        if (claims.containsKey("usr")) {
            builder.claim("usr", claims.get("usr"));
        }

        claims.forEach(builder::claim);

        return builder.build();
    }
}

