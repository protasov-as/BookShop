package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.struct.author.AuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorEntityRepository extends JpaRepository<AuthorEntity, Integer> {

    Optional<AuthorEntity> findBySlug(String slug);
}
