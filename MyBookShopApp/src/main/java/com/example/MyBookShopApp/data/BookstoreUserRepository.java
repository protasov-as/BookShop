package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.security.BookstoreUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookstoreUserRepository extends JpaRepository<BookstoreUser, Integer> {

    BookstoreUser findBookstoreUserByEmail(String email);


}
