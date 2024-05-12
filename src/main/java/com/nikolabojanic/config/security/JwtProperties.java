package com.nikolabojanic.config.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.time.Duration;

@Getter
@Setter
@Configuration
@ConfigurationProperties("app.jwt")
public class JwtProperties {
    private SecretKey key;
    private String issuer;
    private JWSAlgorithm algorithm;
    private Duration expiresIn;

    public void setAlgorithm(String algorithm) {
        this.algorithm = JWSAlgorithm.parse(algorithm);
    }

    public void setKey(String key) {
        OctetSequenceKey jwk = new OctetSequenceKey.Builder(key.getBytes())
                .algorithm(algorithm).build();
        this.key = jwk.toSecretKey();
    }
}
