package com.nekoimi.standalone.framework.security.token;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

/**
 * nekoimi  2022/3/14 11:58
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;
    private final String newToken;
    private final UserDetails subject;
    private final boolean refresh;

    public JwtAuthenticationToken(String token) {
        super(Collections.emptyList());
        this.token = token;
        this.newToken = token;
        this.subject = null;
        this.refresh = false;
        super.setAuthenticated(false);
    }

    public JwtAuthenticationToken(String token, String newToken, UserDetails subject, boolean refresh) {
        super(subject.getAuthorities());
        this.token = token;
        this.newToken = newToken;
        this.subject = subject;
        this.refresh = refresh;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return subject;
    }

    public String getToken() {
        return token;
    }

    public String getNewToken() {
        return newToken;
    }

    public boolean isRefresh() {
        return refresh;
    }
}
