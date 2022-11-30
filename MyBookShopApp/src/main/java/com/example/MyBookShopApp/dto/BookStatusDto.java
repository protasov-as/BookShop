package com.example.MyBookShopApp.dto;

public class BookStatusDto {
    private String booksIds;
    private String status;

    public BookStatusDto(String booksIds, String status) {
        this.booksIds = booksIds;
        this.status = status;
    }

    public String getBooksIds() {
        return booksIds;
    }

    public void setBooksIds(String booksIds) {
        this.booksIds = booksIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
