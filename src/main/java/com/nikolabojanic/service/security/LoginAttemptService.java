package com.nikolabojanic.service.security;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class LoginAttemptService {
    @Autowired
    private HttpServletRequest request;
    @Getter
    private LoadingCache<String, Integer> attemptsCache;

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

    public boolean isBlocked() {
        log.warn("Blocked machine IP: {}", getClientIp());
        int maxAttempts = 3;
        try {
            return attemptsCache.get(getClientIp()) >= maxAttempts;
        } catch (ExecutionException e) {
            return false;
        }
    }

    private String getClientIp() {
        return request.getRemoteAddr();
    }

}
