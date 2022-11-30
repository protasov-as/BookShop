package com.example.MyBookShopApp.dto;

public class BookRatingValue {
    private String bookId;
    private Integer value;

    public BookRatingValue(String bookId, Integer value) {
        this.bookId = bookId;
        this.value = value;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}
