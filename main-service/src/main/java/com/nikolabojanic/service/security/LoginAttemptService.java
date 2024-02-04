package com.nikolabojanic.service.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoginAttemptService {
    @Autowired
    private HttpServletRequest request;
    @Getter
    private LoadingCache<String, Integer> attemptsCache;

    /**
     * Constructs a new LoginAttemptService with a cache for tracking login attempts.
     */
    public LoginAttemptService() {
        attemptsCache = CacheBuilder.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build(
            new CacheLoader<String, Integer>() {
                @Override
                public Integer load(String key) throws Exception {
                    return 0;
                }
            }
        );
    }

    /**
     * Records a failed login attempt for the IP address.
     *
     * @param key The key representing the entity for which the login attempt is recorded.
     */
    public void loginFailed(final String key) {
        log.warn("Login failed for IP: {}", getClientIp());
        int attempts;
        try {
            attempts = attemptsCache.get(key);
        } catch (ExecutionException e) {
            attempts = 0;
        }
        attempts++;
        attemptsCache.put(key, attempts);
    }

    /**
     * Checks if the entity associated with the client's IP address is blocked due to too many failed login attempts.
     *
     * @return True if the entity is blocked, false otherwise.
     */
    public boolean isBlocked() {
        int maxAttempts = 3;
        try {
            if (attemptsCache.get(getClientIp()) >= maxAttempts) {
                log.warn("Blocked machine IP: {}", getClientIp());
                return true;
            } else {
                return false;
            }
        } catch (ExecutionException e) {
            return false;
        }
    }

    private String getClientIp() {
        return request.getRemoteAddr();
    }

}
