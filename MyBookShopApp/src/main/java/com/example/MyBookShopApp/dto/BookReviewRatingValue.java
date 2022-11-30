package com.example.MyBookShopApp.dto;

public class BookReviewRatingValue {
    private short value;
    private Integer reviewid;

    public BookReviewRatingValue(short value, Integer reviewId) {
        this.value = value;
        this.reviewid = reviewId;
    }

    public short getValue() {
        return value;
    }

    public void setValue(short value) {
        this.value = value;
    }

    public Integer getReviewid() {
        return reviewid;
    }

    public void setReviewid(Integer reviewid) {
        this.reviewid = reviewid;
    }
}
