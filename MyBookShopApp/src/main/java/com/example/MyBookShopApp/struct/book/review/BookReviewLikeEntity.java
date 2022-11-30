package com.example.MyBookShopApp.struct.book.review;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "book_review_like")
public class BookReviewLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

//    @Column(columnDefinition = "INT NOT NULL")
//    private int reviewId;

    @Column(columnDefinition = "INT NOT NULL")
    private int userId;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    @JsonIgnore
    private BookReviewEntity review;

    @Column(columnDefinition = "TIMESTAMP NOT NULL")
    private LocalDateTime time;

    @Column(columnDefinition = "SMALLINT NOT NULL")
    private short value;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public int getReviewId() {
//        return reviewId;
//    }
//
//    public void setReviewId(int reviewId) {
//        this.reviewId = reviewId;
//    }

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

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    public BookReviewEntity getReview() {
        return review;
    }

    public void setReview(BookReviewEntity review) {
        this.review = review;
    }
}
