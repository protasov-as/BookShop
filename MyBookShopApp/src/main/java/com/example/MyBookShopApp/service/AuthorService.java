package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.Author;
import com.example.MyBookShopApp.data.AuthorEntityRepository;
import com.example.MyBookShopApp.struct.author.AuthorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private JdbcTemplate jdbcTemplate;
    private AuthorEntityRepository authorEntityRepository;

    @Autowired
    public AuthorService(JdbcTemplate jdbcTemplate, AuthorEntityRepository authorEntityRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.authorEntityRepository = authorEntityRepository;
    }

    public AuthorEntity getAuthorEntityBySlug(String slug) {
        return authorEntityRepository.findBySlug(slug).get();
    }

    public Map<String, List<Author>> getAuthorsMap() {
        List<Author> authors = jdbcTemplate.query("SELECT * FROM authors",(ResultSet rs, int rowNum) -> {
            Author author = new Author();
            author.setId(rs.getInt("id"));
            author.setFirstName(rs.getString("first_name"));
            author.setLastName(rs.getString("last_name"));
            return author;
        });

        return authors.stream().collect(Collectors.groupingBy((Author a) -> {return a.getLastName().substring(0,1);}));
    }

    public Map<String, List<AuthorEntity>> getAuthorMap() {
        List<AuthorEntity> authors = jdbcTemplate.query("SELECT * FROM author",(ResultSet rs, int rowNum) -> {
            AuthorEntity author = new AuthorEntity();
            author.setId(rs.getInt("id"));
            author.setName(rs.getString("name"));
            author.setDescription(rs.getString("description"));
            author.setPhoto(rs.getString("photo"));
            author.setSlug(rs.getString("slug"));
            return author;
        });

        return authors.stream().collect(Collectors.groupingBy((AuthorEntity a) -> {return a.getName().substring(0,1);}));
    }
}
