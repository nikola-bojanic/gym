package com.nikolabojanic.config.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtIssuer {
    private final JwtProperties jwtProperties;

    /**
     * Generates a JSON Web Token (JWT) with the specified user ID and custom claims.
     *
     * @param id     The user ID to include in the JWT.
     * @param claims Custom claims to include in the JWT payload.
     * @return The generated JWT as a serialized string.
     * @throws RuntimeException If an error occurs during JWT generation.
     */
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
            .claim("username", claims.get("username"))
            .claim("roles", claims.get("roles"))
            .issuer(issuer)
            .issueTime(Date.from(issuedAt))
            .expirationTime(Date.from(expirationTime));

        claims.forEach(builder::claim);

        return builder.build();
    }
}

