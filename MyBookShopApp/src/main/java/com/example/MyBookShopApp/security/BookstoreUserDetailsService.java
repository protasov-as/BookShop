package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.data.BookstoreUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BookstoreUserDetailsService implements UserDetailsService {

    private final BookstoreUserRepository bookstoreUserRepository;

    @Autowired
    public BookstoreUserDetailsService(BookstoreUserRepository bookstoreUserRepository) {
        this.bookstoreUserRepository = bookstoreUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        BookstoreUser bookstoreUser = bookstoreUserRepository.findBookstoreUserByEmail(s);
        if(bookstoreUser != null) {
            return new BookstoreUserDetails(bookstoreUser);
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public BookstoreUser getAuthenticatedUserByEmail(String email) throws UsernameNotFoundException {
        return Optional.ofNullable(bookstoreUserRepository
                        .findBookstoreUserByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));
    }

    public BookstoreUser processOAuthPostLogin(String email, String name) {
        BookstoreUser existingUser = bookstoreUserRepository.findBookstoreUserByEmail(email);
        if (existingUser == null) {
            BookstoreUser newUser = new BookstoreUser();
            newUser.setEmail(email);
            newUser.setName(name);
            newUser.setRegTime(LocalDateTime.now());
            newUser.setHash(0);
            newUser.setBalance(0);
            return bookstoreUserRepository.save(newUser);
        }
        return existingUser;
    }

    public BookstoreUser getCurrentUser() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass().toString());
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass().equals(String.class)) {
            throw new UsernameNotFoundException("!Invalid user");
        }
        if(SecurityContextHolder.getContext().getAuthentication().getPrincipal().getClass().equals(BookstoreUserDetails.class)) {
            BookstoreUserDetails userDetails =
                    (BookstoreUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return userDetails.getBookstoreUser();
        } else {
            CustomOAuth2User customOAuth2User = (CustomOAuth2User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            String email = customOAuth2User.getEmail();
            return getAuthenticatedUserByEmail(email);
        }
    }
}
