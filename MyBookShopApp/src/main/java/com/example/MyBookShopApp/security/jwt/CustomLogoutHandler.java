package com.example.MyBookShopApp.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class CustomLogoutHandler implements LogoutHandler {

    private final JWTTokenBlackList jwtTokenBlackList;

    @Value("${app.jwtToken.ttl}")
    long tokenTtl;

    @Autowired
    public CustomLogoutHandler(JWTTokenBlackList jwtTokenBlackList) {
        this.jwtTokenBlackList = jwtTokenBlackList;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    JWTToken jwtToken = new JWTToken(cookie.getValue(), tokenTtl);
                    jwtTokenBlackList.addToken(jwtToken);
                }
            }
        }
    }
}
