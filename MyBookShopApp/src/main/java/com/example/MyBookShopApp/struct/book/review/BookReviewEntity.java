package com.example.MyBookShopApp.struct.book.review;

import com.example.MyBookShopApp.security.BookstoreUser;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "book_review")
public class BookReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(columnDefinition = "INT NOT NULL")
    private int bookId;

//    @Column(columnDefinition = "INT NOT NULL")
//    private int userId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private BookstoreUser user;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime time;

    @Column(columnDefinition = "TEXT NOT NULL")
    private String text;

    @OneToMany(mappedBy = "review")
    private List<BookReviewLikeEntity> reviewLikesList = new ArrayList<>();

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

    public BookstoreUser getUser() {
        return user;
    }

    public void setUser(BookstoreUser user) {
        this.user = user;
    }

    //    public int getUserId() {
//        return userId;
//    }
//
//    public void setUserId(int userId) {
//        this.userId = userId;
//    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<BookReviewLikeEntity> getReviewLikesList() {
        return reviewLikesList;
    }

    public void setReviewLikesList(List<BookReviewLikeEntity> reviewLikesList) {
        this.reviewLikesList = reviewLikesList;
    }

    public String formattedDateTime() {
        if (time != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return time.format(formatter);
        } else {
            return "-";
        }
    }

    public Integer countLikes() {
        return (int) reviewLikesList.stream().filter(r -> r.getValue() == 1).count();
    }

    public Integer countDislikes() {
        return (int) reviewLikesList.stream().filter(r -> r.getValue() == -1).count();
    }

    public Integer countRating() {
        return reviewLikesList.stream().mapToInt(r -> r.getValue()).sum();
    }
}
