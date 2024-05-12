package com.nikolabojanic.config.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import java.time.Duration;
import javax.crypto.SecretKey;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

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

    /**
     * This method takes a key represented as a String and builds an OctetSequenceKey
     * using the provided key bytes and the specified algorithm. The resulting OctetSequenceKey
     * is then converted to a SecretKey and set as the key for the current instance.
     *
     * @param key Represent a signing key.
     */
    public void setKey(String key) {
        OctetSequenceKey jwk = new OctetSequenceKey.Builder(key.getBytes())
            .algorithm(algorithm).build();
        this.key = jwk.toSecretKey();
    }
}
