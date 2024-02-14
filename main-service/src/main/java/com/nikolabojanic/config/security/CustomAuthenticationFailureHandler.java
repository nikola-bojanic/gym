package com.nikolabojanic.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikolabojanic.service.security.LoginAttemptService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationFailureHandler implements AuthenticationEntryPoint {
    private final LoginAttemptService loginAttemptService;
    private final ObjectMapper objectMapper;

    @Override
    public void commence(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException authException) throws IOException {
        loginAttemptService.loginFailed(request.getRemoteAddr());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        String message;
        if (loginAttemptService.isBlocked()) {
            message = "Blocked";
        } else {
            message = "Username or password not correct";
        }
        String jsonResponse = objectMapper.writeValueAsString(
            Collections.singletonMap("error", message));
        response.getWriter().write(jsonResponse);
    }
}
