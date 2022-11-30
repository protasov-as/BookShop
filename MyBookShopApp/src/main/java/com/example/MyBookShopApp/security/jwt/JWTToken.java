package com.example.MyBookShopApp.security.jwt;

public class JWTToken {

    private String token;
    private long ttl;

    public JWTToken(String token, long ttl) {
        this.token = token;
        this.ttl = ttl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    @Override
    public String toString() {
        return getToken();
    }
}
