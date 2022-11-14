package com.example.MyBookShopApp.dto;

public class SearchWordDto {

    private String content;

    public SearchWordDto(String content) {
        this.content = content;
    }

    public SearchWordDto(){}

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
