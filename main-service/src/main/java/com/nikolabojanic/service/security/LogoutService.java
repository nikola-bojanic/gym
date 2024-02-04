package com.nikolabojanic.service.security;

import com.nikolabojanic.entity.TokenEntity;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@RequiredArgsConstructor
@Service
public class LogoutService implements LogoutHandler {
    private final TokenService tokenService;

    @Override
    public void logout(
        HttpServletRequest request,
        HttpServletResponse response,
        Authentication authentication
    ) {
        String requestToken = extractTokenFromRequest(request).orElse(null);
        if (requestToken == null) {
            return;
        }
        TokenEntity storedToken = tokenService.findByData(requestToken);
        if (storedToken != null) {
            storedToken.setRevoked(true);
            storedToken.setExpired(true);
            log.warn("Revoking token on logout for user {}", storedToken.getUser().getUsername());
            tokenService.save(storedToken);
        }
    }

    private Optional<String> extractTokenFromRequest(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return Optional.of(token.substring(7));
        }
        return Optional.empty();
    }
}
