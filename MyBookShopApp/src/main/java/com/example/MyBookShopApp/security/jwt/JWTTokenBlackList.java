package com.example.MyBookShopApp.security.jwt;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class JWTTokenBlackList {

    private TreeSet<JWTToken> blackList = new TreeSet<>();

    public TreeSet<JWTToken> getBlackList() {
        return blackList;
    }

    public void addToken(JWTToken token) {
        blackList.add(token);
    }

    public boolean tokenExists(JWTToken token) {
        return blackList.contains(token);
    }

    public JWTToken findTokenByString(String tokenValue) {
        AtomicReference<JWTToken> jwtToken = new AtomicReference<>();
        blackList.forEach(t -> {
            if(Objects.equals(t.getToken(), tokenValue)) {
                jwtToken.set(t);
            }
        });
        return jwtToken.get();
    }
}
