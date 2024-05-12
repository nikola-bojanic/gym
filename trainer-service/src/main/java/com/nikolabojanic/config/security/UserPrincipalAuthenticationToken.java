package com.nikolabojanic.config.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

public class UserPrincipalAuthenticationToken extends AbstractAuthenticationToken {
    private final UserPrincipal userPrincipal;

    /**
     * Creates an access token based on the provided UserPrincipal.
     * It sets the authentication status to "authenticated" and initializes the token with the
     * authorities obtained from the UserPrincipal.
     *
     * @param userPrincipal The UserPrincipal representing the authenticated user.
     */
    public UserPrincipalAuthenticationToken(UserPrincipal userPrincipal) {
        super(userPrincipal.getAuthorities());
        this.userPrincipal = userPrincipal;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public UserPrincipal getPrincipal() {
        return userPrincipal;
    }
}
