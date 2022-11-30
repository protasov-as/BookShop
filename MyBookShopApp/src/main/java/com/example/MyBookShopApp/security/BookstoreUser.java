package com.example.MyBookShopApp.security;

import com.example.MyBookShopApp.struct.book.review.BookReviewEntity;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class BookstoreUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private Integer balance;
    private Integer hash;
    private LocalDateTime regTime;

    @OneToMany(mappedBy = "user")
    private List<BookReviewEntity> reviews = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getBalance() {
        return balance;
    }

    public void setBalance(Integer balance) {
        this.balance = balance;
    }

    public Integer getHash() {
        return hash;
    }

    public void setHash(Integer hash) {
        this.hash = hash;
    }

    public LocalDateTime getRegTime() {
        return regTime;
    }

    public void setRegTime(LocalDateTime regTime) {
        this.regTime = regTime;
    }

    public List<BookReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(List<BookReviewEntity> reviews) {
        this.reviews = reviews;
    }
}
