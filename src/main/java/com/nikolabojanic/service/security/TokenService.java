package com.nikolabojanic.service.security;

import com.nikolabojanic.entity.TokenEntity;
import com.nikolabojanic.exception.ScEntityNotFoundException;
import com.nikolabojanic.repository.TokenRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public TokenEntity save(TokenEntity tokenEntity) {
        log.info("Saved token to db for user: {}", tokenEntity.getUser().getUsername());
        return tokenRepository.save(tokenEntity);
    }

    public TokenEntity findByData(String data) {
        Optional<TokenEntity> exists = tokenRepository.findByData(data);
        if (exists.isPresent()) {
            log.info("Successfully retrieved token");
            return exists.get();
        } else {
            log.error("Attempted to fetch non existing token");
            throw new ScEntityNotFoundException("");
        }
    }

    public Boolean isTokenValid(String data) {
        return tokenRepository.findByData(data)
                .map(t -> !t.isRevoked() && !t.isExpired())
                .orElse(false);
    }

    public void deleteInvalidUserTokens(Long id, String data) {
        log.info("Deleting old tokens for user: {}", id);
        tokenRepository.deleteInvalidTokens(id, data);
    }
}
