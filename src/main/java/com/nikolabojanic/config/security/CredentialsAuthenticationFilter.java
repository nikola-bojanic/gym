package com.nikolabojanic.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikolabojanic.dto.AuthDTORequest;
import com.nikolabojanic.dto.AuthDTOResponse;
import com.nikolabojanic.entity.TokenEntity;
import com.nikolabojanic.entity.TokenType;
import com.nikolabojanic.entity.UserEntity;
import com.nikolabojanic.service.UserService;
import com.nikolabojanic.service.security.LoginAttemptService;
import com.nikolabojanic.service.security.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class CredentialsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final LoginAttemptService loginAttemptService;
    private final JwtIssuer jwtIssuer;
    private final TokenService tokenService;
    private final UserService userService;

    public CredentialsAuthenticationFilter(
            LoginAttemptService loginAttemptService,
            JwtIssuer jwtIssuer,
            TokenService tokenService,
            UserService userService,
            @Lazy AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher("/api/v1/users/login", "POST"));
        super.setAuthenticationManager(authenticationManager);
        this.loginAttemptService = loginAttemptService;
        this.jwtIssuer = jwtIssuer;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException, IOException {
        InputStream requestBody = request.getInputStream();
        AuthDTORequest authDTORequest = new ObjectMapper().readValue(requestBody, AuthDTORequest.class);

        String username = authDTORequest.getUsername();
        String password = authDTORequest.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);
        return getAuthenticationManager().authenticate(authenticationToken);

    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException {

        loginAttemptService.getAttemptsCache().refresh(request.getRemoteAddr());
        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("usr", principal.getUsername());
        String accessToken = jwtIssuer.generateJwt(principal.getId(), claims);
        UserEntity user = userService.findByUsername(principal.getUsername());
        tokenService.deleteInvalidUserTokens(user.getId(), accessToken);
        TokenEntity token = new TokenEntity(
                accessToken,
                TokenType.BEARER,
                false,
                false,
                user);
        tokenService.save(token);
        AuthDTOResponse authDTOResponse = new AuthDTOResponse(accessToken);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(authDTOResponse);
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }
}