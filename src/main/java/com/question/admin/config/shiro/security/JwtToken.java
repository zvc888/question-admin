package com.question.admin.config.shiro.security;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.HostAuthenticationToken;

public class JwtToken implements HostAuthenticationToken {

    /**
     * 密钥
     */
    private String token;
    private String host;
    public JwtToken(String token) {
        this(token, null);
    }
    public JwtToken(String token, String host) {
        this.token = token;
        this.host = host;
    }
    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public String getHost() {
        return host;
    }
}
