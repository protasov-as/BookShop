package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.data.BookstoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final BookstoreUserRepository bookstoreUserRepository;

    @Autowired
    public CustomAuthenticationProvider(BookstoreUserRepository bookstoreUserRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        String userName = authentication.getName();
        String password = authentication.getCredentials().toString();
        BookstoreUser myUser = bookstoreUserRepository.findBookstoreUserByEmail(userName);
        if (myUser == null) {
            throw new BadCredentialsException("Unknown user " + userName);
        }
        if (!BCrypt.checkpw(password, myUser.getPassword())) {
            throw new BadCredentialsException("Bad password");
        }
        BookstoreUserDetails userDetails = new BookstoreUserDetails(myUser);
        return new UsernamePasswordAuthenticationToken(
                userDetails, password, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
