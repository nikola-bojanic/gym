package com.nikolabojanic.config.security;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;
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
    private JWSAlgorithm algorithm;

    public void setAlgorithm(String algorithm) {
        this.algorithm = JWSAlgorithm.parse(algorithm);
    }

    /**
     * Sets the key used for signing the JWT.
     *
     * @param key The key to set for signing the JWT.
     */
    public void setKey(String key) {
        OctetSequenceKey jwk = new OctetSequenceKey.Builder(key.getBytes())
            .algorithm(algorithm).build();
        this.key = jwk.toSecretKey();
    }
}
