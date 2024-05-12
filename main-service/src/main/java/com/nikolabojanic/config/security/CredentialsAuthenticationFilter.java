package com.nikolabojanic.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikolabojanic.dto.AuthDtoRequest;
import com.nikolabojanic.dto.AuthDtoResponse;
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
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

@Component
public class CredentialsAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private final LoginAttemptService loginAttemptService;
    private final JwtIssuer jwtIssuer;
    private final TokenService tokenService;
    private final UserService userService;

    /**
     * Constructs a new CredentialsAuthenticationFilter.
     *
     * @param loginAttemptService   The service for managing login attempts.
     * @param jwtIssuer             The JWT issuer for creating JWT tokens.
     * @param tokenService          The service for managing tokens.
     * @param userService           The service for managing user-related operations.
     * @param authenticationManager The AuthenticationManager used for authentication.
     */
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
        AuthDtoRequest authDtoRequest = new ObjectMapper().readValue(requestBody, AuthDtoRequest.class);

        String username = authDtoRequest.getUsername();
        String password = authDtoRequest.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(username, password);
        return getAuthenticationManager().authenticate(authenticationToken);

    }

    @Override
    protected void successfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain,
        Authentication authResult) throws IOException {

        loginAttemptService.getAttemptsCache().refresh(request.getRemoteAddr());
        UserPrincipal principal = (UserPrincipal) authResult.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", principal.getUsername());
        claims.put("roles", principal.getAuthorities());
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
        AuthDtoResponse authDtoResponse = new AuthDtoResponse(accessToken);
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(authDtoResponse);
        response.setContentType("application/json");
        response.getWriter().write(jsonResponse);
    }
}