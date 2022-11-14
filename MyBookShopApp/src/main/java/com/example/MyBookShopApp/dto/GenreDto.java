package com.example.MyBookShopApp.dto;

import com.example.MyBookShopApp.struct.genre.GenreEntity;

import java.util.ArrayList;
import java.util.List;

public class GenreDto {

    private GenreEntity genreEntity;
    private Integer count;
    private List<GenreDto> subGenres = new ArrayList<>();

    public GenreEntity getGenreEntity() {
        return genreEntity;
    }

    public void setGenreEntity(GenreEntity genreEntity) {
        this.genreEntity = genreEntity;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<GenreDto> getSubGenres() {
        return subGenres;
    }

    public void setSubGenres(List<GenreDto> subGenres) {
        this.subGenres = subGenres;
    }
}
