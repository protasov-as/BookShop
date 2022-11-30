package com.example.MyBookShopApp.struct.book.rating;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_rating")
public class BookRatingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "INT NOT NULL")
    private int bookId;

    @Column(columnDefinition = "INT NOT NULL")
    private int rating;

    @Column(columnDefinition = "INT NOT NULL")
    private int userId;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
