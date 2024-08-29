package com.yolo.chef.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;

public class CustomJwtAuthenticationToken extends AbstractAuthenticationToken {

    private final Jwt jwt;
    private final String principalClaim;

    public CustomJwtAuthenticationToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities, String principalClaim) {
        super(authorities);
        this.jwt = jwt;
        this.principalClaim = principalClaim;
    }

    @Override
    public Object getCredentials() {
        return jwt;
    }

    @Override
    public Object getPrincipal() {
        return principalClaim;
    }

    public Jwt getJwt() {
        return jwt;
    }
}
